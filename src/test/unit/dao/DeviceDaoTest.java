package test.unit.dao;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.UUID;

import net.younguard.bighorn.GlobalArgs;
import net.younguard.bighorn.broadcast.dao.DeviceDao;
import net.younguard.bighorn.broadcast.domain.DeviceDetailInfo;
import net.younguard.bighorn.broadcast.domain.DeviceMasterInfo;
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
public class DeviceDaoTest
{
	private static int timestamp = DatetimeUtil.currentTimestamp();
	private static String deviceId = UUID.randomUUID().toString();
	private static String notifyToken = UUID.randomUUID().toString();
	private static String osVersion = "iOS-8.1.3";
	private static short state = GlobalArgs.DEVICE_ACCOUNT_STATE_ACTIVE;

	@BeforeClass
	public static void setUpBeforeClass()
			throws Exception
	{
		logger.info("Initlization deviceDao starting...");

		String springXmlFile = "classpath:application-config.xml";
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(springXmlFile);
		deviceDao = (DeviceDao) applicationContext.getBean("broadcastDeviceDao");

		logger.info("Initlization deviceDao success!");
	}

	@AfterClass
	public static void tearDownAfterClass()
			throws Exception
	{
		logger.info("Destroy everything");

		deviceDao.delete(deviceId);
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

		deviceDao.add(deviceId, notifyToken, osVersion, state, timestamp);

		logger.info("Finish Add()");
	}

	@Test
	public void test02Exist()
	{
		logger.info("Inside exist()");

		boolean exist = deviceDao.isExist(deviceId);
		logger.debug("device exist: " + exist);
		assertEquals(true, exist);

		logger.info("Finish exist()");
	}

	@Test
	public void test03Query()
	{
		logger.info("Inside select()");

		DeviceMasterInfo device = deviceDao.select(deviceId);
		assertEquals(deviceId, device.getDeviceId());
		assertEquals(notifyToken, device.getNotifyToken());
		assertEquals(osVersion, device.getOsVersion());

		logger.info("Finish select()");
	}

	@Test
	public void test04QueryAll()
	{
		logger.info("Inside selectAll()");

		List<DeviceDetailInfo> devices = deviceDao.selectAll();
		for (DeviceDetailInfo device : devices) {
			logger.debug("deviceId" + device.getDeviceId());
			logger.debug("notifyToken" + device.getNotifyToken());
			logger.debug("osVersion" + device.getOsVersion());
		}

		logger.info("Finish selectAll()");
	}

	@Test
	public void test05Update()
	{
		logger.info("Inside update()");

		String osVersion = "Android-5.0.2";
		deviceDao.update(deviceId, notifyToken, osVersion, state, timestamp);

		logger.info("Finish update()");
	}

	private static DeviceDao deviceDao;
	private static final Logger logger = LoggerFactory.getLogger(DeviceDaoTest.class);
}
