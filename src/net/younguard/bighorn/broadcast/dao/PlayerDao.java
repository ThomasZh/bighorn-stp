package net.younguard.bighorn.broadcast.dao;

import java.util.List;

import net.younguard.bighorn.broadcast.domain.Page;
import net.younguard.bighorn.domain.GameMemberMasterInfo;
import net.younguard.bighorn.domain.PlayerSummary;

public interface PlayerDao
{
	public Page<PlayerSummary> queryPlayersPagination(short pageNum, short pageSize);

	public PlayerSummary select(String playerId);

	public List<GameMemberMasterInfo> selectGameMembers(String gameId);
	
	public void add(String gameId, String playerId, short color, int timestamp);
}
