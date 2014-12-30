package net.younguard.bighorn.broadcast.udp.client;

import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;

import net.younguard.bighorn.broadcast.cmd.BroadcastCommandParser;
import net.younguard.bighorn.broadcast.cmd.MsgPingReq;
import net.younguard.bighorn.comm.codec.TlvPackageCodecFactory;
import net.younguard.bighorn.comm.tlv.TlvObject;
import net.younguard.bighorn.comm.util.DatetimeUtil;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.IoFutureListener;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.DatagramConnector;
import org.apache.mina.transport.socket.nio.NioDatagramConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UdpClientDemo
{
	DatagramConnector connector;
	IoSession session;

	public UdpClientDemo(String hostname, int port)
	{
		logger.debug("UDPClient::UdpClientDemo");
		logger.debug("Created a datagram connector");
		connector = new NioDatagramConnector();

		logger.debug("Setting the handler");
		connector.setHandler(new UdpClientEventHandler());

		DefaultIoFilterChainBuilder chain = connector.getFilterChain();
		chain.addLast("logger", new LoggingFilter());
		chain.addLast("codec", new ProtocolCodecFilter(new TlvPackageCodecFactory()));

		logger.debug("About to connect to the server...");
		ConnectFuture connFuture = connector.connect(new InetSocketAddress(hostname, port));

		logger.debug("About to wait.");
		connFuture.awaitUninterruptibly();

		logger.debug("Adding a future listener.");
		connFuture.addListener(new IoFutureListener<ConnectFuture>()
		{
			public void operationComplete(ConnectFuture future)
			{
				if (future.isConnected()) {
					logger.debug("...connected");
					session = future.getSession();
					try {
						sendData();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					logger.error("Not connected...exiting");
				}
			}
		});
	}

	private void sendData()
			throws InterruptedException, UnsupportedEncodingException
	{
		for (;;) {
			int timestamp = DatetimeUtil.currentTimestamp();
			Thread.sleep(1000); // 1s

			String content = "[" + timestamp + "]Hello, world!";
			MsgPingReq reqCmd = new MsgPingReq(timestamp, content);

			TlvObject msgTlv = BroadcastCommandParser.encode(reqCmd);
			session.write(msgTlv);
			logger.info("Send(" + reqCmd.getTag() + "): " + reqCmd.getContent());
		}
	}

	public static void main(String[] args)
	{
		// String hostname = "localhost";
		String hostname = "54.186.197.254"; // aws
		// String hostname = "182.92.165.159"; // ali2

		int port = 13101;
		if (args.length == 1)
			hostname = args[0];
		else if (args.length == 2) {
			hostname = args[0];
			port = Integer.parseInt(args[1]);
		}

		new UdpClientDemo(hostname, port);
	}

	private final static Logger logger = LoggerFactory.getLogger(UdpClientDemo.class);

}
