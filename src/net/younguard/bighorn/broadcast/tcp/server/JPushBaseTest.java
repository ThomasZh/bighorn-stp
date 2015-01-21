package net.younguard.bighorn.broadcast.tcp.server;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import cn.jpush.api.JPushClient;
import cn.jpush.api.common.resp.APIConnectionException;
import cn.jpush.api.common.resp.APIRequestException;

public class JPushBaseTest
{
	protected static final String APP_KEY = "f681b6f304f146de15b918ae";
	protected static final String MASTER_SECRET = "4e8f4e0561abf58b83f3f79f";

	public static final String ALERT = "bighorn - alert";
	public static final String MSG_CONTENT = "JPush Test - msgContent";

	public static final String REGISTRATION_ID1 = "0900e8d85ef";
	public static final String REGISTRATION_ID2 = "0a04ad7d8b4";

	protected static JPushClient jpushClient = null;

	@BeforeClass
	public static void setUpBeforeClass()
			throws Exception
	{
		jpushClient = new JPushClient(MASTER_SECRET, APP_KEY);
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
	{
		try {
			jpushClient.sendAndroidMessageWithRegistrationID(ALERT, MSG_CONTENT, REGISTRATION_ID1);
		} catch (APIConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (APIRequestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
