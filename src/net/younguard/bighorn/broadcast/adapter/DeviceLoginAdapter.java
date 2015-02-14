package net.younguard.bighorn.broadcast.adapter;

import java.io.UnsupportedEncodingException;

import net.younguard.bighorn.CommandTag;
import net.younguard.bighorn.ErrorCode;
import net.younguard.bighorn.account.cmd.DeviceLoginReq;
import net.younguard.bighorn.account.cmd.DeviceLoginResp;
import net.younguard.bighorn.broadcast.domain.AccountBaseInfo;
import net.younguard.bighorn.broadcast.domain.DeviceMasterInfo;
import net.younguard.bighorn.broadcast.domain.SessionAccountObject;
import net.younguard.bighorn.broadcast.domain.SessionDeviceObject;
import net.younguard.bighorn.broadcast.service.AccountService;
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

public class DeviceLoginAdapter
		extends RequestCommand
{
	public DeviceLoginAdapter()
	{
		super();

		this.setTag(CommandTag.DEVICE_LOGIN_REQUEST);
	}

	@Override
	public RequestCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = (DeviceLoginReq) new DeviceLoginReq().decode(tlv);
		this.setSequence(reqCmd.getSequence());

		return this;
	}

	@Override
	public ResponseCommand execute(IoSession session)
			throws Exception
	{
		String deviceId = reqCmd.getDeviceId();
		String accountId = reqCmd.getAccountId();
		long ioSessionId = session.getId();
		int timestamp = DatetimeUtil.currentTimestamp();

		try {
			DeviceService deviceService = BighornApplicationContextUtil.getDeviceService();
			AccountService accountService = BighornApplicationContextUtil.getAccountService();
			SessionService sessionService = BighornApplicationContextUtil.getSessionService();

			// put attribute(deviceId,accountId) into ioSession of mina,
			// for every command can know who do that.
			session.setAttribute("deviceId", deviceId);
			session.setAttribute("accountId", accountId);

			// put device info into memcached.
			SessionDeviceObject sdo = sessionService.getDevice(deviceId);
			if (sdo == null) {
				DeviceMasterInfo device = deviceService.query(deviceId);

				sessionService.online(accountId, deviceId, device.getNotifyToken(), device.getOsVersion(), ioSessionId,
						timestamp);
			} else {
				sessionService.online(accountId, deviceId, sdo.getNotifyToken(), sdo.getOsVersion(), ioSessionId,
						timestamp);
			}

			// put account info into memcached.
			SessionAccountObject sao = sessionService.getAccount(accountId);
			if (sao == null) {
				AccountBaseInfo account = accountService.query(accountId);

				sessionService.register(accountId, deviceId, account.getNickname(), account.getAvatarUrl());
			} else {
				if (deviceId.equals(sao.getDeviceId())) {
					; // do nothing
				} else { // change device
					sessionService.register(accountId, deviceId, sao.getNickname(), sao.getAvatarUrl());
					
					// TODO: Notify old device, login on another device.
				}
			}

			DeviceLoginResp respCmd = new DeviceLoginResp(this.getSequence(), ErrorCode.SUCCESS, accountId);
			return respCmd;
		} catch (Exception e) {
			logger.warn("commandTag=[" + this.getTag() + "]|modify an account info error|sessionId=[" + session.getId()
					+ "]|device=[" + deviceId + "]|ErrorCode=[" + ErrorCode.UNKNOWN_FAILURE + "]|"
					+ LogErrorMessage.getFullInfo(e));

			DeviceLoginResp respCmd = new DeviceLoginResp(this.getSequence(), ErrorCode.UNKNOWN_FAILURE);
			return respCmd;
		}
	}

	private DeviceLoginReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(DeviceLoginAdapter.class);

}
