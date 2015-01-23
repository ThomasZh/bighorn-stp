package net.younguard.bighorn.broadcast.tcp.server;

import static org.junit.Assert.assertEquals;

import java.util.UUID;

import net.younguard.bighorn.broadcast.domain.DeviceMasterInfo;
import net.younguard.bighorn.broadcast.service.DeviceService;
import net.younguard.bighorn.broadcast.util.BighornApplicationContextUtil;
import net.younguard.bighorn.comm.util.DatetimeUtil;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Thomas.Zhang
 * 
 */
// @FixMethodOrder(MethodSorters.DEFAULT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
// @FixMethodOrder(MethodSorters.JVM)
public class DeviceServiceTest
{
	private static String deviceId = UUID.randomUUID().toString();
	private static String notifyToken = UUID.randomUUID().toString();
	private static String username = "test";
	private static final short DEVICE_STATE_ACTIVE = 100;
	private static int timestamp = DatetimeUtil.currentTimestamp();

	@BeforeClass
	public static void setUpBeforeClass()
			throws Exception
	{
		logger.info("Initlization service starting...");

		String springXmlFile = "classpath:application-config.xml";
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(springXmlFile);
		BighornApplicationContextUtil.setApplicationContext(applicationContext);
		deviceService = BighornApplicationContextUtil.getDeviceService();

		logger.info("Initlization service success!");
	}

	@AfterClass
	public static void tearDownAfterClass()
			throws Exception
	{
		logger.info("Destroy everything");

		deviceService.remove(deviceId);
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
	public void test01Add()
	{
		logger.info("Inside Add()");

		DeviceMasterInfo device = new DeviceMasterInfo(deviceId, notifyToken, username, DEVICE_STATE_ACTIVE);
		deviceService.create(device, timestamp);

		logger.info("Finish Add()");
	}

	@Test
	public void test02Query()
	{
		logger.info("Inside Query()");

		DeviceMasterInfo device = deviceService.query(deviceId);
		assertEquals(notifyToken, device.getNotifyToken());
		assertEquals(username, device.getUsername());
		assertEquals(DEVICE_STATE_ACTIVE, device.getState());

		logger.info("Finish Query()");
	}

	@Test
	public void test03Modify()
	{
		logger.info("Inside Modify()");

		username = "demo";
		DeviceMasterInfo device = new DeviceMasterInfo(deviceId, notifyToken, username, DEVICE_STATE_ACTIVE);
		deviceService.modify(device, timestamp);

		logger.info("Finish Modify()");
	}

	@Test
	public void test04Query()
	{
		logger.info("Inside Query()");

		DeviceMasterInfo device = deviceService.query(deviceId);
		assertEquals(username, device.getUsername());

		logger.info("Finish Query()");
	}

	private static DeviceService deviceService;
	private static final Logger logger = LoggerFactory.getLogger(DeviceServiceTest.class);
}
