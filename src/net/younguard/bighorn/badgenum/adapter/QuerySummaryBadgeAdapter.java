package net.younguard.bighorn.badgenum.adapter;

import java.io.UnsupportedEncodingException;

import net.younguard.bighorn.CommandTag;
import net.younguard.bighorn.ErrorCode;
import net.younguard.bighorn.badge.cmd.QuerySummaryBadgeReq;
import net.younguard.bighorn.badge.cmd.QuerySummaryBadgeResp;
import net.younguard.bighorn.badgenum.BadgeNumService;
import net.younguard.bighorn.broadcast.util.BighornApplicationContextUtil;
import net.younguard.bighorn.comm.RequestCommand;
import net.younguard.bighorn.comm.ResponseCommand;
import net.younguard.bighorn.comm.tlv.TlvObject;
import net.younguard.bighorn.comm.util.LogErrorMessage;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QuerySummaryBadgeAdapter
		extends RequestCommand
{
	public QuerySummaryBadgeAdapter()
	{
		super();

		this.setTag(CommandTag.QUERY_SUMMSRY_BADGE_NUMBER_REQUEST);
	}

	@Override
	public RequestCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new QuerySummaryBadgeReq().decode(tlv);
		this.setSequence(reqCmd.getSequence());

		return this;
	}

	@Override
	public ResponseCommand execute(IoSession session)
			throws Exception
	{
		QuerySummaryBadgeResp respCmd = null;
		String accountId = (String) session.getAttribute("accountId");
		String deviceId = (String) session.getAttribute("deviceId");

		try {
			BadgeNumService badgeNumService = BighornApplicationContextUtil.getBadgeNumService();

			short inviteNum = badgeNumService.queryInviteNum(accountId);
			short playingNum = badgeNumService.queryPlayingNum(accountId);
			short historyNum = badgeNumService.queryHistoryNum(accountId);

			respCmd = new QuerySummaryBadgeResp(this.getSequence(), ErrorCode.SUCCESS, inviteNum, playingNum,
					historyNum);
		} catch (Exception e) {
			logger.error("sessionId=[" + session.getId() + "]|deviceId=[" + deviceId + "]|accountId=[" + accountId
					+ "]|commandTag=[" + this.getTag() + "]|ErrorCode=[" + ErrorCode.UNKNOWN_FAILURE + "]|"
					+ LogErrorMessage.getFullInfo(e));

			respCmd = new QuerySummaryBadgeResp(this.getSequence(), ErrorCode.UNKNOWN_FAILURE, (short) 0, (short) 0,
					(short) 0);
		}

		return respCmd;
	}

	private QuerySummaryBadgeReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(QuerySummaryBadgeAdapter.class);

}
