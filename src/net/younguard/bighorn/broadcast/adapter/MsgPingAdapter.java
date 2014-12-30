package net.younguard.bighorn.broadcast.adapter;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import net.younguard.bighorn.broadcast.ErrorCode;
import net.younguard.bighorn.broadcast.cmd.BroadcastCommandParser;
import net.younguard.bighorn.broadcast.cmd.CommandTag;
import net.younguard.bighorn.broadcast.cmd.MsgPangResp;
import net.younguard.bighorn.broadcast.cmd.MsgPingReq;
import net.younguard.bighorn.broadcast.cmd.MsgPongResp;
import net.younguard.bighorn.comm.RequestCommand;
import net.younguard.bighorn.comm.ResponseCommand;
import net.younguard.bighorn.comm.tlv.TlvObject;
import net.younguard.bighorn.comm.util.LogErrorMessage;

import org.apache.mina.core.service.IoService;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MsgPingAdapter
		extends RequestCommand
{
	public MsgPingAdapter()
	{
		super();

		this.setTag(CommandTag.MESSAGE_PING_REQUEST);
	}

	@Override
	public RequestCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = (MsgPingReq) new MsgPingReq().decode(tlv);
		this.setSequence(reqCmd.getSequence());

		return this;
	}

	@Override
	public ResponseCommand execute(IoSession session)
			throws Exception
	{
		String txt = reqCmd.getContent();
		
		try {
			IoService ioService = session.getService();
			Map<Long, IoSession> sessions = ioService.getManagedSessions();

			MsgPongResp pongRespCmd = new MsgPongResp(this.getSequence(), txt);
			logger.info("user session=[" + session.getId() + "] message ping=[" + txt + "]");

			// broadcast
			for (Map.Entry<Long, IoSession> it : sessions.entrySet()) {
				long sessionId = it.getKey();
				IoSession ioSession = it.getValue();

				if (sessionId == session.getId()) {
					logger.debug("This is pinger's session=[" + sessionId + "]");
					continue; // don't send me again.
				}

				TlvObject pongRespTlv = BroadcastCommandParser.encode(pongRespCmd);

				ioSession.write(pongRespTlv);
				logger.info("broadcast message pong=[" + txt + "] to session=[" + sessionId + "]");
			}

			// message pang
			MsgPangResp respCmd = new MsgPangResp(this.getSequence(), ErrorCode.SUCCESS);
			return respCmd;
		} catch (Exception e) {
			logger.warn("sessionId=[" + session.getId() + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
					+ ErrorCode.UNKNOWN_FAILURE + "]|" + LogErrorMessage.getFullInfo(e));

			MsgPangResp respCmd = new MsgPangResp(this.getSequence(), ErrorCode.UNKNOWN_FAILURE);
			return respCmd;
		}
	}

	private MsgPingReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(MsgPingAdapter.class);

}
