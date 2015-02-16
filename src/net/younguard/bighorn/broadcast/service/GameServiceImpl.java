package net.younguard.bighorn.broadcast.service;

import java.util.List;
import java.util.UUID;

import net.younguard.bighorn.GlobalArgs;
import net.younguard.bighorn.broadcast.dao.GameDao;
import net.younguard.bighorn.broadcast.dao.GameManualDao;
import net.younguard.bighorn.broadcast.dao.PlayerDao;
import net.younguard.bighorn.broadcast.domain.Page;
import net.younguard.bighorn.domain.GameMasterInfo;
import net.younguard.bighorn.domain.GameStep;
import net.younguard.bighorn.domain.PlayerSummary;

public class GameServiceImpl
		implements GameService
{
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

	@Override
	public List<PlayerSummary> queryPlayersPagination(short pageNum, short pageSize)
	{
		Page<PlayerSummary> games = playerDao.queryPlayersPagination(pageNum, pageSize);
		List<PlayerSummary> array = games.getPageItems();

		return array;
	}

	@Override
	public PlayerSummary queryPlayer(String playerId)
	{
		return playerDao.select(playerId);
	}

	@Override
	public String create(String playerId, short color, short timeRule, int timestamp)
	{
		String gameId = UUID.randomUUID().toString();
		short state = GlobalArgs.GAME_STATE_INVITE;
		short step = 0;
		gameDao.add(gameId, timeRule, state, step, timestamp);

		playerDao.add(gameId, playerId, color, timestamp);

		return gameId;
	}

	@Override
	public void join(String gameId, String accountId, short color, int timestamp)
	{
		playerDao.add(gameId, accountId, color, timestamp);
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
	public String queryOpponentId(String gameId, String playerId)
	{
		List<PlayerSummary> array = playerDao.selectPlayers(gameId);

		for (PlayerSummary player : array) {
			if (!playerId.equals(player.getAccountId())) {
				return player.getAccountId();
			}
		}

		return null;
	}

	@Override
	public List<GameStep> queryGameManual(String gameId)
	{
		short lastStep = 0;
		return manualDao.select(gameId, lastStep);
	}

	@Override
	public List<GameStep> queryGameManual(String gameId, short lastStep)
	{
		return manualDao.select(gameId, lastStep);
	}

	@Override
	public void addStep(String gameId, String accountId, short step, short color, short x, short y, int timestamp)
	{
		manualDao.add(gameId, accountId, step, color, x, y, timestamp);
	}

	// ////////////////////////////////////////////////////////////

	private GameDao gameDao;
	private PlayerDao playerDao;
	private GameManualDao manualDao;

	public PlayerDao getPlayerDao()
	{
		return playerDao;
	}

	public void setPlayerDao(PlayerDao playerDao)
	{
		this.playerDao = playerDao;
	}

	public GameDao getGameDao()
	{
		return gameDao;
	}

	public void setGameDao(GameDao gameDao)
	{
		this.gameDao = gameDao;
	}

	public GameManualDao getManualDao()
	{
		return manualDao;
	}

	public void setManualDao(GameManualDao manualDao)
	{
		this.manualDao = manualDao;
	}

}
