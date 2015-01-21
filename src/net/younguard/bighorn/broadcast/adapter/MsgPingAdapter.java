package net.younguard.bighorn.broadcast.adapter;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import net.younguard.bighorn.broadcast.ErrorCode;
import net.younguard.bighorn.broadcast.cmd.BroadcastCommandParser;
import net.younguard.bighorn.broadcast.cmd.CommandTag;
import net.younguard.bighorn.broadcast.cmd.MsgPangResp;
import net.younguard.bighorn.broadcast.cmd.MsgPingReq;
import net.younguard.bighorn.broadcast.cmd.MsgPongResp;
import net.younguard.bighorn.broadcast.session.SessionMap;
import net.younguard.bighorn.broadcast.session.SessionObject;
import net.younguard.bighorn.broadcast.session.SessionService;
import net.younguard.bighorn.comm.RequestCommand;
import net.younguard.bighorn.comm.ResponseCommand;
import net.younguard.bighorn.comm.tlv.TlvObject;
import net.younguard.bighorn.comm.util.GenericSingleton;
import net.younguard.bighorn.comm.util.LogErrorMessage;

import org.apache.mina.core.service.IoService;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.jpush.api.JPushClient;

/**
 * sever received client message ping command.
 * 
 * Copyright 2014-2015 by Young Guard Salon Community, China. All rights
 * reserved. http://www.younguard.net
 * 
 * NOTICE ! You can copy or redistribute this code freely, but you should not
 * remove the information about the copyright notice and the author.
 * 
 * @author ThomasZhang, thomas.zh@qq.com
 */
public class MsgPingAdapter
		extends RequestCommand
{
	protected static final String APP_KEY = "f681b6f304f146de15b918ae";
	protected static final String MASTER_SECRET = "4e8f4e0561abf58b83f3f79f";
	public static final String ALERT = "bighorn";

	public MsgPingAdapter()
	{
		super();

		this.setTag(CommandTag.MESSAGE_PING_REQUEST);
	}

	@Override
	public RequestCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = (MsgPingReq) new MsgPingReq().decode(tlv);
		this.setSequence(reqCmd.getSequence());

		return this;
	}

	@Override
	public ResponseCommand execute(IoSession session)
			throws Exception
	{
		String fromName = reqCmd.getUsername();
		String txt = reqCmd.getContent();

		try {
			IoService ioService = session.getService();
			Map<Long, IoSession> sessions = ioService.getManagedSessions();
			String myDeviceId = (String) session.getAttribute("deviceId");

			MsgPongResp pongRespCmd = new MsgPongResp(this.getSequence(), fromName, txt);
			logger.info("user=[" + fromName + "] session=[" + session.getId() + "] message ping=[" + txt + "]");

			// broadcast
			SessionService sessionService = GenericSingleton.getInstance(SessionMap.class);
			HashMap<String, SessionObject> sessionMap = sessionService.getSessionMap();
			for (Map.Entry<String, SessionObject> it : sessionMap.entrySet()) {
				String deviceId = it.getKey();
				SessionObject so = it.getValue();
				String username = so.getUsername();

				if (myDeviceId.equals(deviceId)) {
					logger.debug("This is pinger's device=[" + deviceId + "]");
					continue; // don't send me again.
				} else {
					if (so.isOnline()) {
						long sessionId = so.getIoSessionId();
						IoSession ioSession = sessions.get(sessionId);

						if (ioSession != null) {
							TlvObject pongRespTlv = BroadcastCommandParser.encode(pongRespCmd);

							ioSession.write(pongRespTlv);
							logger.info("broadcast message pong=[" + txt + "] to user=[" + username + "] session=["
									+ sessionId + "]");
						} else { // offline
							if (so.getNotifyToken() != null && so.getNotifyToken().length() > 0) {
								String MSG_CONTENT = fromName + ":" + txt;
								JPushClient jpushClient = new JPushClient(MASTER_SECRET, APP_KEY);
								jpushClient.sendAndroidNotificationWithRegistrationID(ALERT, MSG_CONTENT, null,
										so.getNotifyToken());

								logger.info("broadcast message pong=[" + txt + "] to user=[" + username
										+ "] notify token=[" + so.getNotifyToken() + "] by jpush");
							} else {
								logger.warn("broadcast message pong=[" + txt + "] can't send to user=[" + username
										+ "] by jpush, because has no notify token");
							}
						}
					} else { // offline
						if (so.getNotifyToken() != null && so.getNotifyToken().length() > 0) {
							String MSG_CONTENT = fromName + ":" + txt;
							JPushClient jpushClient = new JPushClient(MASTER_SECRET, APP_KEY);
							jpushClient.sendAndroidNotificationWithRegistrationID(ALERT, MSG_CONTENT, null,
									so.getNotifyToken());

							logger.info("broadcast message pong=[" + txt + "] to user=[" + username
									+ "] notify token=[" + so.getNotifyToken() + "] by jpush");
						} else {
							logger.warn("broadcast message pong=[" + txt + "] can't send to user=[" + username
									+ "] by jpush, because has no notify token");
						}
					}
				}
			}

			// broadcast
			// for (Map.Entry<Long, IoSession> it : sessions.entrySet()) {
			// long sessionId = it.getKey();
			// IoSession ioSession = it.getValue();
			//
			// if (sessionId == session.getId()) {
			// logger.debug("This is pinger's session=[" + sessionId + "]");
			// continue; // don't send me again.
			// }
			//
			// TlvObject pongRespTlv =
			// BroadcastCommandParser.encode(pongRespCmd);
			//
			// ioSession.write(pongRespTlv);
			// logger.info("broadcast message pong=[" + txt + "] to session=[" +
			// sessionId + "]");
			// }

			// message pang
			MsgPangResp respCmd = new MsgPangResp(this.getSequence(), ErrorCode.SUCCESS);
			return respCmd;
		} catch (Exception e) {
			logger.warn("sessionId=[" + session.getId() + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
					+ ErrorCode.UNKNOWN_FAILURE + "]|" + LogErrorMessage.getFullInfo(e));

			MsgPangResp respCmd = new MsgPangResp(this.getSequence(), ErrorCode.UNKNOWN_FAILURE);
			return respCmd;
		}
	}

	private MsgPingReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(MsgPingAdapter.class);

}
