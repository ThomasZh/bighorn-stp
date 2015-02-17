package net.younguard.bighorn.broadcast.service;

import java.util.List;

import net.younguard.bighorn.broadcast.dao.PlayerDao;
import net.younguard.bighorn.broadcast.domain.Page;
import net.younguard.bighorn.domain.PlayerDetailInfo;
import net.younguard.bighorn.domain.PlayerMasterInfo;

public class PlayerServiceImpl
		implements PlayerService
{
	@Override
	public void add(String accountId)
	{
		playerDao.add(accountId);
	}

	@Override
	public void modifyInviteNum(String accountId, short num)
	{
		playerDao.updateInviteNum(accountId, num);
	}

	@Override
	public void modifyPlayingNum(String accountId, short num)
	{
		playerDao.updatePlayingNum(accountId, num);
	}

	@Override
	public void modifyCompletedNum(String accountId, short num)
	{
		playerDao.updateCompletedNum(accountId, num);
	}

	@Override
	public short queryInviteNum(String accountId)
	{
		return playerDao.selectInviteNum(accountId);
	}

	@Override
	public short queryPlayingNum(String accountId)
	{
		return playerDao.selectPlayingNum(accountId);
	}

	@Override
	public short queryCompletedNum(String accountId)
	{
		return playerDao.selectCompletedNum(accountId);
	}

	@Override
	public List<PlayerMasterInfo> queryPagination(short pageNum, short pageSize)
	{
		Page<PlayerMasterInfo> games = playerDao.selectPagination(pageNum, pageSize);
		List<PlayerMasterInfo> array = games.getPageItems();

		return array;
	}

	@Override
	public PlayerDetailInfo query(String playerId)
	{
		return playerDao.select(playerId);
	}

	private PlayerDao playerDao;

	public PlayerDao getPlayerDao()
	{
		return playerDao;
	}

	public void setPlayerDao(PlayerDao playerDao)
	{
		this.playerDao = playerDao;
	}

}
