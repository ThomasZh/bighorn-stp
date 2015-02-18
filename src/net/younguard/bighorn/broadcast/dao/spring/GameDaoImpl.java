package net.younguard.bighorn.broadcast.dao.spring;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.younguard.bighorn.broadcast.dao.GameDao;
import net.younguard.bighorn.broadcast.domain.Page;
import net.younguard.bighorn.broadcast.util.PaginationHelper;
import net.younguard.bighorn.domain.GameMasterInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

public class GameDaoImpl
		extends JdbcDaoSupport
		implements GameDao
{
	@Override
	public void add(final String gameId, final short timeRule, final short state, final short step, final int timestamp)
	{
		String sql = "INSERT INTO bighorn_game (game_id,time_rule,state,last_step,create_time,last_update_time) VALUES (?,?,?,?,?,?)";
		logger.debug("INSERT INTO bighorn_game (game_id,time_rule,state,last_step,create_time,last_update_time) VALUES ("
				+ gameId + "," + timeRule + "," + state + "," + step + "," + timestamp + "," + timestamp + ")");

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				int i = 1;
				ps.setString(i++, gameId);
				ps.setShort(i++, timeRule);
				ps.setShort(i++, state);
				ps.setShort(i++, step);
				ps.setInt(i++, timestamp);
				ps.setInt(i++, timestamp);
			}
		});
	}

	@Override
	public void updateState(final String gameId, final short state, final int timestamp)
	{
		String sql = "UPDATE bighorn_game SET state=?,last_update_time=? WHERE game_id=?";
		logger.debug("UPDATE bighorn_game SET state=" + state + ",last_update_time=" + timestamp + " WHERE game_id="
				+ gameId);

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				int i = 1;
				ps.setShort(i++, state);
				ps.setInt(i++, timestamp);
				ps.setString(i++, gameId);
			}
		});
	}

	@Override
	public void updateWinner(final String gameId, final String winnerId, final int timestamp)
	{
		String sql = "UPDATE bighorn_game SET winner_id=?,last_update_time=? WHERE game_id=?";
		logger.debug("UPDATE bighorn_game SET winner_id=" + winnerId + ",last_update_time=" + timestamp
				+ " WHERE game_id=" + gameId);

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				int i = 1;
				ps.setString(i++, winnerId);
				ps.setInt(i++, timestamp);
				ps.setString(i++, gameId);
			}
		});
	}

	@Override
	public Page<GameMasterInfo> queryGamePagination(short pageNum, short pageSize, final short state)
	{
		PaginationHelper<GameMasterInfo> ph = new PaginationHelper<GameMasterInfo>();

		String countSql = "SELECT count(game_id) FROM bighorn_game WHERE state=?";
		String sql = "SELECT game_id,time_rule,create_time,last_update_time,last_step,winner_id "
				+ " FROM bighorn_game WHERE state=? ORDER BY last_update_time DESC";
		logger.debug("SELECT game_id,time_rule,create_time,last_update_time,last_step,winner_id "
				+ " FROM bighorn_game WHERE state=" + state + " ORDER BY last_update_time DESC");

		return ph.fetchPage(this.getJdbcTemplate(), countSql, sql, new Object[] { state }, pageNum, pageSize,
				new ParameterizedRowMapper<GameMasterInfo>()
				{
					public GameMasterInfo mapRow(ResultSet rs, int i)
							throws SQLException
					{
						GameMasterInfo data = new GameMasterInfo();

						int n = 1;
						data.setGameId(rs.getString(n++));
						data.setTimeRule(rs.getShort(n++));
						data.setState(state);
						data.setCreateTime(rs.getInt(n++));
						data.setLastUpdateTime(rs.getInt(n++));
						data.setLastStep(rs.getShort(n++));
						data.setWinnerId(rs.getString(n++));

						return data;
					}
				});
	}

	@Override
	public Page<GameMasterInfo> queryGamePagination(short pageNum, short pageSize, final short state, String accountId)
	{
		PaginationHelper<GameMasterInfo> ph = new PaginationHelper<GameMasterInfo>();

		String countSql = "SELECT count(g.game_id) FROM bighorn_game g,bighorn_game_member p "
				+ " WHERE g.state=? AND g.game_id=p.game_id AND p.account_id=?";
		String sql = "SELECT g.game_id,g.time_rule,g.create_time,g.last_update_time,g.last_step,g.winner_id "
				+ " FROM bighorn_game g,bighorn_game_member p "
				+ " WHERE g.state=? AND g.game_id=p.game_id AND p.account_id=? ORDER BY g.last_update_time DESC";
		logger.debug("SELECT g.game_id,g.time_rule,g.create_time,g.last_update_time,g.last_step,g.winner_id "
				+ " FROM bighorn_game g,bighorn_game_member p " + " WHERE g.state=" + state
				+ " AND g.game_id=p.game_id AND p.account_id=" + accountId + " ORDER BY g.last_update_time DESC");

		return ph.fetchPage(this.getJdbcTemplate(), countSql, sql, new Object[] { state }, pageNum, pageSize,
				new ParameterizedRowMapper<GameMasterInfo>()
				{
					public GameMasterInfo mapRow(ResultSet rs, int i)
							throws SQLException
					{
						GameMasterInfo data = new GameMasterInfo();

						int n = 1;
						data.setGameId(rs.getString(n++));
						data.setTimeRule(rs.getShort(n++));
						data.setState(state);
						data.setCreateTime(rs.getInt(n++));
						data.setLastUpdateTime(rs.getInt(n++));
						data.setLastStep(rs.getShort(n++));
						data.setWinnerId(rs.getString(n++));

						return data;
					}
				});
	}

	private final static Logger logger = LoggerFactory.getLogger(GameDaoImpl.class);

}
