package net.younguard.bighorn.broadcast.tcp.client;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;

import net.younguard.bighorn.broadcast.cmd.BroadcastCommandParser;
import net.younguard.bighorn.broadcast.cmd.SentMsgReq;
import net.younguard.bighorn.comm.codec.TlvPackageCodecFactory;
import net.younguard.bighorn.comm.tlv.TlvObject;

import org.apache.mina.core.future.ConnectFuture;
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
		connector.getSessionConfig().setReadBufferSize(65535); // 64k

		connector.setHandler(new TcpClientEventHandler());

		ConnectFuture connFuture = connector.connect(new InetSocketAddress(hostname, 13105));
		connFuture.awaitUninterruptibly();
		session = connFuture.getSession();
		logger.info("TCP client started.");

		for (short i = 0;; i++) {
			Thread.sleep(1000); // 1s

			String content = "[" + i + "]Hello, world!";
			SentMsgReq reqCmd = new SentMsgReq(i, content);

			TlvObject msgTlv = BroadcastCommandParser.encode(reqCmd);
			session.write(msgTlv);
			logger.info("Send(" + reqCmd.getTag() + "): " + reqCmd.getContent());
		}
	}

	public static void main(String[] args)
			throws IOException, InterruptedException
	{
		String hostname = "localhost";
		int port = 13105;
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
