package test.unit.dao;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.UUID;

import net.younguard.bighorn.broadcast.dao.PlayerDao;
import net.younguard.bighorn.broadcast.domain.Page;
import net.younguard.bighorn.comm.util.DatetimeUtil;
import net.younguard.bighorn.domain.PlayerDetailInfo;
import net.younguard.bighorn.domain.PlayerMasterInfo;

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
public class PlayerDaoTest
{
	private static int timestamp = DatetimeUtil.currentTimestamp();
	private static String accountId = UUID.randomUUID().toString();

	@BeforeClass
	public static void setUpBeforeClass()
			throws Exception
	{
		logger.info("Initlization playerDao starting...");

		String springXmlFile = "classpath:application-config.xml";
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(springXmlFile);
		playerDao = (PlayerDao) applicationContext.getBean("broadcastPlayerDao");

		logger.info("Initlization playerDao success!");
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

		playerDao.add(accountId);

		logger.info("Finish Add()");
	}

	@Test
	public void test02Query()
	{
		logger.info("Inside select()");

		short inviteNum = playerDao.selectInviteNum(accountId);
		short playingNum = playerDao.selectPlayingNum(accountId);
		short completedNum = playerDao.selectCompletedNum(accountId);

		assertEquals(0, inviteNum);
		assertEquals(0, playingNum);
		assertEquals(0, completedNum);

		logger.info("Finish select()");
	}

	@Test
	public void test03Update()
	{
		logger.info("Inside update()");

		short num = 1;
		playerDao.updateInviteNum(accountId, num);
		playerDao.updatePlayingNum(accountId, num);
		playerDao.updateCompletedNum(accountId, num);

		logger.info("Finish update()");
	}

	@Test
	public void test04Query()
	{
		logger.info("Inside select()");

		short inviteNum = playerDao.selectInviteNum(accountId);
		short playingNum = playerDao.selectPlayingNum(accountId);
		short completedNum = playerDao.selectCompletedNum(accountId);

		assertEquals(1, inviteNum);
		assertEquals(1, playingNum);
		assertEquals(1, completedNum);

		logger.info("Finish select()");
	}

	@Test
	public void test05Query()
	{
		logger.info("Inside select()");

		PlayerDetailInfo player = playerDao.select(accountId);
		logger.debug("accountId: " + player.getAccountId());
		logger.debug("inviteNum: " + player.getInviteNum());
		logger.debug("playingNum: " + player.getPlayingNum());
		logger.debug("completedNum: " + player.getCompletedNum());

		logger.info("Finish select()");
	}

	@Test
	public void test06QueryAll()
	{
		logger.info("Inside selectAll()");

		short pageNum = 1;
		short pageSize = 20;
		Page<PlayerMasterInfo> pages = playerDao.selectPagination(pageNum, pageSize);
		List<PlayerMasterInfo> players = pages.getPageItems();
		for (PlayerMasterInfo player : players) {
			logger.debug("accountId: " + player.getAccountId());
			logger.debug("inviteNum: " + player.getInviteNum());
			logger.debug("playingNum: " + player.getPlayingNum());
			logger.debug("completedNum: " + player.getCompletedNum());
		}

		logger.info("Finish selectAll()");
	}

	private static PlayerDao playerDao;
	private static final Logger logger = LoggerFactory.getLogger(PlayerDaoTest.class);

}
