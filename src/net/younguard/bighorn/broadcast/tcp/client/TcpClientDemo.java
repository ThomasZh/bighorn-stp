package net.younguard.bighorn.broadcast.tcp.client;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;

import net.younguard.bighorn.broadcast.ErrorCode;
import net.younguard.bighorn.broadcast.cmd.BroadcastCommandParser;
import net.younguard.bighorn.broadcast.cmd.MsgPingReq;
import net.younguard.bighorn.broadcast.cmd.QueryOnlineNumReq;
import net.younguard.bighorn.comm.codec.TlvPackageCodecFactory;
import net.younguard.bighorn.comm.tlv.TlvObject;
import net.younguard.bighorn.comm.util.DatetimeUtil;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TcpClientDemo
		extends IoHandlerAdapter
{
	private IoConnector connector;
	private static IoSession session;

	public TcpClientDemo(String hostname, int port)
			throws InterruptedException, UnsupportedEncodingException
	{
		connector = new NioSocketConnector();

		connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new TlvPackageCodecFactory()));
		connector.getSessionConfig().setReadBufferSize(4096); // 4k

		connector.setHandler(new TcpClientEventHandler());

		ConnectFuture connFuture = connector.connect(new InetSocketAddress(hostname, port));
		connFuture.awaitUninterruptibly();
		session = connFuture.getSession();
		logger.info("TCP client started.");

		for (int i = 0;; i++) {
			int timestamp = DatetimeUtil.currentTimestamp();
			Thread.sleep(1000); // 1s

			String content = "[" + timestamp + "]Hello, world!";
			MsgPingReq reqCmd = new MsgPingReq(timestamp, content);
			TlvObject msgTlv = BroadcastCommandParser.encode(reqCmd);
			WriteFuture future = session.write(msgTlv);
			// Wait until the message is completely written out to the
			// O/S buffer.
			future.awaitUninterruptibly();
			if (future.isWritten()) {
				logger.info("Send(" + reqCmd.getTag() + "): " + reqCmd.getContent());
			} else {
				// The messsage couldn't be written out completely for
				// some reason. (e.g. Connection is closed)
				logger.warn("sessionId=[" + session.getId() + "]|ErrorCode=[" + ErrorCode.CONNECTION_CLOSED
						+ "]|couldn't be written out MsgPingReq completely for some reason.(e.g. Connection is closed)");

				break;
			}

			if (i % 30 == 0) {
				QueryOnlineNumReq qonReqCmd = new QueryOnlineNumReq(timestamp);
				TlvObject qonTlv = BroadcastCommandParser.encode(qonReqCmd);
				future = session.write(qonTlv);
				// Wait until the message is completely written out to the
				// O/S buffer.
				future.awaitUninterruptibly();
				if (future.isWritten()) {
					logger.info("Send query online number request=(" + reqCmd.getTag() + ")");
				} else {
					// The messsage couldn't be written out completely for
					// some reason. (e.g. Connection is closed)
					logger.warn("sessionId=["
							+ session.getId()
							+ "]|ErrorCode=["
							+ ErrorCode.CONNECTION_CLOSED
							+ "]|couldn't be written out QueryOnlineNumReq completely for some reason.(e.g. Connection is closed)");

					break;
				}
			}
		}
	}

	public static void main(String[] args)
			throws IOException, InterruptedException
	{
		// String hostname = "localhost";
		String hostname = "54.186.197.254"; // aws
		// String hostname = "182.92.165.159"; // ali2

		int port = 13103;
		if (args.length == 1)
			hostname = args[0];
		else if (args.length == 2) {
			hostname = args[0];
			port = Integer.parseInt(args[1]);
		}
		TcpClientDemo client = new TcpClientDemo(hostname, port);

		client.connector.dispose(true);
	}

	private final static Logger logger = LoggerFactory.getLogger(TcpClientDemo.class);
}
