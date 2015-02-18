package test.unit.dao;

import java.util.List;
import java.util.UUID;

import net.younguard.bighorn.GlobalArgs;
import net.younguard.bighorn.broadcast.dao.GameManualDao;
import net.younguard.bighorn.comm.util.DatetimeUtil;
import net.younguard.bighorn.domain.GameStep;

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
public class GameManualDaoTest
{
	private static int timestamp = DatetimeUtil.currentTimestamp();
	private static String gameId = UUID.randomUUID().toString();
	private static String accountId = UUID.randomUUID().toString();
	private static short step = 1;
	private static short color = GlobalArgs.PLAYER_COLOR_RED;
	private static short x = 5;
	private static short y = 6;

	@BeforeClass
	public static void setUpBeforeClass()
			throws Exception
	{
		logger.info("Initlization gameManualDao starting...");

		String springXmlFile = "classpath:application-config.xml";
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(springXmlFile);
		gameManualDao = (GameManualDao) applicationContext.getBean("broadcastGameManualDao");

		logger.info("Initlization gameManualDao success!");
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
	public void test01Add()
	{
		logger.info("Inside Add()");

		gameManualDao.add(gameId, accountId, step, color, x, y, timestamp);

		logger.info("Finish Add()");
	}

	@Test
	public void test02Add()
	{
		logger.info("Inside Add()");

		String accountId = UUID.randomUUID().toString();
		short step = 2;
		short color = GlobalArgs.PLAYER_COLOR_BLACK;
		short x = 6;
		short y = 7;
		gameManualDao.add(gameId, accountId, step, color, x, y, timestamp + 1);

		logger.info("Finish Add()");
	}

	@Test
	public void test03QueryAll()
	{
		logger.info("Inside selectAll()");

		short lastStep = 0;
		List<GameStep> steps = gameManualDao.select(gameId, lastStep);
		for (GameStep step : steps) {
			logger.debug("step: " + step.getStep());
			logger.debug("color: " + step.getColor());
			logger.debug("x: " + step.getX());
			logger.debug("y: " + step.getY());
			logger.debug("accountId: " + step.getAccountId());
		}

		logger.info("Finish selectAll()");
	}

	private static GameManualDao gameManualDao;
	private static final Logger logger = LoggerFactory.getLogger(GameManualDaoTest.class);

}
