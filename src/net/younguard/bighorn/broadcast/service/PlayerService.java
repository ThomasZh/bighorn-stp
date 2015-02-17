package net.younguard.bighorn.broadcast.service;

import java.util.List;

import net.younguard.bighorn.domain.PlayerDetailInfo;
import net.younguard.bighorn.domain.PlayerMasterInfo;

public interface PlayerService
{
	public PlayerDetailInfo query(String accountId);

	public List<PlayerMasterInfo> queryPagination(short pageNum, short pageSize);

	public void add(String accountId);

	public void modifyInviteNum(String accountId, short num);

	public void modifyPlayingNum(String accountId, short num);

	public void modifyCompletedNum(String accountId, short num);

	public short queryInviteNum(String accountId);

	public short queryPlayingNum(String accountId);

	public short queryCompletedNum(String accountId);

}
