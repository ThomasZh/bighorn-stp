package net.younguard.bighorn.broadcast.dao.spring;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.younguard.bighorn.GlobalArgs;
import net.younguard.bighorn.broadcast.dao.PlayerDao;
import net.younguard.bighorn.broadcast.domain.Page;
import net.younguard.bighorn.broadcast.util.PaginationHelper;
import net.younguard.bighorn.domain.PlayerSummary;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

public class PlayerDaoImpl
		extends JdbcDaoSupport
		implements PlayerDao
{
	@Override
	public void add(final String gameId, final String playerId, final short color, final int timestamp)
	{
		String sql = "INSERT INTO bighorn_game_player (game_id,player_id,state,color,create_time,last_update_time) VALUES (?,?,?,?,?,?,?)";
		logger.debug("INSERT INTO bighorn_game_player (game_id,player_id,state,color,create_time,last_update_time) VALUES ("
				+ gameId
				+ ","
				+ playerId
				+ ","
				+ GlobalArgs.PLAYER_STATE_PLAYING
				+ ","
				+ color
				+ ","
				+ timestamp
				+ ","
				+ timestamp + ")");

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				int i = 1;
				ps.setString(i++, gameId);
				ps.setString(i++, playerId);
				ps.setShort(i++, GlobalArgs.PLAYER_STATE_PLAYING);
				ps.setShort(i++, color);
				ps.setInt(i++, timestamp);
				ps.setInt(i++, timestamp);
			}
		});
	}

	@Override
	public Page<PlayerSummary> queryPlayersPagination(short pageNum, short pageSize)
	{
		PaginationHelper<PlayerSummary> ph = new PaginationHelper<PlayerSummary>();

		String countSql = "SELECT count(player_id) FROM bighorn_game_player WHERE state=?";
		String sql = "SELECT player_id,invite_num,playing_num,played_num "
				+ " FROM bighorn_game_player WHERE state>? ORDER BY played_num DESC";
		logger.debug("SELECT player_id,invite_num,playing_num,played_num " + " FROM bighorn_game_player WHERE state>"
				+ GlobalArgs.ACCOUNT_STATE_INACTIVE + " ORDER BY played_num DESC");

		return ph.fetchPage(this.getJdbcTemplate(), countSql, sql, new Object[] { GlobalArgs.ACCOUNT_STATE_INACTIVE },
				pageNum, pageSize, new ParameterizedRowMapper<PlayerSummary>()
				{
					public PlayerSummary mapRow(ResultSet rs, int i)
							throws SQLException
					{
						PlayerSummary data = new PlayerSummary();

						int n = 1;
						data.setAccountId(rs.getString(n++));
						data.setInviteNum(rs.getShort(n++));
						data.setPlayingNum(rs.getShort(n++));
						data.setPlayedNum(rs.getShort(n++));

						return data;
					}
				});
	}

	@Override
	public PlayerSummary select(final String accountId)
	{
		final PlayerSummary data = new PlayerSummary();

		String sql = "SELECT player_id,invite_num,playing_num,played_num FROM bighorn_game_player WHERE player_id=?";
		logger.debug("SELECT player_id,invite_num,playing_num,played_num FROM bighorn_game_player WHERE player_id="
				+ accountId);

		this.getJdbcTemplate().query(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setString(1, accountId);
			}
		}, new RowCallbackHandler()
		{
			public void processRow(ResultSet rs)
					throws SQLException
			{
				int n = 1;
				data.setAccountId(accountId);
				data.setInviteNum(rs.getShort(n++));
				data.setPlayingNum(rs.getShort(n++));
				data.setPlayedNum(rs.getShort(n++));
			}
		});

		return data;
	}

	@Override
	public List<PlayerSummary> selectPlayers(final String gameId)
	{
		final List<PlayerSummary> array = new ArrayList<PlayerSummary>();

		String sql = "SELECT player_id,invite_num,playing_num,played_num FROM bighorn_game_player WHERE player_id=?";
		logger.debug("SELECT player_id,invite_num,playing_num,played_num FROM bighorn_game_player WHERE player_id="
				+ gameId);

		this.getJdbcTemplate().query(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setString(1, gameId);
			}
		}, new RowCallbackHandler()
		{
			public void processRow(ResultSet rs)
					throws SQLException
			{
				PlayerSummary data = new PlayerSummary();

				int n = 1;
				data.setAccountId(gameId);
				data.setInviteNum(rs.getShort(n++));
				data.setPlayingNum(rs.getShort(n++));
				data.setPlayedNum(rs.getShort(n++));

				array.add(data);
			}
		});

		return array;
	}

	private final static Logger logger = LoggerFactory.getLogger(PlayerDaoImpl.class);

}
