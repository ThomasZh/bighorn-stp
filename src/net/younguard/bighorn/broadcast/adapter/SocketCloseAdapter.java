package net.younguard.bighorn.broadcast.adapter;

import java.io.UnsupportedEncodingException;

import net.younguard.bighorn.broadcast.ErrorCode;
import net.younguard.bighorn.broadcast.cmd.CommandTag;
import net.younguard.bighorn.broadcast.cmd.SocketCloseReq;
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
		try {
			String myDeviceId = (String) session.getAttribute("deviceId");

			SessionService sessionService = GenericSingleton.getInstance(SessionMap.class);
			sessionService.offline(myDeviceId);

			logger.warn("sessionId=[" + session.getId() + "]|commandTag=[" + this.getTag() + "]|deviceId=["
					+ myDeviceId + "]");

			session.close(true);
		} catch (Exception e) {
			logger.warn("sessionId=[" + session.getId() + "]|commandTag=[" + this.getTag() + "]|ErrorCode=["
					+ ErrorCode.UNKNOWN_FAILURE + "]|" + LogErrorMessage.getFullInfo(e));
		}

		return null;
	}

	private SocketCloseReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(SocketCloseAdapter.class);

}
