package net.younguard.bighorn.broadcast.tcp.server;

import java.io.UnsupportedEncodingException;
import java.net.SocketAddress;

import net.younguard.bighorn.BroadcastCommandParser;
import net.younguard.bighorn.ErrorCode;
import net.younguard.bighorn.broadcast.adapter.BroadAdapterParser;
import net.younguard.bighorn.broadcast.service.DeviceService;
import net.younguard.bighorn.broadcast.service.SessionService;
import net.younguard.bighorn.broadcast.util.BighornApplicationContextUtil;
import net.younguard.bighorn.comm.RequestCommand;
import net.younguard.bighorn.comm.ResponseCommand;
import net.younguard.bighorn.comm.tlv.TlvObject;
import net.younguard.bighorn.comm.util.DatetimeUtil;
import net.younguard.bighorn.comm.util.LogErrorMessage;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.SocketSessionConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TCP Server mina event handler. <br>
 * 1.pkg decode to request command;<br>
 * 2.illegal request check;<br>
 * 3.do command execute;<br>
 * 4.package command execute result to tlv;<br>
 * 5.send tlv to client;<br>
 * 
 * Copyright 2014-2015 by Young Guard Salon Community, China. All rights
 * reserved. http://www.younguard.net
 * 
 * NOTICE ! You can copy or redistribute this code freely, but you should not
 * remove the information about the copyright notice and the author.
 * 
 * @author ThomasZhang, thomas.zh@qq.com
 */
public class TcpServerEventHandler
		extends IoHandlerAdapter
{
	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception
	{
		logger.debug("Session recv...");

		if (message != null && message instanceof TlvObject) {
			long startTime = System.currentTimeMillis();

			TlvObject pkg = (TlvObject) message;
			// TlvByteUtilPrinter.hexDump("message body: ", pkg.getValue());

			RequestCommand reqCmd = null;
			try {
				// decode all the message to request command
				reqCmd = (RequestCommand) BroadAdapterParser.decode(pkg);
			} catch (UnsupportedEncodingException uee) {
				logger.warn("sessionId=[" + session.getId() + "]|commandTag=[" + pkg.getTag() + "]|ErrorCode=["
						+ ErrorCode.ENCODING_FAILURE + "]|" + LogErrorMessage.getFullInfo(uee));

				session.close(true);
				return;// break the logic blow
			}

			ResponseCommand respCmd = (ResponseCommand) reqCmd.execute(session);
			if (respCmd != null) {
				TlvObject respTlv = null;
				try {
					respTlv = BroadcastCommandParser.encode(respCmd);
					session.write(respTlv);
					logger.debug("send response command: " + respCmd.getTag());
				} catch (UnsupportedEncodingException uee) {
					logger.warn("sessionId=[" + session.getId() + "]|commandTag=[" + pkg.getTag() + "]|ErrorCode=["
							+ ErrorCode.ENCODING_FAILURE + "]|" + LogErrorMessage.getFullInfo(uee));

					session.close(true);
					return;// break the logic blow
				}
			}
			long endTime = System.currentTimeMillis();
			long deltaTime = endTime - startTime;
			logger.info("sessionId=[" + session.getId() + "]|commandTag=[" + reqCmd.getTag() + "]|execute(" + deltaTime
					+ "ms) command end.");
		}// end of if
	}

	@Override
	public void sessionOpened(IoSession session)
			throws Exception
	{
		super.sessionOpened(session);

		SocketAddress rsa = session.getRemoteAddress();
		logger.info("sessionId=[" + session.getId() + "]|remote address=[" + rsa.toString() + "]");
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception
	{
		super.exceptionCaught(session, cause);

		if (cause != null)
			logger.error(LogErrorMessage.getFullInfo(cause));

		super.sessionClosed(session);
	}

	@Override
	public void sessionClosed(IoSession session)
			throws Exception
	{
		super.sessionClosed(session);

		String deviceId = (String) session.getAttribute("deviceId");

		SocketAddress rsa = session.getRemoteAddress();
		if (rsa != null) {
			logger.info("sessionId=[" + session.getId() + "]|device=[" + deviceId + "]|remote address=["
					+ rsa.toString() + "] disconnect.");
		}

		if (deviceId != null) {
			int timestamp = DatetimeUtil.currentTimestamp();
			SessionService sessionService = BighornApplicationContextUtil.getSessionService();
			DeviceService deviceService = BighornApplicationContextUtil.getDeviceService();

			sessionService.offline(deviceId, timestamp);
			deviceService.modifyLastUpdateTime(deviceId, timestamp);

			int num = sessionService.getOnlineNum();
			logger.debug("session cache's online number=[" + num + "]");
		}
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status)
			throws Exception
	{
		if (status == IdleStatus.BOTH_IDLE) {
			// SocketAddress rsa = session.getRemoteAddress();
			logger.info("sessionId=[" + session.getId() + "]|session both(in/out) idle!");
			this.sessionClosed(session);// close session
		}
	}

	@Override
	public void sessionCreated(IoSession session)
			throws Exception
	{
		super.sessionCreated(session);

		// Empty handler
		SocketSessionConfig cfg = (SocketSessionConfig) session.getConfig();
		cfg.setReuseAddress(true);
		cfg.setWriteTimeout(30);
		cfg.setTcpNoDelay(false);
		cfg.setKeepAlive(false);
		// setup this, after mina call close(), netstat not too many TIME_WAIT
		cfg.setSoLinger(0);

		cfg.setTcpNoDelay(true);
		cfg.setUseReadOperation(false);
		cfg.setIdleTime(IdleStatus.BOTH_IDLE, 480);// 8 minutes
		cfg.setReadBufferSize(65535);
		cfg.setReceiveBufferSize(65535);
	}

	private final static Logger logger = LoggerFactory.getLogger(TcpServerEventHandler.class);

}
