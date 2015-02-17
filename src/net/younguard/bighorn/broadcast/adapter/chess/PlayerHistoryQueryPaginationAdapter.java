package net.younguard.bighorn.broadcast.adapter.chess;

import java.io.UnsupportedEncodingException;
import java.util.List;

import net.younguard.bighorn.CommandTag;
import net.younguard.bighorn.ErrorCode;
import net.younguard.bighorn.broadcast.service.AccountService;
import net.younguard.bighorn.broadcast.service.GameService;
import net.younguard.bighorn.broadcast.util.BighornApplicationContextUtil;
import net.younguard.bighorn.chess.cmd.PlayerHistoryQueryPaginationReq;
import net.younguard.bighorn.chess.cmd.PlayerHistoryQueryPaginationResp;
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

public class PlayerHistoryQueryPaginationAdapter
		extends RequestCommand
{
	public PlayerHistoryQueryPaginationAdapter()
	{
		super();

		this.setTag(CommandTag.GAME_PLAYER_HISTORY_QUERY_PAGINATION_RESPONSE);
	}

	@Override
	public RequestCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = (PlayerHistoryQueryPaginationReq) new PlayerHistoryQueryPaginationReq().decode(tlv);
		this.setSequence(reqCmd.getSequence());

		return this;
	}

	@Override
	public ResponseCommand execute(IoSession session)
			throws Exception
	{
		short pageNum = reqCmd.getPageNum();
		short pageSize = reqCmd.getPageSize();
		String accountId = reqCmd.getPlayerId();
		String deviceId = (String) session.getAttribute("deviceId");

		try {
			GameService gameService = BighornApplicationContextUtil.getGameService();
			AccountService accountService = BighornApplicationContextUtil.getAccountService();

			List<GameMasterInfo> games = gameService.queryHistoryPagination(pageNum, pageSize, accountId);
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

			PlayerHistoryQueryPaginationResp respCmd = new PlayerHistoryQueryPaginationResp(this.getSequence(),
					ErrorCode.SUCCESS, games);
			return respCmd;
		} catch (Exception e) {
			logger.warn("commandTag=[" + this.getTag() + "]|ErrorCode=[" + ErrorCode.UNKNOWN_FAILURE + "]|sessionId=["
					+ session.getId() + "]|device=[" + deviceId + "]|" + LogErrorMessage.getFullInfo(e));

			PlayerHistoryQueryPaginationResp respCmd = new PlayerHistoryQueryPaginationResp(this.getSequence(),
					ErrorCode.UNKNOWN_FAILURE);
			return respCmd;
		}
	}

	private PlayerHistoryQueryPaginationReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(PlayerHistoryQueryPaginationAdapter.class);

}
