package net.younguard.bighorn.broadcast.tcp.server;

import java.io.UnsupportedEncodingException;
import java.net.SocketAddress;

import net.younguard.bighorn.broadcast.cmd.BroadcastCommandParser;
import net.younguard.bighorn.comm.RequestCommand;
import net.younguard.bighorn.comm.ResponseCommand;
import net.younguard.bighorn.comm.tlv.TlvObject;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.SocketSessionConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * do 5 task on messageReceived:<br>
 * <br>
 * 1.monitor counting;<br>
 * 2.pkg decode to request command;<br>
 * 3.illegal request check;<br>
 * 4.setup command service;<br>
 * 5.do command execute;<br>
 * 
 * @author liwenzhi
 * 
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
				reqCmd = (RequestCommand) BroadcastCommandParser.decode(pkg);
			} catch (UnsupportedEncodingException uee) {
				logger.warn(uee.getMessage());
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

		// if (cause != null)
		// logger.error(LogErrorMessage.getFullInfo(cause));
	}

	@Override
	public void sessionClosed(IoSession session)
			throws Exception
	{
		super.sessionClosed(session);

		SocketAddress rsa = session.getRemoteAddress();
		if (rsa != null) {
			logger.info("sessionId=[" + session.getId() + "]|remote address=[" + rsa.toString() + "] disconnect.");
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
		// 设置了它后，MINA在调用了close()方法后，不会再进入TIME_WAIT状态了，而直接Close掉了，
		// 这样就不会产生这样的那些TIME_WAIT的状态了。
		cfg.setSoLinger(0);

		cfg.setTcpNoDelay(true);
		cfg.setUseReadOperation(false);
		cfg.setIdleTime(IdleStatus.BOTH_IDLE, 480);// 8 minutes
		cfg.setReadBufferSize(65535);
		cfg.setReceiveBufferSize(65535);
	}

	private final static Logger logger = LoggerFactory.getLogger(TcpServerEventHandler.class);

}
