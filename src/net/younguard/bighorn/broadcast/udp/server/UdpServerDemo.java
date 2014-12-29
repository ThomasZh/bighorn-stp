package net.younguard.bighorn.broadcast.udp.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

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
	private final static int PORT = 13105;

	public static void main(String[] args)
			throws IOException
	{
		logger.info("STP server is starting...");

		startMinaServer();
		logger.info("STP Server is listenig at port: " + PORT);
	}

	private static void startMinaServer()
			throws IOException
	{
		NioDatagramAcceptor acceptor = new NioDatagramAcceptor();
		acceptor.setHandler(new UdpServerEventHandler());

		// get a reference to the filter chain from the acceptor
		DefaultIoFilterChainBuilder filterChainBuilder = acceptor.getFilterChain();
		// 连接池设置
		filterChainBuilder.addLast("threadPool", new ExecutorFilter(Executors.newCachedThreadPool()));
		filterChainBuilder.addLast("logger", new LoggingFilter());
		// 过滤器（自定义协议）
		filterChainBuilder.addLast("codec", new ProtocolCodecFilter(new TlvPackageCodecFactory()));

		// 设置数据将被读取的缓冲区大小
		acceptor.getSessionConfig().setReadBufferSize(65535); // 64k
		// 10s内没有读写就设置为空闲通道，并在eventHandler.sessionIdle中移除会话
		acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);

		DatagramSessionConfig dcfg = acceptor.getSessionConfig();
		dcfg.setReuseAddress(true);
		acceptor.bind(new InetSocketAddress(PORT));
	}

	private final static Logger logger = LoggerFactory.getLogger(UdpServerDemo.class);
}
