package net.younguard.bighorn.broadcast.dao.spring;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.younguard.bighorn.GlobalArgs;
import net.younguard.bighorn.broadcast.dao.GameMemberDao;
import net.younguard.bighorn.domain.GameMemberMasterInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

public class GameMemberDaoImpl
		extends JdbcDaoSupport
		implements GameMemberDao
{
	@Override
	public void add(final String gameId, final String playerId, final short color, final int timestamp)
	{
		String sql = "INSERT INTO bighorn_game_member (game_id,account_id,state,color,create_time,last_update_time) VALUES (?,?,?,?,?,?)";
		logger.debug("INSERT INTO bighorn_game_member (game_id,account_id,state,color,create_time,last_update_time) VALUES ("
				+ gameId
				+ ","
				+ playerId
				+ ","
				+ GlobalArgs.GAME_MEMBER_STATE_PLAYING
				+ ","
				+ color
				+ ","
				+ timestamp
				+ "," + timestamp + ")");

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				int i = 1;
				ps.setString(i++, gameId);
				ps.setString(i++, playerId);
				ps.setShort(i++, GlobalArgs.GAME_MEMBER_STATE_PLAYING);
				ps.setShort(i++, color);
				ps.setInt(i++, timestamp);
				ps.setInt(i++, timestamp);
			}
		});
	}

	@Override
	public List<GameMemberMasterInfo> select(final String gameId)
	{
		final List<GameMemberMasterInfo> array = new ArrayList<GameMemberMasterInfo>();

		String sql = "SELECT account_id,state FROM bighorn_game_member WHERE game_id=?";
		logger.debug("SELECT account_id,state FROM bighorn_game_member WHERE game_id=" + gameId);

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
				GameMemberMasterInfo data = new GameMemberMasterInfo();

				int n = 1;
				data.setAccountId(gameId);
				data.setState(rs.getShort(n++));

				array.add(data);
			}
		});

		return array;
	}

	private final static Logger logger = LoggerFactory.getLogger(GameMemberDaoImpl.class);

}
