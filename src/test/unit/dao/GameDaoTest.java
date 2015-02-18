package test.unit.dao;

import java.util.List;
import java.util.UUID;

import net.younguard.bighorn.GlobalArgs;
import net.younguard.bighorn.broadcast.dao.GameDao;
import net.younguard.bighorn.broadcast.domain.Page;
import net.younguard.bighorn.comm.util.DatetimeUtil;
import net.younguard.bighorn.domain.GameMasterInfo;

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
public class GameDaoTest
{
	private static int timestamp = DatetimeUtil.currentTimestamp();
	private static String gameId = UUID.randomUUID().toString();
	private static short timeRule = GlobalArgs.GAME_TIME_RULE_1DAY_25STEPS;
	private static short state = GlobalArgs.GAME_STATE_PLAYING;
	private static short step = 0;

	@BeforeClass
	public static void setUpBeforeClass()
			throws Exception
	{
		logger.info("Initlization gameDao starting...");

		String springXmlFile = "classpath:application-config.xml";
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(springXmlFile);
		gameDao = (GameDao) applicationContext.getBean("broadcastGameDao");

		logger.info("Initlization gameDao success!");
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

		gameDao.add(gameId, timeRule, state, step, timestamp);

		logger.info("Finish Add()");
	}

	@Test
	public void test02Update()
	{
		logger.info("Inside update()");

		short state = GlobalArgs.GAME_STATE_COMPLETE;
		gameDao.updateState(gameId, state, timestamp);

		logger.info("Finish update()");
	}

	@Test
	public void test03Update()
	{
		logger.info("Inside update()");

		String winnerId = UUID.randomUUID().toString();
		gameDao.updateWinner(gameId, winnerId, timestamp);

		logger.info("Finish update()");
	}

	@Test
	public void test04QueryAll()
	{
		logger.info("Inside selectAll()");

		short pageNum = 1;
		short pageSize = 20;
		short state = GlobalArgs.GAME_STATE_COMPLETE;
		Page<GameMasterInfo> pages = gameDao.queryGamePagination(pageNum, pageSize, state);
		List<GameMasterInfo> games = pages.getPageItems();
		for (GameMasterInfo game : games) {
			logger.debug("getGameId: " + game.getGameId());
			logger.debug("getTimeRule: " + game.getTimeRule());
			logger.debug("getState: " + game.getState());
			logger.debug("getWinnerId: " + game.getWinnerId());
			logger.debug("getLastStep: " + game.getLastStep());
			logger.debug("getLastUpdateTime: " + game.getLastUpdateTime());
		}

		logger.info("Finish selectAll()");
	}

	@Test
	public void test05QueryAll()
	{
		logger.info("Inside selectAll()");

		short pageNum = 1;
		short pageSize = 20;
		short state = GlobalArgs.GAME_STATE_COMPLETE;
		String accountId = UUID.randomUUID().toString();
		Page<GameMasterInfo> pages = gameDao.queryGamePagination(pageNum, pageSize, state, accountId);
		List<GameMasterInfo> games = pages.getPageItems();
		for (GameMasterInfo game : games) {
			logger.debug("getGameId: " + game.getGameId());
			logger.debug("getTimeRule: " + game.getTimeRule());
			logger.debug("getState: " + game.getState());
			logger.debug("getWinnerId: " + game.getWinnerId());
			logger.debug("getLastStep: " + game.getLastStep());
			logger.debug("getLastUpdateTime: " + game.getLastUpdateTime());
		}

		logger.info("Finish selectAll()");
	}

	private static GameDao gameDao;
	private static final Logger logger = LoggerFactory.getLogger(GameDaoTest.class);

}
