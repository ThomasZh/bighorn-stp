package net.younguard.bighorn.broadcast.adapter.chess;

import java.io.UnsupportedEncodingException;
import java.util.List;

import net.younguard.bighorn.CommandTag;
import net.younguard.bighorn.ErrorCode;
import net.younguard.bighorn.broadcast.service.AccountService;
import net.younguard.bighorn.broadcast.service.GameService;
import net.younguard.bighorn.broadcast.util.BighornApplicationContextUtil;
import net.younguard.bighorn.chess.cmd.GamePlayingQueryPaginationReq;
import net.younguard.bighorn.chess.cmd.GamePlayingQueryPaginationResp;
import net.younguard.bighorn.comm.RequestCommand;
import net.younguard.bighorn.comm.ResponseCommand;
import net.younguard.bighorn.comm.tlv.TlvObject;
import net.younguard.bighorn.comm.util.LogErrorMessage;
import net.younguard.bighorn.domain.AccountBaseInfo;
import net.younguard.bighorn.domain.GameMasterInfo;
import net.younguard.bighorn.domain.GameMemberMasterInfo;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GamePlayingQueryPaginationAdapter
		extends RequestCommand
{
	public GamePlayingQueryPaginationAdapter()
	{
		super();

		this.setTag(CommandTag.GAME_PLAYING_QUERY_PAGINATION_REQUEST);
	}

	@Override
	public RequestCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = (GamePlayingQueryPaginationReq) new GamePlayingQueryPaginationReq().decode(tlv);
		this.setSequence(reqCmd.getSequence());

		return this;
	}

	@Override
	public ResponseCommand execute(IoSession session)
			throws Exception
	{
		short pageNum = reqCmd.getPageNum();
		short pageSize = reqCmd.getPageSize();
		String deviceId = (String) session.getAttribute("deviceId");

		try {
			GameService gameService = BighornApplicationContextUtil.getGameService();
			AccountService accountService = BighornApplicationContextUtil.getAccountService();

			List<GameMasterInfo> games = gameService.queryPlayingPagination(pageNum, pageSize);
			for (GameMasterInfo game : games) {
				List<GameMemberMasterInfo> players = gameService.queryGameMembers(game.getGameId());

				for (GameMemberMasterInfo player : players) {
					AccountBaseInfo account = accountService.query(player.getAccountId());
					player.setNickname(account.getNickname());
					player.setAvatarUrl(account.getAvatarUrl());
				}

				game.setPlayers(players);
			}
			logger.info("commandTag=[" + this.getTag() + "]|ErrorCode=[" + ErrorCode.SUCCESS + "]|sessionId=["
					+ session.getId() + "]|device=[" + deviceId + "]|");

			GamePlayingQueryPaginationResp respCmd = new GamePlayingQueryPaginationResp(this.getSequence(),
					ErrorCode.SUCCESS, games);
			return respCmd;
		} catch (Exception e) {
			logger.warn("commandTag=[" + this.getTag() + "]|ErrorCode=[" + ErrorCode.UNKNOWN_FAILURE + "]|sessionId=["
					+ session.getId() + "]|device=[" + deviceId + "]|" + LogErrorMessage.getFullInfo(e));

			GamePlayingQueryPaginationResp respCmd = new GamePlayingQueryPaginationResp(this.getSequence(),
					ErrorCode.UNKNOWN_FAILURE);
			return respCmd;
		}
	}

	private GamePlayingQueryPaginationReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(GamePlayingQueryPaginationAdapter.class);

}
