package net.younguard.bighorn.broadcast.adapter;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import net.younguard.bighorn.broadcast.ErrorCode;
import net.younguard.bighorn.broadcast.cmd.CommandTag;
import net.younguard.bighorn.broadcast.cmd.QueryOnlineNumReq;
import net.younguard.bighorn.broadcast.cmd.QueryOnlineNumResp;
import net.younguard.bighorn.comm.RequestCommand;
import net.younguard.bighorn.comm.ResponseCommand;
import net.younguard.bighorn.comm.tlv.TlvObject;
import net.younguard.bighorn.comm.util.LogErrorMessage;

import org.apache.mina.core.service.IoService;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * sever received client query online device number command.
 * 
 * Copyright 2014-2015 by Young Guard Salon Community, China. All rights
 * reserved. http://www.younguard.net
 * 
 * NOTICE ! You can copy or redistribute this code freely, but you should not
 * remove the information about the copyright notice and the author.
 * 
 * @author ThomasZhang, thomas.zh@qq.com
 */
public class QueryOnlineNumAdapter
		extends RequestCommand
{
	public QueryOnlineNumAdapter()
	{
		super();

		this.setTag(CommandTag.QUERY_ONLINE_NUMBER_REQUEST);
	}

	@Override
	public RequestCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = (QueryOnlineNumReq) new QueryOnlineNumReq().decode(tlv);
		this.setSequence(reqCmd.getSequence());

		return this;
	}

	@Override
	public ResponseCommand execute(IoSession session)
			throws Exception
	{
		try {
			IoService ioService = session.getService();
			Map<Long, IoSession> sessions = ioService.getManagedSessions();

//			int num = 0;
//			SessionService sessionService = GenericSingleton.getInstance(SessionMap.class);
//			HashMap<String, SessionObject> sessionMap = sessionService.getSessionMap();
//			for (Map.Entry<String, SessionObject> it : sessionMap.entrySet()) {
//				SessionObject so = it.getValue();
//
//				if (so.isOnline())
//					num++;
//			}

			int num = sessions.size();
			logger.info("sessions number=[" + num + "] of this stp.");

			QueryOnlineNumResp respCmd = new QueryOnlineNumResp(this.getSequence(), ErrorCode.SUCCESS, num);
			return respCmd;
		} catch (Exception e) {
			logger.warn("sessionId=[" + session.getId() + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
					+ ErrorCode.UNKNOWN_FAILURE + "]|" + LogErrorMessage.getFullInfo(e));

			QueryOnlineNumResp respCmd = new QueryOnlineNumResp(this.getSequence(), ErrorCode.UNKNOWN_FAILURE);
			return respCmd;
		}
	}

	private QueryOnlineNumReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(QueryOnlineNumAdapter.class);

}
