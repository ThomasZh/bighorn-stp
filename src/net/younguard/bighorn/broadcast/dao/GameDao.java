package net.younguard.bighorn.broadcast.dao;

import net.younguard.bighorn.broadcast.domain.Page;
import net.younguard.bighorn.domain.GameMasterInfo;

public interface GameDao
{
	public Page<GameMasterInfo> queryGamePagination(short pageNum, short pageSize, short state);

	public Page<GameMasterInfo> queryGamePagination(short pageNum, short pageSize, short state, String accountId);

	public void add(String gameId, short timeRule, short state, short step, int timestamp);
	
	public void updateState(String gameId, short state, int timestamp);
	
	public void updateWinner(String gameId, String winnerId, int timestamp);
}
