package test.unit.dao;

import static org.junit.Assert.assertEquals;

import java.util.UUID;

import net.younguard.bighorn.GlobalArgs;
import net.younguard.bighorn.broadcast.dao.DeviceAccountDao;
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
public class DeviceAccountDaoTest
{
	private static int timestamp = DatetimeUtil.currentTimestamp();
	private static String accountId = UUID.randomUUID().toString();
	private static String deviceId = UUID.randomUUID().toString();

	@BeforeClass
	public static void setUpBeforeClass()
			throws Exception
	{
		logger.info("Initlization deviceAccountDao starting...");

		String springXmlFile = "classpath:application-config.xml";
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(springXmlFile);
		deviceAccountDao = (DeviceAccountDao) applicationContext.getBean("broadcastDeviceAccountDao");

		logger.info("Initlization deviceAccountDao success!");
	}

	@AfterClass
	public static void tearDownAfterClass()
			throws Exception
	{
		logger.info("Destroy everything");

		deviceAccountDao.delete(deviceId, accountId);
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

		deviceAccountDao.add(deviceId, accountId, timestamp);

		logger.info("Finish Add()");
	}

	@Test
	public void test02Exist()
	{
		logger.info("Inside exist()");

		boolean exist = deviceAccountDao.isExist(deviceId, accountId);
		logger.debug("account exist: " + exist);
		assertEquals(true, exist);

		logger.info("Finish exist()");
	}

	@Test
	public void test03Active()
	{
		logger.info("Inside active()");

		boolean active = deviceAccountDao.isActive(deviceId, accountId);
		logger.debug("account active: " + active);
		assertEquals(true, active);

		logger.info("Finish active()");
	}

	@Test
	public void test04Update()
	{
		logger.info("Inside update()");

		short state = GlobalArgs.DEVICE_ACCOUNT_STATE_INACTIVE;
		deviceAccountDao.update(deviceId, accountId, state, timestamp);

		logger.info("Finish update()");
	}

	@Test
	public void test05Active()
	{
		logger.info("Inside active()");

		boolean active = deviceAccountDao.isActive(deviceId, accountId);
		logger.debug("account active: " + active);
		assertEquals(false, active);

		logger.info("Finish active()");
	}

	private static DeviceAccountDao deviceAccountDao;
	private static final Logger logger = LoggerFactory.getLogger(AccountDaoTest.class);

}
