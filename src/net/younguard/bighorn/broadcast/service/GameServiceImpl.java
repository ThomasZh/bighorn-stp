package net.younguard.bighorn.broadcast.service;

import java.util.List;
import java.util.UUID;

import net.younguard.bighorn.GlobalArgs;
import net.younguard.bighorn.broadcast.dao.GameDao;
import net.younguard.bighorn.broadcast.dao.GameManualDao;
import net.younguard.bighorn.broadcast.dao.GameMemberDao;
import net.younguard.bighorn.broadcast.domain.Page;
import net.younguard.bighorn.domain.GameMasterInfo;
import net.younguard.bighorn.domain.GameMemberMasterInfo;
import net.younguard.bighorn.domain.GameStep;

public class GameServiceImpl
		implements GameService
{
	@Override
	public String create(String playerId, short color, short timeRule, int timestamp)
	{
		String gameId = UUID.randomUUID().toString();
		short state = GlobalArgs.GAME_STATE_INVITE;
		short step = 0;
		gameDao.add(gameId, timeRule, state, step, timestamp);

		gameMemberDao.add(gameId, playerId, color, timestamp);

		return gameId;
	}

	@Override
	public void join(String gameId, String accountId, short color, int timestamp)
	{
		gameMemberDao.add(gameId, accountId, color, timestamp);
	}

	@Override
	public void modifyGameState(String gameId, short state, int timestamp)
	{
		gameDao.updateState(gameId, state, timestamp);
	}

	@Override
	public void modifyGameWinner(String gameId, String winnerId, int timestamp)
	{
		gameDao.updateWinner(gameId, winnerId, timestamp);
	}

	@Override
	public List<GameMasterInfo> queryInvitePagination(short pageNum, short pageSize)
	{
		Page<GameMasterInfo> games = gameDao.queryGamePagination(pageNum, pageSize, GlobalArgs.GAME_STATE_INVITE);
		List<GameMasterInfo> array = games.getPageItems();

		return array;
	}

	@Override
	public List<GameMasterInfo> queryInvitePagination(short pageNum, short pageSize, String accountId)
	{
		Page<GameMasterInfo> games = gameDao.queryGamePagination(pageNum, pageSize, GlobalArgs.GAME_STATE_INVITE,
				accountId);
		List<GameMasterInfo> array = games.getPageItems();

		return array;
	}

	@Override
	public List<GameMasterInfo> queryPlayingPagination(short pageNum, short pageSize)
	{
		Page<GameMasterInfo> games = gameDao.queryGamePagination(pageNum, pageSize, GlobalArgs.GAME_STATE_PLAYING);
		List<GameMasterInfo> array = games.getPageItems();

		return array;
	}

	@Override
	public List<GameMasterInfo> queryPlayingPagination(short pageNum, short pageSize, String accountId)
	{
		Page<GameMasterInfo> games = gameDao.queryGamePagination(pageNum, pageSize, GlobalArgs.GAME_STATE_PLAYING,
				accountId);
		List<GameMasterInfo> array = games.getPageItems();

		return array;
	}

	@Override
	public List<GameMasterInfo> queryHistoryPagination(short pageNum, short pageSize)
	{
		Page<GameMasterInfo> games = gameDao.queryGamePagination(pageNum, pageSize, GlobalArgs.GAME_STATE_COMPLETE);
		List<GameMasterInfo> array = games.getPageItems();

		return array;
	}

	@Override
	public List<GameMasterInfo> queryHistoryPagination(short pageNum, short pageSize, String accountId)
	{
		Page<GameMasterInfo> games = gameDao.queryGamePagination(pageNum, pageSize, GlobalArgs.GAME_STATE_COMPLETE,
				accountId);
		List<GameMasterInfo> array = games.getPageItems();

		return array;
	}

	// /////////////////////////////////////////////////////////
	// member

	public void createMember(String gameId, String accountId, short color, short state, int timestamp)
	{

	}

	public void modifyMemberState(String gameId, String accountId, short state, int timestamp)
	{

	}

	public List<GameMemberMasterInfo> queryGameMembers(String gameId)
	{
		return null;
	}

	@Override
	public String queryOpponentId(String gameId, String playerId)
	{
		List<GameMemberMasterInfo> array = gameMemberDao.select(gameId);

		for (GameMemberMasterInfo player : array) {
			if (!playerId.equals(player.getAccountId())) {
				return player.getAccountId();
			}
		}

		return null;
	}

	// /////////////////////////////////////////////////////////
	// manual

	@Override
	public List<GameStep> queryGameManual(String gameId)
	{
		short lastStep = 0;
		return gameManualDao.select(gameId, lastStep);
	}

	@Override
	public List<GameStep> queryGameManual(String gameId, short lastStep)
	{
		return gameManualDao.select(gameId, lastStep);
	}

	@Override
	public void addStep(String gameId, String accountId, short step, short color, short x, short y, int timestamp)
	{
		gameManualDao.add(gameId, accountId, step, color, x, y, timestamp);
	}

	// ////////////////////////////////////////////////////////////

	private GameDao gameDao;
	private GameMemberDao gameMemberDao;
	private GameManualDao gameManualDao;

	public GameMemberDao getGameMemberDao()
	{
		return gameMemberDao;
	}

	public void setGameMemberDao(GameMemberDao playerDao)
	{
		this.gameMemberDao = playerDao;
	}

	public GameDao getGameDao()
	{
		return gameDao;
	}

	public void setGameDao(GameDao gameDao)
	{
		this.gameDao = gameDao;
	}

	public GameManualDao getGameManualDao()
	{
		return gameManualDao;
	}

	public void setGameManualDao(GameManualDao manualDao)
	{
		this.gameManualDao = manualDao;
	}

}
