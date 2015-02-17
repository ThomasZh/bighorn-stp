package net.younguard.bighorn.broadcast.adapter.chess;

import java.io.UnsupportedEncodingException;

import net.younguard.bighorn.CommandTag;
import net.younguard.bighorn.ErrorCode;
import net.younguard.bighorn.broadcast.service.GameService;
import net.younguard.bighorn.broadcast.service.PlayerService;
import net.younguard.bighorn.broadcast.util.BighornApplicationContextUtil;
import net.younguard.bighorn.chess.cmd.GameInviteCreateReq;
import net.younguard.bighorn.chess.cmd.GameInviteCreateResp;
import net.younguard.bighorn.comm.RequestCommand;
import net.younguard.bighorn.comm.ResponseCommand;
import net.younguard.bighorn.comm.tlv.TlvObject;
import net.younguard.bighorn.comm.util.DatetimeUtil;
import net.younguard.bighorn.comm.util.LogErrorMessage;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameInviteCreateAdaper
		extends RequestCommand
{
	public GameInviteCreateAdaper()
	{
		super();

		this.setTag(CommandTag.GAME_INVITE_CREATE_REQUEST);
	}

	@Override
	public RequestCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = (GameInviteCreateReq) new GameInviteCreateReq().decode(tlv);
		this.setSequence(reqCmd.getSequence());

		return this;
	}

	@Override
	public ResponseCommand execute(IoSession session)
			throws Exception
	{
		short color = reqCmd.getColor();
		short timeRule = reqCmd.getTimeRule();
		String accountId = (String) session.getAttribute("accountId");
		String deviceId = (String) session.getAttribute("deviceId");
		int timestamp = DatetimeUtil.currentTimestamp();

		try {
			GameService gameService = BighornApplicationContextUtil.getGameService();
			PlayerService playerService = BighornApplicationContextUtil.getPlayerService();

			String gameId = gameService.create(accountId, color, timeRule, timestamp);
			logger.info("commandTag=[" + this.getTag() + "]|ErrorCode=[" + ErrorCode.SUCCESS + "]|sessionId=["
					+ session.getId() + "]|device=[" + deviceId + "]|");
			
			short num = playerService.queryInviteNum(accountId);
			playerService.modifyInviteNum(accountId, ++num);

			GameInviteCreateResp respCmd = new GameInviteCreateResp(this.getSequence(), ErrorCode.SUCCESS, gameId);
			return respCmd;
		} catch (Exception e) {
			logger.warn("commandTag=[" + this.getTag() + "]|ErrorCode=[" + ErrorCode.UNKNOWN_FAILURE + "]|sessionId=["
					+ session.getId() + "]|device=[" + deviceId + "]|" + LogErrorMessage.getFullInfo(e));

			GameInviteCreateResp respCmd = new GameInviteCreateResp(this.getSequence(), ErrorCode.UNKNOWN_FAILURE);
			return respCmd;
		}
	}

	private GameInviteCreateReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(GameInviteCreateAdaper.class);

}
