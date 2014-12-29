package net.younguard.bighorn.broadcast.tcp.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

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
	private final static int PORT = 13105;

	public static void main(String[] args)
			throws IOException
	{
		logger.info("STP server is starting...");

		startMinaServer();
		logger.info("STP Server is listenig at port: " + PORT);

		// start send heart bit for GateKeeper, every 1 second.
		// new Thread(new HeartBitMonitor()).start();

		logger.info("STP server start success!");
	}

	private static void startMinaServer()
			throws IOException
	{
		IoAcceptor acceptor = new NioSocketAcceptor();

		// 杩�婊ゅ��锛����瀹�涔����璁�锛�
		acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new TlvPackageCodecFactory()));
		// 璁剧疆��版��灏�琚�璇诲�����缂���插�哄ぇ灏�
		acceptor.getSessionConfig().setReadBufferSize(65535); // 64k
		// 8���������娌℃��璇诲��灏辫�剧疆涓虹┖��查�����锛�骞跺��eventHandler.sessionIdle涓�绉婚�や��璇�
		acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 480);

		// 杩���ユ��璁剧疆
		// get a reference to the filter chain from the acceptor
		DefaultIoFilterChainBuilder filterChainBuilder = acceptor.getFilterChain();
		filterChainBuilder.addLast("threadPool", new ExecutorFilter(Executors.newCachedThreadPool()));

		// eventHandler to process message package
		acceptor.setHandler(new TcpServerEventHandler());
		acceptor.bind(new InetSocketAddress(PORT));
	}

	private final static Logger logger = LoggerFactory.getLogger(TcpServerDemo.class);

}
