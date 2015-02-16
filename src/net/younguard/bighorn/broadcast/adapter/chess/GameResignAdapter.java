package net.younguard.bighorn.broadcast.adapter.chess;

import java.io.UnsupportedEncodingException;

import net.younguard.bighorn.CommandTag;
import net.younguard.bighorn.ErrorCode;
import net.younguard.bighorn.GlobalArgs;
import net.younguard.bighorn.broadcast.service.GameService;
import net.younguard.bighorn.broadcast.util.BighornApplicationContextUtil;
import net.younguard.bighorn.chess.cmd.GameResignReq;
import net.younguard.bighorn.chess.cmd.GameResignResp;
import net.younguard.bighorn.comm.RequestCommand;
import net.younguard.bighorn.comm.ResponseCommand;
import net.younguard.bighorn.comm.tlv.TlvObject;
import net.younguard.bighorn.comm.util.DatetimeUtil;
import net.younguard.bighorn.comm.util.LogErrorMessage;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameResignAdapter
		extends RequestCommand
{
	public GameResignAdapter()
	{
		super();

		this.setTag(CommandTag.GAME_RESIGN_RESPONSE);
	}

	@Override
	public RequestCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = (GameResignReq) new GameResignReq().decode(tlv);
		this.setSequence(reqCmd.getSequence());

		return this;
	}

	@Override
	public ResponseCommand execute(IoSession session)
			throws Exception
	{
		String gameId = reqCmd.getGameId();
		String accountId = (String) session.getAttribute("accountId");
		String deviceId = (String) session.getAttribute("deviceId");
		int timestamp = DatetimeUtil.currentTimestamp();

		try {
			GameService gameService = BighornApplicationContextUtil.getGameService();

			gameService.modifyGameState(gameId, GlobalArgs.GAME_STATE_COMPLETE, timestamp);
			String opponentId = gameService.queryOpponentId(gameId, accountId);
			gameService.modifyGameWinner(gameId, opponentId, timestamp);
			logger.info("commandTag=[" + this.getTag() + "]|ErrorCode=[" + ErrorCode.SUCCESS + "]|sessionId=["
					+ session.getId() + "]|device=[" + deviceId + "]|");

			// TODO: send message to opponent
			
			GameResignResp respCmd = new GameResignResp(this.getSequence(), ErrorCode.SUCCESS);
			return respCmd;
		} catch (Exception e) {
			logger.warn("commandTag=[" + this.getTag() + "]|ErrorCode=[" + ErrorCode.UNKNOWN_FAILURE + "]|sessionId=["
					+ session.getId() + "]|device=[" + deviceId + "]|" + LogErrorMessage.getFullInfo(e));

			GameResignResp respCmd = new GameResignResp(this.getSequence(), ErrorCode.UNKNOWN_FAILURE);
			return respCmd;
		}
	}

	private GameResignReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(GameResignAdapter.class);

}
