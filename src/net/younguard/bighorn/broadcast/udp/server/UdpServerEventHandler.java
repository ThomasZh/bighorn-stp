package net.younguard.bighorn.broadcast.udp.server;

import java.io.UnsupportedEncodingException;
import java.net.SocketAddress;

import net.younguard.bighorn.BroadcastCommandParser;
import net.younguard.bighorn.ErrorCode;
import net.younguard.bighorn.comm.RequestCommand;
import net.younguard.bighorn.comm.ResponseCommand;
import net.younguard.bighorn.comm.tlv.TlvObject;
import net.younguard.bighorn.comm.util.LogErrorMessage;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UdpServerEventHandler
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
				reqCmd = (RequestCommand) BroadcastCommandParser.decode(pkg);
			} catch (UnsupportedEncodingException uee) {
				logger.warn("sessionId=[" + session.getId() + "]|commandTag=[" + pkg.getTag() + "]|ErrorCode=["
						+ ErrorCode.ENCODING_FAILURE + "]|" + LogErrorMessage.getFullInfo(uee));

				session.close(true);
				return;// break the logic blow
			}

			ResponseCommand respCmd = (ResponseCommand) reqCmd.execute(session);
			if (respCmd != null) {
				TlvObject tResp = BroadcastCommandParser.encode(respCmd);
				session.write(tResp);
				logger.debug("send response command: " + respCmd.getTag());
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

	private final static Logger logger = LoggerFactory.getLogger(UdpServerEventHandler.class);
}
