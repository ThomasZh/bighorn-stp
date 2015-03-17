package net.younguard.bighorn.badgenum;

import java.util.List;

import net.younguard.bighorn.badgenum.dao.BadgeNumDao;
import net.younguard.bighorn.domain.badge.ListBadgeNumber;

public class BadgeNumServiceImpl
		implements BadgeNumService
{
	@Override
	public short queryBadgeNum(String accountId)
	{
		short inviteNum = badgeNumDao.selectInviteNum(accountId);
		short playingNum = badgeNumDao.selectPlayingNum(accountId);
		short historyNum = badgeNumDao.selectHistoryNum(accountId);
		return (short) (inviteNum + playingNum + historyNum);
	}

	@Override
	public short queryInviteNum(String accountId)
	{
		return badgeNumDao.selectInviteNum(accountId);
	}

	@Override
	public short queryPlayingNum(String accountId)
	{
		return badgeNumDao.selectPlayingNum(accountId);
	}

	@Override
	public short queryHistoryNum(String accountId)
	{
		return badgeNumDao.selectHistoryNum(accountId);
	}

	@Override
	public void modifyInviteNum(String accountId, short num)
	{
		badgeNumDao.updateInviteNum(accountId, num);
	}

	@Override
	public void modifyPlayingNum(String accountId, short num)
	{
		badgeNumDao.updatePlayingNum(accountId, num);
	}

	@Override
	public void modifyHistoryNum(String accountId, short num)
	{
		badgeNumDao.updateHistoryNum(accountId, num);
	}

	@Override
	public short countBadgeNum(String accountId)
	{
		short inviteNum = badgeNumDao.countInviteNum(accountId);
		short playingNum = badgeNumDao.countPlayingNum(accountId);
		short historyNum = badgeNumDao.countHistoryNum(accountId);
		return (short) (inviteNum + playingNum + historyNum);
	}

	@Override
	public short countInviteNum(String accountId)
	{
		return badgeNumDao.countInviteNum(accountId);
	}

	@Override
	public short countPlayingNum(String accountId)
	{
		return badgeNumDao.countPlayingNum(accountId);
	}

	@Override
	public short countHistoryNum(String accountId)
	{
		return badgeNumDao.countHistoryNum(accountId);
	}

	@Override
	public List<ListBadgeNumber> queryInviteNumList(String accountId)
	{
		return badgeNumDao.queryInviteNumList(accountId);
	}

	@Override
	public List<ListBadgeNumber> queryPlayingNumList(String accountId)
	{
		return badgeNumDao.queryPlayingNumList(accountId);
	}

	@Override
	public List<ListBadgeNumber> queryHistoryNumList(String accountId)
	{
		return badgeNumDao.queryHistoryNumList(accountId);
	}

	// ////////////////////////////////////////////////////////////

	private BadgeNumDao badgeNumDao;

	public BadgeNumDao getBadgeNumDao()
	{
		return badgeNumDao;
	}

	public void setBadgeNumDao(BadgeNumDao badgeNumDao)
	{
		this.badgeNumDao = badgeNumDao;
	}

}
