package net.younguard.bighorn.broadcast.adapter;

import java.io.UnsupportedEncodingException;

import net.younguard.bighorn.CommandTag;
import net.younguard.bighorn.ErrorCode;
import net.younguard.bighorn.account.cmd.AccountInfoModifyReq;
import net.younguard.bighorn.account.cmd.AccountInfoModifyResp;
import net.younguard.bighorn.broadcast.service.AccountService;
import net.younguard.bighorn.broadcast.service.DeviceService;
import net.younguard.bighorn.broadcast.util.BighornApplicationContextUtil;
import net.younguard.bighorn.comm.RequestCommand;
import net.younguard.bighorn.comm.ResponseCommand;
import net.younguard.bighorn.comm.tlv.TlvObject;
import net.younguard.bighorn.comm.util.DatetimeUtil;
import net.younguard.bighorn.comm.util.LogErrorMessage;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AccountInfoModifyAdapter
		extends RequestCommand
{
	public AccountInfoModifyAdapter()
	{
		super();

		this.setTag(CommandTag.ACCOUNT_INFO_MODIFY_REQUEST);
	}

	@Override
	public RequestCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = (AccountInfoModifyReq) new AccountInfoModifyReq().decode(tlv);
		this.setSequence(reqCmd.getSequence());

		return this;
	}

	@Override
	public ResponseCommand execute(IoSession session)
			throws Exception
	{
		String deviceId = reqCmd.getDeviceId();
		String accountId = reqCmd.getAccountId();
		String nickname = reqCmd.getNickname();
		String avatarUrl = reqCmd.getAvatarUrl();
		int timestamp = DatetimeUtil.currentTimestamp();

		try {
			AccountService accountService = BighornApplicationContextUtil.getAccountService();
			DeviceService deviceService = BighornApplicationContextUtil.getDeviceService();

			if (accountId != null && accountId.length() > 0) {
				if (accountService.isExist(accountId)) { // modify
					logger.info("commandTag=[" + this.getTag() + "]|modify an account|sessionId=[" + session.getId()
							+ "]|device=[" + deviceId + "]|nickname=[" + nickname + "]");

					accountService.modify(accountId, nickname, avatarUrl, timestamp);
				} else { // create
					logger.info("commandTag=[" + this.getTag() + "]|create an new account|sessionId=["
							+ session.getId() + "]|device=[" + deviceId + "]|nickname=[" + nickname + "]");

					accountId = accountService.create(nickname, avatarUrl, timestamp);
				}
			} else { // create
				logger.info("commandTag=[" + this.getTag() + "]|create an new account|sessionId=[" + session.getId()
						+ "]|device=[" + deviceId + "]|nickname=[" + nickname + "]");

				accountId = accountService.create(nickname, avatarUrl, timestamp);
			}
			
			deviceService.bind(deviceId, accountId, timestamp);

			AccountInfoModifyResp respCmd = new AccountInfoModifyResp(this.getSequence(), ErrorCode.SUCCESS, accountId);
			return respCmd;
		} catch (Exception e) {
			logger.warn("commandTag=[" + this.getTag() + "]|modify an account info error|sessionId=[" + session.getId()
					+ "]|device=[" + deviceId + "]|ErrorCode=[" + ErrorCode.UNKNOWN_FAILURE + "]|"
					+ LogErrorMessage.getFullInfo(e));

			AccountInfoModifyResp respCmd = new AccountInfoModifyResp(this.getSequence(), ErrorCode.UNKNOWN_FAILURE);
			return respCmd;
		}
	}

	private AccountInfoModifyReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(AccountInfoModifyAdapter.class);

}
