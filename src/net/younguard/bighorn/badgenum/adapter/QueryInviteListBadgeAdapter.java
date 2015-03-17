package net.younguard.bighorn.badgenum.adapter;

import java.io.UnsupportedEncodingException;
import java.util.List;

import net.younguard.bighorn.CommandTag;
import net.younguard.bighorn.ErrorCode;
import net.younguard.bighorn.badge.cmd.QueryInviteListBadgeReq;
import net.younguard.bighorn.badge.cmd.QueryInviteListBadgeResp;
import net.younguard.bighorn.badgenum.BadgeNumService;
import net.younguard.bighorn.broadcast.util.BighornApplicationContextUtil;
import net.younguard.bighorn.comm.RequestCommand;
import net.younguard.bighorn.comm.ResponseCommand;
import net.younguard.bighorn.comm.tlv.TlvObject;
import net.younguard.bighorn.comm.util.LogErrorMessage;
import net.younguard.bighorn.domain.badge.ListBadgeNumber;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QueryInviteListBadgeAdapter
		extends RequestCommand
{
	public QueryInviteListBadgeAdapter()
	{
		super();

		this.setTag(CommandTag.QUERY_INVITE_LIST_BADGE_NUMBER_REQUEST);
	}

	@Override
	public RequestCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new QueryInviteListBadgeReq().decode(tlv);
		this.setSequence(reqCmd.getSequence());

		return this;
	}

	@Override
	public ResponseCommand execute(IoSession session)
			throws Exception
	{
		QueryInviteListBadgeResp respCmd = null;
		String accountId = (String) session.getAttribute("accountId");
		String deviceId = (String) session.getAttribute("deviceId");

		try {
			BadgeNumService badgeNumService = BighornApplicationContextUtil.getBadgeNumService();

			List<ListBadgeNumber> inviteNums = badgeNumService.queryInviteNumList(accountId);

			respCmd = new QueryInviteListBadgeResp(this.getSequence(), ErrorCode.SUCCESS, inviteNums);
		} catch (Exception e) {
			logger.error("sessionId=[" + session.getId() + "]|deviceId=[" + deviceId + "]|accountId=[" + accountId
					+ "]|commandTag=[" + this.getTag() + "]|ErrorCode=[" + ErrorCode.UNKNOWN_FAILURE + "]|"
					+ LogErrorMessage.getFullInfo(e));

			respCmd = new QueryInviteListBadgeResp(this.getSequence(), ErrorCode.UNKNOWN_FAILURE, null);
		}

		return respCmd;
	}

	private QueryInviteListBadgeReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(QueryInviteListBadgeAdapter.class);

}
