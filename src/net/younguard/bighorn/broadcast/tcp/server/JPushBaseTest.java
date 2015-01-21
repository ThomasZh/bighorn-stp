package net.younguard.bighorn.broadcast.tcp.server;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.jpush.api.JPushClient;
import cn.jpush.api.common.resp.APIConnectionException;
import cn.jpush.api.common.resp.APIRequestException;

/**
 * JUnit test case for jpush
 * 
 * Copyright 2014-2015 by Young Guard Salon Community, China. All rights
 * reserved. http://www.younguard.net
 * 
 * NOTICE ! You can copy or redistribute this code freely, but you should not
 * remove the information about the copyright notice and the author.
 * 
 * @author ThomasZhang, thomas.zh@qq.com
 */
public class JPushBaseTest
{
	protected static final String APP_KEY = "f681b6f304f146de15b918ae";
	protected static final String MASTER_SECRET = "4e8f4e0561abf58b83f3f79f";

	public static final String ALERT = "bighorn - alert";
	public static final String MSG_CONTENT = "JPush Test - msgContent";

	public static final String REGISTRATION_ID1 = "080a4aa2e02";
	// public static final String REGISTRATION_ID2 = "0a04ad7d8b4";

	protected static JPushClient jpushClient = null;

	@BeforeClass
	public static void setUpBeforeClass()
			throws Exception
	{
		jpushClient = new JPushClient(MASTER_SECRET, APP_KEY, 3);
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
			jpushClient.sendAndroidNotificationWithRegistrationID(ALERT, MSG_CONTENT, null, REGISTRATION_ID1);
			//jpushClient.sendAndroidMessageWithRegistrationID(ALERT, MSG_CONTENT, REGISTRATION_ID1);
			
		} catch (APIConnectionException e) {
			// Connection error, should retry later
			logger.error("Connection error, should retry later", e);
		} catch (APIRequestException e) {
			// Should review the error, and fix the request
			logger.error("Should review the error, and fix the request", e);
			logger.info("HTTP Status: " + e.getStatus());
			logger.info("Error Code: " + e.getErrorCode());
			logger.info("Error Message: " + e.getErrorMessage());
		}
	}

	private final static Logger logger = LoggerFactory.getLogger(JPushBaseTest.class);

}
