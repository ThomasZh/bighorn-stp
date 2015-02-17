package net.younguard.bighorn.broadcast.adapter.chess;

import java.io.UnsupportedEncodingException;
import java.util.List;

import net.younguard.bighorn.CommandTag;
import net.younguard.bighorn.ErrorCode;
import net.younguard.bighorn.broadcast.service.AccountService;
import net.younguard.bighorn.broadcast.service.GameService;
import net.younguard.bighorn.broadcast.util.BighornApplicationContextUtil;
import net.younguard.bighorn.chess.cmd.MyInviteQueryPaginationReq;
import net.younguard.bighorn.chess.cmd.MyInviteQueryPaginationResp;
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

public class MyInviteQueryPaginationAdapter
		extends RequestCommand
{
	public MyInviteQueryPaginationAdapter()
	{
		super();

		this.setTag(CommandTag.GAME_MY_HISTORY_QUERY_PAGINATION_REQUEST);
	}

	@Override
	public RequestCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = (MyInviteQueryPaginationReq) new MyInviteQueryPaginationReq().decode(tlv);
		this.setSequence(reqCmd.getSequence());

		return this;
	}

	@Override
	public ResponseCommand execute(IoSession session)
			throws Exception
	{
		short pageNum = reqCmd.getPageNum();
		short pageSize = reqCmd.getPageSize();
		String accountId = (String) session.getAttribute("accountId");
		String deviceId = (String) session.getAttribute("deviceId");

		try {
			GameService gameService = BighornApplicationContextUtil.getGameService();
			AccountService accountService = BighornApplicationContextUtil.getAccountService();

			List<GameMasterInfo> games = gameService.queryInvitePagination(pageNum, pageSize, accountId);
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

			MyInviteQueryPaginationResp respCmd = new MyInviteQueryPaginationResp(this.getSequence(),
					ErrorCode.SUCCESS, games);
			return respCmd;
		} catch (Exception e) {
			logger.warn("commandTag=[" + this.getTag() + "]|ErrorCode=[" + ErrorCode.UNKNOWN_FAILURE + "]|sessionId=["
					+ session.getId() + "]|device=[" + deviceId + "]|" + LogErrorMessage.getFullInfo(e));

			MyInviteQueryPaginationResp respCmd = new MyInviteQueryPaginationResp(this.getSequence(),
					ErrorCode.UNKNOWN_FAILURE);
			return respCmd;
		}
	}

	private MyInviteQueryPaginationReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(MyInviteQueryPaginationAdapter.class);

}
