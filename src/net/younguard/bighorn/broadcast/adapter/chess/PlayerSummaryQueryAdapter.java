package net.younguard.bighorn.broadcast.adapter.chess;

import java.io.UnsupportedEncodingException;

import net.younguard.bighorn.CommandTag;
import net.younguard.bighorn.ErrorCode;
import net.younguard.bighorn.broadcast.service.AccountService;
import net.younguard.bighorn.broadcast.service.PlayerService;
import net.younguard.bighorn.broadcast.util.BighornApplicationContextUtil;
import net.younguard.bighorn.chess.cmd.PlayerSummaryQueryReq;
import net.younguard.bighorn.chess.cmd.PlayerSummaryQueryResp;
import net.younguard.bighorn.comm.RequestCommand;
import net.younguard.bighorn.comm.ResponseCommand;
import net.younguard.bighorn.comm.tlv.TlvObject;
import net.younguard.bighorn.comm.util.LogErrorMessage;
import net.younguard.bighorn.domain.AccountBaseInfo;
import net.younguard.bighorn.domain.PlayerDetailInfo;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlayerSummaryQueryAdapter
		extends RequestCommand
{
	public PlayerSummaryQueryAdapter()
	{
		super();

		this.setTag(CommandTag.GAME_PLAYERS_QUERY_PAGINATION_REQUEST);
	}

	@Override
	public RequestCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = (PlayerSummaryQueryReq) new PlayerSummaryQueryReq().decode(tlv);
		this.setSequence(reqCmd.getSequence());

		return this;
	}

	@Override
	public ResponseCommand execute(IoSession session)
			throws Exception
	{
		String playerId = reqCmd.getPlayerId();
		String deviceId = (String) session.getAttribute("deviceId");

		try {
			PlayerService playerService = BighornApplicationContextUtil.getPlayerService();
			AccountService accountService = BighornApplicationContextUtil.getAccountService();

			PlayerDetailInfo player = playerService.query(playerId);
			AccountBaseInfo account = accountService.query(playerId);
			player.setNickname(account.getNickname());
			player.setAvatarUrl(account.getAvatarUrl());
			logger.info("commandTag=[" + this.getTag() + "]|ErrorCode=[" + ErrorCode.SUCCESS + "]|sessionId=["
					+ session.getId() + "]|device=[" + deviceId + "]|");

			PlayerSummaryQueryResp respCmd = new PlayerSummaryQueryResp(this.getSequence(), ErrorCode.SUCCESS, player);
			return respCmd;
		} catch (Exception e) {
			logger.warn("commandTag=[" + this.getTag() + "]|ErrorCode=[" + ErrorCode.UNKNOWN_FAILURE + "]|sessionId=["
					+ session.getId() + "]|device=[" + deviceId + "]|" + LogErrorMessage.getFullInfo(e));

			PlayerSummaryQueryResp respCmd = new PlayerSummaryQueryResp(this.getSequence(), ErrorCode.UNKNOWN_FAILURE);
			return respCmd;
		}
	}

	private PlayerSummaryQueryReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(PlayerSummaryQueryAdapter.class);

}
