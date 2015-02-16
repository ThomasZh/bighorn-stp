package net.younguard.bighorn.broadcast.service;

import java.util.List;

import net.younguard.bighorn.domain.GameMasterInfo;
import net.younguard.bighorn.domain.GameStep;
import net.younguard.bighorn.domain.PlayerSummary;

public interface GameService
{
	public List<GameMasterInfo> queryInvitePagination(short pageNum, short pageSize);

	public List<GameMasterInfo> queryPlayingPagination(short pageNum, short pageSize);

	public List<GameMasterInfo> queryHistoryPagination(short pageNum, short pageSize);

	public List<GameMasterInfo> queryInvitePagination(short pageNum, short pageSize, String accountId);

	public List<GameMasterInfo> queryPlayingPagination(short pageNum, short pageSize, String accountId);

	public List<GameMasterInfo> queryHistoryPagination(short pageNum, short pageSize, String accountId);

	public List<PlayerSummary> queryPlayersPagination(short pageNum, short pageSize);

	public PlayerSummary queryPlayer(String playerId);

	public String create(String playerId, short color, short timeRule, int timestamp);

	public void join(String gameId, String accountId, short color, int timestamp);

	public void modifyGameState(String gameId, short state, int timestamp);

	public void modifyGameWinner(String gameId, String winnerId, int timestamp);

	public String queryOpponentId(String gameId, String playerId);

	public List<GameStep> queryGameManual(String gameId);

	public List<GameStep> queryGameManual(String gameId, short lastStep);

	public void addStep(String gameId, String accountId, short step, short color, short x, short y, int timestamp);
}
