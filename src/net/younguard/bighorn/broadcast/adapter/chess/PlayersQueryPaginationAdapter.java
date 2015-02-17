package net.younguard.bighorn.broadcast.adapter.chess;

import java.io.UnsupportedEncodingException;
import java.util.List;

import net.younguard.bighorn.CommandTag;
import net.younguard.bighorn.ErrorCode;
import net.younguard.bighorn.broadcast.service.AccountService;
import net.younguard.bighorn.broadcast.service.GameService;
import net.younguard.bighorn.broadcast.util.BighornApplicationContextUtil;
import net.younguard.bighorn.chess.cmd.PlayersQueryPaginationReq;
import net.younguard.bighorn.chess.cmd.PlayersQueryPaginationResp;
import net.younguard.bighorn.comm.RequestCommand;
import net.younguard.bighorn.comm.ResponseCommand;
import net.younguard.bighorn.comm.tlv.TlvObject;
import net.younguard.bighorn.comm.util.LogErrorMessage;
import net.younguard.bighorn.domain.AccountBaseInfo;
import net.younguard.bighorn.domain.PlayerSummary;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlayersQueryPaginationAdapter
		extends RequestCommand
{
	public PlayersQueryPaginationAdapter()
	{
		super();

		this.setTag(CommandTag.GAME_PLAYERS_QUERY_PAGINATION_REQUEST);
	}

	@Override
	public RequestCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = (PlayersQueryPaginationReq) new PlayersQueryPaginationReq().decode(tlv);
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

			List<PlayerSummary> players = gameService.queryPlayersPagination(pageNum, pageSize);
			for (PlayerSummary player : players) {
				AccountBaseInfo account = accountService.query(player.getAccountId());
				player.setNickname(account.getNickname());
				player.setAvatarUrl(account.getAvatarUrl());
			}
			logger.info("commandTag=[" + this.getTag() + "]|ErrorCode=[" + ErrorCode.SUCCESS + "]|sessionId=["
					+ session.getId() + "]|device=[" + deviceId + "]|");

			PlayersQueryPaginationResp respCmd = new PlayersQueryPaginationResp(this.getSequence(), ErrorCode.SUCCESS,
					players);
			return respCmd;
		} catch (Exception e) {
			logger.warn("commandTag=[" + this.getTag() + "]|ErrorCode=[" + ErrorCode.UNKNOWN_FAILURE + "]|sessionId=["
					+ session.getId() + "]|device=[" + deviceId + "]|" + LogErrorMessage.getFullInfo(e));

			PlayersQueryPaginationResp respCmd = new PlayersQueryPaginationResp(this.getSequence(),
					ErrorCode.UNKNOWN_FAILURE);
			return respCmd;
		}
	}

	private PlayersQueryPaginationReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(PlayersQueryPaginationAdapter.class);

}
