package net.younguard.bighorn.broadcast.adapter;

import java.io.UnsupportedEncodingException;

import net.younguard.bighorn.broadcast.ErrorCode;
import net.younguard.bighorn.broadcast.cmd.CommandTag;
import net.younguard.bighorn.broadcast.cmd.RegisterNotifyTokenReq;
import net.younguard.bighorn.broadcast.cmd.RegisterNotifyTokenResp;
import net.younguard.bighorn.broadcast.session.SessionMap;
import net.younguard.bighorn.broadcast.session.SessionService;
import net.younguard.bighorn.comm.RequestCommand;
import net.younguard.bighorn.comm.ResponseCommand;
import net.younguard.bighorn.comm.tlv.TlvObject;
import net.younguard.bighorn.comm.util.GenericSingleton;
import net.younguard.bighorn.comm.util.LogErrorMessage;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * after connect socket, client send first package for server. put device ID,notify token and username.
 * 
 * Copyright 2014-2015 by Young Guard Salon Community, China. All rights reserved.
 * http://www.younguard.net
 * 
 * NOTICE ! You can copy or redistribute this code freely, but you should not
 * remove the information about the copyright notice and the author.
 * 
 * @author ThomasZhang, thomas.zh@qq.com
 */
public class RegisterNotifyTokenAdapter
		extends RequestCommand
{
	public RegisterNotifyTokenAdapter()
	{
		super();

		this.setTag(CommandTag.REGISTER_NOTIFY_TOKEN_REQUEST);
	}

	@Override
	public RequestCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = (RegisterNotifyTokenReq) new RegisterNotifyTokenReq().decode(tlv);
		this.setSequence(reqCmd.getSequence());

		return this;
	}

	@Override
	public ResponseCommand execute(IoSession session)
			throws Exception
	{
		String deviceId = reqCmd.getDeviceId();
		String notifyToken = reqCmd.getNotifyToken();
		String username = reqCmd.getUsername();

		try {
			long ioSessionId = session.getId();
			session.setAttribute("deviceId", deviceId);

			SessionService sessionService = GenericSingleton.getInstance(SessionMap.class);
			sessionService.online(deviceId, notifyToken, username, ioSessionId);

			RegisterNotifyTokenResp respCmd = new RegisterNotifyTokenResp(this.getSequence(), ErrorCode.SUCCESS);
			return respCmd;
		} catch (Exception e) {
			logger.warn("sessionId=[" + session.getId() + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
					+ ErrorCode.UNKNOWN_FAILURE + "]|" + LogErrorMessage.getFullInfo(e));

			RegisterNotifyTokenResp respCmd = new RegisterNotifyTokenResp(this.getSequence(), ErrorCode.UNKNOWN_FAILURE);
			return respCmd;
		}
	}

	private RegisterNotifyTokenReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(RegisterNotifyTokenAdapter.class);

}
