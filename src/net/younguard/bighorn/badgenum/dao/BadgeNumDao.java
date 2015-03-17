package net.younguard.bighorn.badgenum.dao;

import java.util.List;

import net.younguard.bighorn.domain.badge.ListBadgeNumber;

public interface BadgeNumDao
{
	public boolean isExist(String accountId);

	public void add(String accountId);

	public short selectInviteNum(String accountId);

	public short selectPlayingNum(String accountId);

	public short selectHistoryNum(String accountId);

	public void updateInviteNum(String accountId, short num);

	public void updatePlayingNum(String accountId, short num);

	public void updateHistoryNum(String accountId, short num);

	public short countInviteNum(String accountId);

	public short countPlayingNum(String accountId);

	public short countHistoryNum(String accountId);
	
	public List<ListBadgeNumber> queryInviteNumList(String accountId);

	public List<ListBadgeNumber> queryPlayingNumList(String accountId);

	public List<ListBadgeNumber> queryHistoryNumList(String accountId);
}
