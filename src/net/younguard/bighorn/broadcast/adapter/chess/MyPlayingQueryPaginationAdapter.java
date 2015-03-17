package net.younguard.bighorn.broadcast.adapter.chess;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import net.younguard.bighorn.CommandTag;
import net.younguard.bighorn.ErrorCode;
import net.younguard.bighorn.badgenum.BadgeNumService;
import net.younguard.bighorn.broadcast.service.AccountService;
import net.younguard.bighorn.broadcast.service.GameService;
import net.younguard.bighorn.broadcast.util.BighornApplicationContextUtil;
import net.younguard.bighorn.chess.cmd.MyPlayingQueryPaginationReq;
import net.younguard.bighorn.chess.cmd.MyPlayingQueryPaginationResp;
import net.younguard.bighorn.comm.RequestCommand;
import net.younguard.bighorn.comm.ResponseCommand;
import net.younguard.bighorn.comm.tlv.TlvObject;
import net.younguard.bighorn.comm.util.LogErrorMessage;
import net.younguard.bighorn.domain.AccountBaseInfo;
import net.younguard.bighorn.domain.GameMasterInfo;
import net.younguard.bighorn.domain.GameMemberMasterInfo;
import net.younguard.bighorn.domain.MyGameMasterInfo;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyPlayingQueryPaginationAdapter
		extends RequestCommand
{
	public MyPlayingQueryPaginationAdapter()
	{
		super();

		this.setTag(CommandTag.GAME_MY_HISTORY_QUERY_PAGINATION_REQUEST);
	}

	@Override
	public RequestCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = (MyPlayingQueryPaginationReq) new MyPlayingQueryPaginationReq().decode(tlv);
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
			BadgeNumService badgeNumService = BighornApplicationContextUtil.getBadgeNumService();

			List<MyGameMasterInfo> myGames = new ArrayList<MyGameMasterInfo>();
			List<GameMasterInfo> games = gameService.queryPlayingPagination(pageNum, pageSize, accountId);
			for (GameMasterInfo game : games) {
				List<GameMemberMasterInfo> players = gameService.queryGameMembers(game.getGameId());

				for (GameMemberMasterInfo player : players) {
					AccountBaseInfo account = accountService.query(player.getAccountId());
					player.setNickname(account.getNickname());
					player.setAvatarUrl(account.getAvatarUrl());
				}

				MyGameMasterInfo myGame = new MyGameMasterInfo();
				myGame.setGameId(game.getGameId());
				myGame.setPlayers(players);
				myGame.setCreateTime(game.getCreateTime());
				myGame.setLastStep(game.getLastStep());
				myGame.setState(game.getState());
				myGame.setTimeRule(game.getTimeRule());
				myGame.setWinnerId(game.getWinnerId());
				myGame.setLastUpdateTime(game.getLastUpdateTime());
				short badgeNum = badgeNumService.queryHistoryNum(accountId);
				myGame.setBadgeNum(badgeNum);

				myGames.add(myGame);
			}
			logger.info("commandTag=[" + this.getTag() + "]|ErrorCode=[" + ErrorCode.SUCCESS + "]|sessionId=["
					+ session.getId() + "]|device=[" + deviceId + "]|");

			MyPlayingQueryPaginationResp respCmd = new MyPlayingQueryPaginationResp(this.getSequence(),
					ErrorCode.SUCCESS, myGames);
			return respCmd;
		} catch (Exception e) {
			logger.warn("commandTag=[" + this.getTag() + "]|ErrorCode=[" + ErrorCode.UNKNOWN_FAILURE + "]|sessionId=["
					+ session.getId() + "]|device=[" + deviceId + "]|" + LogErrorMessage.getFullInfo(e));

			MyPlayingQueryPaginationResp respCmd = new MyPlayingQueryPaginationResp(this.getSequence(),
					ErrorCode.UNKNOWN_FAILURE);
			return respCmd;
		}
	}

	private MyPlayingQueryPaginationReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(MyPlayingQueryPaginationAdapter.class);

}
