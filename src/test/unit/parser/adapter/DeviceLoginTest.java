package test.unit.parser.adapter;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

import net.younguard.bighorn.BroadcastCommandParser;
import net.younguard.bighorn.CommandTag;
import net.younguard.bighorn.account.cmd.DeviceLoginReq;
import net.younguard.bighorn.broadcast.adapter.BroadAdapterParser;
import net.younguard.bighorn.broadcast.adapter.session.DeviceLoginAdapter;
import net.younguard.bighorn.comm.tlv.TlvByteUtil;
import net.younguard.bighorn.comm.tlv.TlvByteUtilPrinter;
import net.younguard.bighorn.comm.tlv.TlvObject;
import net.younguard.bighorn.comm.util.DatetimeUtil;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class DeviceLoginTest
{

	@BeforeClass
	public static void setUpBeforeClass()
			throws Exception
	{
	}

	@AfterClass
	public static void tearDownAfterClass()
			throws Exception
	{
	}

	@Before
	public void setUp()
			throws Exception
	{
	}

	@After
	public void tearDown()
			throws Exception
	{
	}

	@Test
	public void test()
			throws UnsupportedEncodingException
	{
		int timestamp = DatetimeUtil.currentTimestamp();
		String deviceId = UUID.randomUUID().toString();
		String accountId = UUID.randomUUID().toString();
		DeviceLoginReq reqCmd = new DeviceLoginReq(timestamp, deviceId, accountId);

		TlvObject reqTlv = BroadcastCommandParser.encode(reqCmd);
		TlvByteUtilPrinter.hexDump("DeviceLoginReq", reqTlv.toBytes());

		byte[] b = TlvByteUtil.sub(reqTlv.toBytes(), TlvObject.HEADER_LENGTH);
		TlvObject tlv2 = new TlvObject(CommandTag.DEVICE_LOGIN_REQUEST, b);

		DeviceLoginAdapter adapter = (DeviceLoginAdapter) BroadAdapterParser.decode(tlv2);
	}

}
