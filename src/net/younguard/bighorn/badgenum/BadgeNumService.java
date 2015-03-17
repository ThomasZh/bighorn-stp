package net.younguard.bighorn.badgenum;

import java.util.List;

import net.younguard.bighorn.domain.badge.ListBadgeNumber;

public interface BadgeNumService
{
	public short queryBadgeNum(String accountId);

	public short queryInviteNum(String accountId);

	public short queryPlayingNum(String accountId);

	public short queryHistoryNum(String accountId);

	public void modifyInviteNum(String accountId, short num);

	public void modifyPlayingNum(String accountId, short num);

	public void modifyHistoryNum(String accountId, short num);

	public short countBadgeNum(String accountId);

	public short countInviteNum(String accountId);

	public short countPlayingNum(String accountId);

	public short countHistoryNum(String accountId);

	public List<ListBadgeNumber> queryInviteNumList(String accountId);

	public List<ListBadgeNumber> queryPlayingNumList(String accountId);

	public List<ListBadgeNumber> queryHistoryNumList(String accountId);
}
