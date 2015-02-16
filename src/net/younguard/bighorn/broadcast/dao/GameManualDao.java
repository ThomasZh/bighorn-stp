package net.younguard.bighorn.broadcast.dao;

import java.util.List;

import net.younguard.bighorn.domain.GameStep;

public interface GameManualDao
{
	public List<GameStep> select(String gameId, short lastStep);

	public void add(String gameId, String accountId, short step, short color, short x, short y, int timestamp);
}
