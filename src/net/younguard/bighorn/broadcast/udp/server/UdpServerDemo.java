package net.younguard.bighorn.broadcast.udp.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import net.younguard.bighorn.broadcast.util.PropArgs;
import net.younguard.bighorn.comm.codec.TlvPackageCodecFactory;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.DatagramSessionConfig;
import org.apache.mina.transport.socket.nio.NioDatagramAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UdpServerDemo
{
	public static void main(String[] args)
			throws IOException
	{
		logger.info("stp server is starting...");

		startMinaServer(PropArgs.STP_PORT);
		logger.info("stp Server is listenig at port: " + PropArgs.STP_PORT);
	}

	private static void startMinaServer(int port)
			throws IOException
	{
		NioDatagramAcceptor acceptor = new NioDatagramAcceptor();
		acceptor.setHandler(new UdpServerEventHandler());

		// get a reference to the filter chain from the acceptor
		DefaultIoFilterChainBuilder filterChainBuilder = acceptor.getFilterChain();
		// setup thread pool
		filterChainBuilder.addLast("threadPool", new ExecutorFilter(Executors.newCachedThreadPool()));
		filterChainBuilder.addLast("logger", new LoggingFilter());
		// filter (user define protocol tlv)
		filterChainBuilder.addLast("codec", new ProtocolCodecFilter(new TlvPackageCodecFactory()));

		// setup read buffer size
		acceptor.getSessionConfig().setReadBufferSize(PropArgs.BUFFER_SIZE); // 64k
		// 10 seconds idle, and remove eventHandler in sessionIdle
		acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);

		DatagramSessionConfig dcfg = acceptor.getSessionConfig();
		dcfg.setReuseAddress(true);
		acceptor.bind(new InetSocketAddress(port));
	}

	private final static Logger logger = LoggerFactory.getLogger(UdpServerDemo.class);
}
