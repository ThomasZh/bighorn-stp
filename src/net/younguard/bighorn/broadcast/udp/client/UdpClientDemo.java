package net.younguard.bighorn.broadcast.udp.client;

import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;

import net.younguard.bighorn.broadcast.cmd.BroadcastCommandParser;
import net.younguard.bighorn.broadcast.cmd.SentMsgReq;
import net.younguard.bighorn.comm.codec.TlvPackageCodecFactory;
import net.younguard.bighorn.comm.tlv.TlvObject;

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

	public UdpClientDemo()
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
		ConnectFuture connFuture = connector.connect(new InetSocketAddress("127.0.0.1"/* "182.92.71.66" */, 13105));

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
	{
		new UdpClientDemo();
	}

	private final static Logger logger = LoggerFactory.getLogger(UdpClientDemo.class);

}
