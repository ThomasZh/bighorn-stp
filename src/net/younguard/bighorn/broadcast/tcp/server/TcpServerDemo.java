package net.younguard.bighorn.broadcast.tcp.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import net.younguard.bighorn.broadcast.util.PropArgs;
import net.younguard.bighorn.comm.codec.TlvPackageCodecFactory;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TcpServerDemo
{
	public static void main(String[] args)
			throws IOException
	{
		logger.info("stp server is starting...");

		startMinaServer(PropArgs.STP_PORT);
		logger.info("stp server is listenig at port: " + PropArgs.STP_PORT);

		logger.info("stp server start success!");
	}

	private static void startMinaServer(int port)
			throws IOException
	{
		IoAcceptor acceptor = new NioSocketAcceptor();

		// filter (user define protocol tlv)
		acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new TlvPackageCodecFactory()));
		// setup read buffer size
		acceptor.getSessionConfig().setReadBufferSize(PropArgs.BUFFER_SIZE); // 64k
		// 8 minutes idle, and remove eventHandler in sessionIdle
		acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 480);

		// get a reference to the filter chain from the acceptor
		DefaultIoFilterChainBuilder filterChainBuilder = acceptor.getFilterChain();
		// setup thread pool
		filterChainBuilder.addLast("threadPool", new ExecutorFilter(Executors.newCachedThreadPool()));

		// eventHandler to process message package
		acceptor.setHandler(new TcpServerEventHandler());
		acceptor.bind(new InetSocketAddress(port));
	}

	private final static Logger logger = LoggerFactory.getLogger(TcpServerDemo.class);

}
