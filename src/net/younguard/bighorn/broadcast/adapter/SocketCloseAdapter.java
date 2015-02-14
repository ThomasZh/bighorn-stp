package net.younguard.bighorn.broadcast.adapter;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import net.younguard.bighorn.CommandTag;
import net.younguard.bighorn.ErrorCode;
import net.younguard.bighorn.comm.RequestCommand;
import net.younguard.bighorn.comm.ResponseCommand;
import net.younguard.bighorn.comm.tlv.TlvObject;
import net.younguard.bighorn.comm.util.LogErrorMessage;
import net.younguard.bighorn.session.cmd.SocketCloseReq;

import org.apache.mina.core.service.IoService;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * before disconnect socket, client send last package for server.
 * 
 * Copyright 2014-2015 by Young Guard Salon Community, China. All rights
 * reserved. http://www.younguard.net
 * 
 * NOTICE ! You can copy or redistribute this code freely, but you should not
 * remove the information about the copyright notice and the author.
 * 
 * @author ThomasZhang, thomas.zh@qq.com
 */
public class SocketCloseAdapter
		extends RequestCommand
{
	public SocketCloseAdapter()
	{
		super();

		this.setTag(CommandTag.SOCKET_CLOSE_REQUEST);
	}

	@Override
	public RequestCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = (SocketCloseReq) new SocketCloseReq().decode(tlv);
		this.setSequence(reqCmd.getSequence());

		return this;
	}

	@Override
	public ResponseCommand execute(IoSession session)
			throws Exception
	{
		String deviceId = (String) session.getAttribute("deviceId");

		try {
			IoService ioService = session.getService();
			Map<Long, IoSession> sessions = ioService.getManagedSessions();
			session.close(true);

			int num = sessions.size();
			logger.debug("mina io sessions number=[" + num + "]");

			logger.info("sessionId=[" + session.getId() + "]|device=[" + deviceId + "]|commandTag=[" + this.getTag()
					+ "]");
		} catch (Exception e) {
			logger.warn("sessionId=[" + session.getId() + "]|device=[" + deviceId + "]commandTag=[" + this.getTag()
					+ "]|ErrorCode=[" + ErrorCode.UNKNOWN_FAILURE + "]|" + LogErrorMessage.getFullInfo(e));
		}

		return null;
	}

	private SocketCloseReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(SocketCloseAdapter.class);

}
