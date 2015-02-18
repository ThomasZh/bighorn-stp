package test.unit.dao;

import java.util.List;
import java.util.UUID;

import net.younguard.bighorn.GlobalArgs;
import net.younguard.bighorn.broadcast.dao.GameMemberDao;
import net.younguard.bighorn.comm.util.DatetimeUtil;
import net.younguard.bighorn.domain.GameMemberMasterInfo;

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
public class GameMemberDaoTest
{
	private static int timestamp = DatetimeUtil.currentTimestamp();
	private static String gameId = UUID.randomUUID().toString();
	private static String accountId = UUID.randomUUID().toString();

	@BeforeClass
	public static void setUpBeforeClass()
			throws Exception
	{
		logger.info("Initlization gameMemberDao starting...");

		String springXmlFile = "classpath:application-config.xml";
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(springXmlFile);
		gameMemberDao = (GameMemberDao) applicationContext.getBean("broadcastGameMemberDao");

		logger.info("Initlization gameMemberDao success!");
	}

	@AfterClass
	public static void tearDownAfterClass()
			throws Exception
	{
		logger.info("Destroy everything");

		gameMemberDao.delete(gameId, accountId);
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

		gameMemberDao.add(gameId, accountId, GlobalArgs.PLAYER_COLOR_RED, timestamp);

		logger.info("Finish Add()");
	}

	@Test
	public void test02QueryAll()
	{
		logger.info("Inside selectAll()");

		List<GameMemberMasterInfo> members = gameMemberDao.select(gameId);
		for (GameMemberMasterInfo member : members) {
			logger.debug("deviceId: " + member.getAccountId());
			logger.debug("state: " + member.getState());
			logger.debug("color: " + member.getColor());
		}

		logger.info("Finish selectAll()");
	}

	private static GameMemberDao gameMemberDao;
	private static final Logger logger = LoggerFactory.getLogger(GameMemberDaoTest.class);

}
