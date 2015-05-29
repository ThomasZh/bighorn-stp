package net.younguard.bighorn.broadcast.service;

import java.util.List;

import net.younguard.bighorn.broadcast.domain.Page;
import net.younguard.bighorn.domain.GameMasterInfo;
import net.younguard.bighorn.domain.GameMemberMasterInfo;
import net.younguard.bighorn.domain.GameStep;

public interface GameService
{
	// /////////////////////////////////////////////////////////
	// game

	public String create(String playerId, short color, short timeRule, int timestamp);

	public void join(String gameId, String accountId, short color, int timestamp);

	public void modifyGameState(String gameId, short state, int timestamp);

	public void modifyGameWinner(String gameId, String winnerId, int timestamp);

	public Page<GameMasterInfo> queryInvitePagination(short pageNum, short pageSize);

	public Page<GameMasterInfo> queryPlayingPagination(short pageNum, short pageSize);

	public List<GameMasterInfo> queryHistoryPagination(short pageNum, short pageSize);

	public List<GameMasterInfo> queryInvitePagination(short pageNum, short pageSize, String accountId);

	public List<GameMasterInfo> queryPlayingPagination(short pageNum, short pageSize, String accountId);

	public List<GameMasterInfo> queryHistoryPagination(short pageNum, short pageSize, String accountId);

	// /////////////////////////////////////////////////////////
	// member

	public void createMember(String gameId, String accountId, short color, short state, int timestamp);

	public void modifyMemberState(String gameId, String accountId, short state, int timestamp);

	public List<GameMemberMasterInfo> queryGameMembers(String gameId);

	public String queryOpponentId(String gameId, String accountId);

	public short queryColor(String gameId, String accountId);

	// /////////////////////////////////////////////////////////
	// manual

	public short queryLastStep(String gameId);

	public List<GameStep> queryGameManual(String gameId);

	public List<GameStep> queryGameManual(String gameId, short lastStep);

	public void addStep(String gameId, String accountId, short step, short color, short x, short y, int timestamp);

}
