package net.younguard.bighorn.broadcast.adapter;

import java.io.UnsupportedEncodingException;

import net.younguard.bighorn.CommandTag;
import net.younguard.bighorn.ErrorCode;
import net.younguard.bighorn.account.cmd.RegisterDeviceNotifyTokenReq;
import net.younguard.bighorn.account.cmd.RegisterDeviceNotifyTokenResp;
import net.younguard.bighorn.broadcast.domain.DeviceMasterInfo;
import net.younguard.bighorn.broadcast.domain.SessionDeviceObject;
import net.younguard.bighorn.broadcast.service.DeviceService;
import net.younguard.bighorn.broadcast.service.SessionService;
import net.younguard.bighorn.broadcast.util.BighornApplicationContextUtil;
import net.younguard.bighorn.comm.RequestCommand;
import net.younguard.bighorn.comm.ResponseCommand;
import net.younguard.bighorn.comm.tlv.TlvObject;
import net.younguard.bighorn.comm.util.DatetimeUtil;
import net.younguard.bighorn.comm.util.LogErrorMessage;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * after connect socket, client send first package for server. put device
 * ID,notify token and username.
 * 
 * Copyright 2014-2015 by Young Guard Salon Community, China. All rights
 * reserved. http://www.younguard.net
 * 
 * NOTICE ! You can copy or redistribute this code freely, but you should not
 * remove the information about the copyright notice and the author.
 * 
 * @author ThomasZhang, thomas.zh@qq.com
 */
public class RegisterDeviceNotifyTokenAdapter
		extends RequestCommand
{
	private static final short DEVICE_STATE_ACTIVE = 100;

	// private static final short DEVICE_STATE_INACTIVE = 101;

	public RegisterDeviceNotifyTokenAdapter()
	{
		super();

		this.setTag(CommandTag.REGISTER_NOTIFY_TOKEN_REQUEST);
	}

	@Override
	public RequestCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = (RegisterDeviceNotifyTokenReq) new RegisterDeviceNotifyTokenReq().decode(tlv);
		this.setSequence(reqCmd.getSequence());

		return this;
	}

	@Override
	public ResponseCommand execute(IoSession session)
			throws Exception
	{
		String deviceId = reqCmd.getDeviceId();
		String notifyToken = reqCmd.getNotifyToken();
		String osVersion = reqCmd.getOsVersion();
		int timestamp = DatetimeUtil.currentTimestamp();

		try {
			SessionService sessionService = BighornApplicationContextUtil.getSessionService();
			DeviceService deviceService = BighornApplicationContextUtil.getDeviceService();

			long ioSessionId = session.getId();
			logger.info("sessionId=[" + ioSessionId + "]|device=[" + deviceId + "]|commandTag=[" + this.getTag()
					+ "]|osVersion=[" + osVersion + "]|notifyToken=[" + notifyToken + "]");

			if (deviceId != null) {
				session.setAttribute("deviceId", deviceId);

				SessionDeviceObject sdo = sessionService.getDevice(deviceId);
				if (sdo == null) {
					if (deviceService.isExist(deviceId)) {
						DeviceMasterInfo device = deviceService.query(deviceId);
						if (deviceId.equals(device.getDeviceId()) && notifyToken.equals(device.getNotifyToken())
								&& osVersion.equals(device.getOsVersion()) && device.getState() == DEVICE_STATE_ACTIVE) {
							; // do nothing
						} else {
							device.setDeviceId(deviceId);
							device.setNotifyToken(notifyToken);
							device.setOsVersion(osVersion);
							device.setState(DEVICE_STATE_ACTIVE);

							deviceService.modify(device, timestamp);
						}
					} else {
						DeviceMasterInfo device = new DeviceMasterInfo(deviceId, notifyToken, osVersion,
								DEVICE_STATE_ACTIVE);
						deviceService.create(device, timestamp);
					}
				} else { // compare it
					if (notifyToken.equals(sdo.getNotifyToken()) && osVersion.equals(sdo.getOsVersion())) {
						; // do nothing
					} else { // device info changed
						DeviceMasterInfo device = new DeviceMasterInfo();

						device.setDeviceId(deviceId);
						device.setNotifyToken(notifyToken);
						device.setOsVersion(osVersion);
						device.setState(DEVICE_STATE_ACTIVE);

						deviceService.modify(device, timestamp);
					}
				}

				String accountId = null; // no account
				sessionService.online(accountId, deviceId, osVersion, notifyToken, ioSessionId, timestamp);
			}

			RegisterDeviceNotifyTokenResp respCmd = new RegisterDeviceNotifyTokenResp(this.getSequence(),
					ErrorCode.SUCCESS);
			return respCmd;
		} catch (Exception e) {
			logger.warn("sessionId=[" + session.getId() + "]device=[" + deviceId + "]|commandTag=[" + this.getTag()
					+ "]|ErrorCode=[" + ErrorCode.UNKNOWN_FAILURE + "]|" + LogErrorMessage.getFullInfo(e));

			RegisterDeviceNotifyTokenResp respCmd = new RegisterDeviceNotifyTokenResp(this.getSequence(),
					ErrorCode.UNKNOWN_FAILURE);
			return respCmd;
		}
	}

	private RegisterDeviceNotifyTokenReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(RegisterDeviceNotifyTokenAdapter.class);

}
