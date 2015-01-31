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
import net.younguard.bighorn.broadcast.domain.SessionObject;
import net.younguard.bighorn.broadcast.service.SessionService;
import net.younguard.bighorn.broadcast.util.BighornApplicationContextUtil;
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
	private static final String APP_KEY = "f681b6f304f146de15b918ae";
	private static final String MASTER_SECRET = "4e8f4e0561abf58b83f3f79f";
	private static final String ALERT = "bighorn";
	private static final int DAY1 = 86400; // 24h

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
		String deviceId = (String) session.getAttribute("deviceId");
		int timestamp = DatetimeUtil.currentTimestamp();

		try {
			logger.info("sessionId=[" + session.getId() + "]|device=[" + deviceId + "]|commandTag=[" + this.getTag()
					+ "]|user=[" + fromName + "]|message=[" + txt + "]");

			// message pang
			MsgPangResp pangRespCmd = new MsgPangResp(this.getSequence(), ErrorCode.SUCCESS);
			TlvObject pangRespTlv = BroadAdapterParser.encode(pangRespCmd);
			session.write(pangRespTlv);
		} catch (Exception e) {
			logger.warn("sessionId=[" + session.getId() + "]|device=[" + deviceId + "]commandTag=[" + this.getTag()
					+ "]|ErrorCode=[" + ErrorCode.UNKNOWN_FAILURE + "]|" + LogErrorMessage.getFullInfo(e));

			MsgPangResp respCmd = new MsgPangResp(this.getSequence(), ErrorCode.UNKNOWN_FAILURE);
			return respCmd;
		}

		try {
			IoService ioService = session.getService();
			Map<Long, IoSession> sessions = ioService.getManagedSessions();

			SessionService sessionService = BighornApplicationContextUtil.getSessionService();
			JPushClient jpushClient = new JPushClient(MASTER_SECRET, APP_KEY);
			String MSG_CONTENT = fromName + ":" + txt;

			// broadcast
			MsgPongResp pongRespCmd = new MsgPongResp(this.getSequence(), fromName, txt);

			HashMap<String, SessionObject> sessionMap = sessionService.getSessionMap();
			for (Map.Entry<String, SessionObject> it : sessionMap.entrySet()) {
				String onlineDeviceId = it.getKey();
				SessionObject so = it.getValue();
				String username = so.getUsername();

				if (deviceId.equals(onlineDeviceId)) {
					logger.debug("This is pinger's device=[" + deviceId + "]");
					continue; // don't send me again.
				} else {
					if (so.isOnline()) {
						long sessionId = so.getIoSessionId();
						IoSession ioSession = sessions.get(sessionId);

						TlvObject pongRespTlv = BroadcastCommandParser.encode(pongRespCmd);

						ioSession.write(pongRespTlv);
						logger.info("broadcast message pong=[" + txt + "] to user=[" + username + "] session=["
								+ sessionId + "]");
					} else { // offline
						if (so.getNotifyToken() != null && so.getNotifyToken().length() > 0) {
							int lastTryTime = so.getLastTrtTime();
							if (timestamp - lastTryTime > DAY1) { // 24h
								logger.warn("broadcast message pong=[" + txt + "] can't send to user=[" + username
										+ "] by jpush, because this user not online one day.");
							} else {
								jpushClient.sendAndroidNotificationWithRegistrationID(ALERT, MSG_CONTENT, null,
										so.getNotifyToken());

								logger.info("broadcast message pong=[" + txt + "] to user=[" + username
										+ "] notify token=[" + so.getNotifyToken() + "] by jpush");
							}
						} else {
							logger.warn("broadcast message pong=[" + txt + "] can't send to user=[" + username
									+ "] by jpush, because has no notify token");
						}
					}
				}
			}
		} catch (Exception e) {
			logger.warn("sessionId=[" + session.getId() + "]|device=[" + deviceId + "]commandTag=[" + this.getTag()
					+ "]|ErrorCode=[" + ErrorCode.UNKNOWN_FAILURE + "]|" + LogErrorMessage.getFullInfo(e));
		}

		return null;
	}

	private MsgPingReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(MsgPingAdapter.class);

}
