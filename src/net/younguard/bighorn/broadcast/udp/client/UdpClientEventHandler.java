package net.younguard.bighorn.broadcast.udp.client;

import java.io.UnsupportedEncodingException;
import java.net.SocketAddress;

import net.younguard.bighorn.BroadcastCommandParser;
import net.younguard.bighorn.CommandTag;
import net.younguard.bighorn.broadcast.cmd.MsgPangResp;
import net.younguard.bighorn.broadcast.cmd.MsgPongNotify;
import net.younguard.bighorn.comm.Command;
import net.younguard.bighorn.comm.tlv.TlvObject;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UdpClientEventHandler
		extends IoHandlerAdapter
{
	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception
	{
		logger.debug("Session recv...");

		if (message != null && message instanceof TlvObject) {
			TlvObject pkg = (TlvObject) message;
			// TlvByteUtilPrinter.hexDump("message body: ", pkg.getValue());

			Command respCmd = null;
			try {
				// decode all the message to request command
				respCmd = BroadcastCommandParser.decode(pkg);
			} catch (UnsupportedEncodingException uee) {
				logger.warn(uee.getMessage());
				session.close(true);
				return;// break the logic blow
			}

			switch (pkg.getTag()) {
			case CommandTag.MESSAGE_PONG_RESPONSE:
				MsgPongNotify pongRespCmd = (MsgPongNotify) respCmd;
				logger.info("pong response sequence=[" + pongRespCmd.getSequence() + "] content=["
						+ pongRespCmd.getContent() + "]");
				break;
			case CommandTag.MESSAGE_PANG_RESPONSE:
				MsgPangResp pangRespCmd = (MsgPangResp) respCmd;
				logger.info("pang response sequence=[" + pangRespCmd.getSequence() + "] state=["
						+ pangRespCmd.getRespState() + "]");
				break;
			}
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
	}

	@Override
	public void sessionClosed(IoSession session)
			throws Exception
	{
		super.sessionClosed(session);
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status)
			throws Exception
	{
		super.sessionIdle(session, status);
	}

	@Override
	public void sessionCreated(IoSession session)
			throws Exception
	{
		super.sessionCreated(session);
	}

	private final static Logger logger = LoggerFactory.getLogger(UdpClientEventHandler.class);
}
