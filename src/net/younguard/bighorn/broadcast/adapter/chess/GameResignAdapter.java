package net.younguard.bighorn.broadcast.adapter.chess;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import net.younguard.bighorn.CommandTag;
import net.younguard.bighorn.ErrorCode;
import net.younguard.bighorn.GlobalArgs;
import net.younguard.bighorn.broadcast.domain.SessionAccountObject;
import net.younguard.bighorn.broadcast.domain.SessionDeviceObject;
import net.younguard.bighorn.broadcast.service.GameService;
import net.younguard.bighorn.broadcast.service.PlayerService;
import net.younguard.bighorn.broadcast.service.SessionService;
import net.younguard.bighorn.broadcast.util.BighornApplicationContextUtil;
import net.younguard.bighorn.chess.cmd.GameResignNotify;
import net.younguard.bighorn.chess.cmd.GameResignReq;
import net.younguard.bighorn.chess.cmd.GameResignResp;
import net.younguard.bighorn.comm.CommandParser;
import net.younguard.bighorn.comm.RequestCommand;
import net.younguard.bighorn.comm.ResponseCommand;
import net.younguard.bighorn.comm.tlv.TlvObject;
import net.younguard.bighorn.comm.util.DatetimeUtil;
import net.younguard.bighorn.comm.util.LogErrorMessage;

import org.apache.mina.core.service.IoService;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.jpush.api.JPushClient;

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
			SessionService sessionService = BighornApplicationContextUtil.getSessionService();
			PlayerService playerService = BighornApplicationContextUtil.getPlayerService();

			gameService.modifyGameState(gameId, GlobalArgs.GAME_STATE_COMPLETE, timestamp);
			String opponentId = gameService.queryOpponentId(gameId, accountId);
			gameService.modifyGameWinner(gameId, opponentId, timestamp);
			logger.info("commandTag=[" + this.getTag() + "]|ErrorCode=[" + ErrorCode.SUCCESS + "]|sessionId=["
					+ session.getId() + "]|device=[" + deviceId + "]|");

			GameResignResp respCmd = new GameResignResp(this.getSequence(), ErrorCode.SUCCESS);
			TlvObject tlvResp = CommandParser.encode(respCmd);
			session.write(tlvResp);

			// recount number of my summary(playing & completed)
			short num = playerService.queryPlayingNum(accountId);
			playerService.modifyPlayingNum(accountId, --num);

			num = playerService.queryCompletedNum(accountId);
			playerService.modifyCompletedNum(accountId, ++num);

			// recount number of opponent's summary(playing & completed)
			num = playerService.queryPlayingNum(opponentId);
			playerService.modifyPlayingNum(opponentId, --num);

			num = playerService.queryCompletedNum(opponentId);
			playerService.modifyCompletedNum(opponentId, ++num);

			// Logic: send message to opponent
			SessionAccountObject opponentSao = sessionService.getAccount(opponentId);
			String opponentDeviceId = opponentSao.getDeviceId();
			SessionDeviceObject opponentSdo = sessionService.getDevice(opponentDeviceId);

			if (sessionService.isOnline(opponentDeviceId)) { // online
				IoService ioService = session.getService();
				Map<Long, IoSession> sessions = ioService.getManagedSessions();

				long opponentIoSessionId = opponentSdo.getIoSessionId();
				IoSession opponentIoSession = sessions.get(opponentIoSessionId);

				GameResignNotify notifyResp = new GameResignNotify(timestamp, gameId, accountId);
				TlvObject tlvNotify = CommandParser.encode(notifyResp);
				opponentIoSession.write(tlvNotify);
			} else { // offline
				String notifyToken = opponentSdo.getNotifyToken();
				SessionAccountObject sao = sessionService.getAccount(accountId);
				String nickname = sao.getNickname();
				String txt = nickname + ":" + "resign, Congratulation you win this game!";

				JPushClient jpushClient = new JPushClient(MASTER_SECRET, APP_KEY);
				jpushClient.sendAndroidNotificationWithRegistrationID("Chess", txt, null, notifyToken);

				logger.info("send a message play step=[" + txt + "] to player=[" + opponentId + "] notify token=["
						+ notifyToken + "] by jpush");
			}
		} catch (Exception e) {
			logger.warn("commandTag=[" + this.getTag() + "]|ErrorCode=[" + ErrorCode.UNKNOWN_FAILURE + "]|sessionId=["
					+ session.getId() + "]|device=[" + deviceId + "]|" + LogErrorMessage.getFullInfo(e));

			GameResignResp respCmd = new GameResignResp(this.getSequence(), ErrorCode.UNKNOWN_FAILURE);
			TlvObject tlvResp = CommandParser.encode(respCmd);
			session.write(tlvResp);
		}

		return null;
	}

	private GameResignReq reqCmd;
	private static final String APP_KEY = "f681b6f304f146de15b918ae";
	private static final String MASTER_SECRET = "4e8f4e0561abf58b83f3f79f";

	private final static Logger logger = LoggerFactory.getLogger(GameResignAdapter.class);

}
