package net.younguard.bighorn.broadcast.dao;

import net.younguard.bighorn.broadcast.domain.Page;
import net.younguard.bighorn.domain.PlayerDetailInfo;
import net.younguard.bighorn.domain.PlayerMasterInfo;

public interface PlayerDao
{
	public void add(String accountId);

	public void updateInviteNum(String accountId, short num);

	public void updatePlayingNum(String accountId, short num);

	public void updateCompletedNum(String accountId, short num);

	public short selectInviteNum(String accountId);

	public short selectPlayingNum(String accountId);

	public short selectCompletedNum(String accountId);

	public Page<PlayerMasterInfo> selectPagination(short pageNum, short pageSize);

	public PlayerDetailInfo select(String accountId);
}
