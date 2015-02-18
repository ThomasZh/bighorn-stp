package net.younguard.bighorn.broadcast.dao;

import java.util.List;

import net.younguard.bighorn.domain.GameMemberMasterInfo;

public interface GameMemberDao
{
	public void add(String gameId, String playerId, short color, int timestamp);

	public List<GameMemberMasterInfo> select(String gameId);

	public void delete(String gameId, String playerId);

}
