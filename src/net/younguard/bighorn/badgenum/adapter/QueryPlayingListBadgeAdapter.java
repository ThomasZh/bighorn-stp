package net.younguard.bighorn.badgenum.adapter;

import java.io.UnsupportedEncodingException;
import java.util.List;

import net.younguard.bighorn.CommandTag;
import net.younguard.bighorn.ErrorCode;
import net.younguard.bighorn.badge.cmd.QueryPlayingListBadgeReq;
import net.younguard.bighorn.badge.cmd.QueryPlayingListBadgeResp;
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

public class QueryPlayingListBadgeAdapter
		extends RequestCommand
{
	public QueryPlayingListBadgeAdapter()
	{
		super();

		this.setTag(CommandTag.QUERY_PLAYING_LIST_BADGE_NUMBER_REQUEST);
	}

	@Override
	public RequestCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = new QueryPlayingListBadgeReq().decode(tlv);
		this.setSequence(reqCmd.getSequence());

		return this;
	}

	@Override
	public ResponseCommand execute(IoSession session)
			throws Exception
	{
		QueryPlayingListBadgeResp respCmd = null;
		String accountId = (String) session.getAttribute("accountId");
		String deviceId = (String) session.getAttribute("deviceId");

		try {
			BadgeNumService badgeNumService = BighornApplicationContextUtil.getBadgeNumService();

			List<ListBadgeNumber> inviteNums = badgeNumService.queryPlayingNumList(accountId);

			respCmd = new QueryPlayingListBadgeResp(this.getSequence(), ErrorCode.SUCCESS, inviteNums);
		} catch (Exception e) {
			logger.error("sessionId=[" + session.getId() + "]|deviceId=[" + deviceId + "]|accountId=[" + accountId
					+ "]|commandTag=[" + this.getTag() + "]|ErrorCode=[" + ErrorCode.UNKNOWN_FAILURE + "]|"
					+ LogErrorMessage.getFullInfo(e));

			respCmd = new QueryPlayingListBadgeResp(this.getSequence(), ErrorCode.UNKNOWN_FAILURE, null);
		}

		return respCmd;
	}

	private QueryPlayingListBadgeReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(QueryPlayingListBadgeAdapter.class);

}
