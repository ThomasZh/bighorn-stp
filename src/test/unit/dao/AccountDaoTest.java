package test.unit.dao;

import static org.junit.Assert.assertEquals;

import java.util.UUID;

import net.younguard.bighorn.broadcast.dao.AccountDao;
import net.younguard.bighorn.comm.util.DatetimeUtil;
import net.younguard.bighorn.domain.AccountBaseInfo;

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
public class AccountDaoTest
{
	private static int timestamp = DatetimeUtil.currentTimestamp();
	private static String accountId = UUID.randomUUID().toString();
	private static String nickname = "Thomas";
	private static String avatarUrl = "http://tripc2c-person-face.b0.upaiyun.com/2015/01/14/dced0439c59b2eef0420b3a9a28c34cf.jpg";

	@BeforeClass
	public static void setUpBeforeClass()
			throws Exception
	{
		logger.info("Initlization accountDao starting...");

		String springXmlFile = "classpath:application-config.xml";
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(springXmlFile);
		accountDao = (AccountDao) applicationContext.getBean("broadcastAccountDao");

		logger.info("Initlization accountDao success!");
	}

	@AfterClass
	public static void tearDownAfterClass()
			throws Exception
	{
		logger.info("Destroy everything");

		accountDao.delete(accountId);
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

		accountDao.add(accountId, nickname, avatarUrl, timestamp);

		logger.info("Finish Add()");
	}

	@Test
	public void test02Exist()
	{
		logger.info("Inside exist()");

		boolean exist = accountDao.isExist(accountId);
		logger.debug("account exist: " + exist);
		assertEquals(true, exist);

		logger.info("Finish exist()");
	}

	@Test
	public void test03Query()
	{
		logger.info("Inside select()");

		AccountBaseInfo account = accountDao.select(accountId);
		assertEquals(accountId, account.getAccountId());
		assertEquals(nickname, account.getNickname());
		assertEquals(avatarUrl, account.getAvatarUrl());

		logger.info("Finish select()");
	}

	@Test
	public void test04Update()
	{
		logger.info("Inside update()");

		accountDao.update(accountId, "Leonard", avatarUrl, timestamp);

		logger.info("Finish update()");
	}

	private static AccountDao accountDao;
	private static final Logger logger = LoggerFactory.getLogger(AccountDaoTest.class);

}
