package net.younguard.bighorn.broadcast.udp.client;

import java.io.UnsupportedEncodingException;
import java.net.SocketAddress;

import net.younguard.bighorn.broadcast.cmd.BroadcastCommandParser;
import net.younguard.bighorn.broadcast.cmd.CommandTag;
import net.younguard.bighorn.broadcast.cmd.SentMsgReq;
import net.younguard.bighorn.broadcast.cmd.SentMsgResp;
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
			case CommandTag.SENT_MESSAGE_REQUEST:
				SentMsgReq sentReqCmd = (SentMsgReq) respCmd;
				logger.info("receive message sequence=[" + sentReqCmd.getSequence() + "] content=["
						+ sentReqCmd.getContent() + "]");
				break;
			case CommandTag.SENT_MESSAGE_RESPONSE:
				SentMsgResp sentRespCmd = (SentMsgResp) respCmd;
				logger.info("response sequence=[" + sentRespCmd.getSequence() + "] state=["
						+ sentRespCmd.getRespState() + "]");
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
