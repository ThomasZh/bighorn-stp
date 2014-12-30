package net.younguard.bighorn.broadcast.adapter;

import java.io.UnsupportedEncodingException;

import net.younguard.bighorn.broadcast.cmd.CommandTag;
import net.younguard.bighorn.broadcast.cmd.MsgPangResp;
import net.younguard.bighorn.broadcast.cmd.MsgPongResp;
import net.younguard.bighorn.comm.Command;
import net.younguard.bighorn.comm.CommandParser;
import net.younguard.bighorn.comm.tlv.TlvObject;

public class BroadAdapterParser
		extends CommandParser
{
	public static TlvObject encode(Command cmd)
			throws UnsupportedEncodingException
	{
		return cmd.encode();
	}

	public static Command decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		switch (tlv.getTag()) {
		case CommandTag.MESSAGE_PING_REQUEST:
			return new MsgPingAdapter().decode(tlv);
		case CommandTag.MESSAGE_PANG_RESPONSE:
			return new MsgPangResp().decode(tlv);
		case CommandTag.MESSAGE_PONG_RESPONSE:
			return new MsgPongResp().decode(tlv);
		default:
			throw new UnsupportedEncodingException("Unknown command=[" + tlv.getTag() + "]");
		}
	}
}
