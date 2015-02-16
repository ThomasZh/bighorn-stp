package net.younguard.bighorn.broadcast.dao.spring;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.younguard.bighorn.broadcast.dao.GameManualDao;
import net.younguard.bighorn.domain.GameStep;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

public class GameManualDaoImpl
		extends JdbcDaoSupport
		implements GameManualDao
{
	@Override
	public void add(final String gameId, final String accountId, final short step, final short color, final short x,
			final short y, final int timestamp)
	{
		String sql = "INSERT INTO bighorn_game_manual (game_id,player_id,step,color,x,y,timestamp) VALUES (?,?,?,?,?,?,?)";
		logger.debug("INSERT INTO bighorn_game_manual (game_id,player_id,step,color,x,y,timestamp) VALUES (" + gameId
				+ "," + accountId + "," + step + "," + color + "," + x + "," + y + "," + timestamp + ")");

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				int i = 1;
				ps.setString(i++, gameId);
				ps.setString(i++, accountId);
				ps.setShort(i++, step);
				ps.setShort(i++, color);
				ps.setShort(i++, x);
				ps.setShort(i++, y);
				ps.setInt(i++, timestamp);
			}
		});
	}

	@Override
	public List<GameStep> select(final String gameId, short lastStep)
	{
		final List<GameStep> array = new ArrayList<GameStep>();

		String sql = "SELECT step,color,x,y FROM bighorn_game_manual WHERE game_id=? AND step>? ORDER BY step ASC";
		logger.debug("SELECT step,color,x,y FROM bighorn_game_manual WHERE game_id=" + gameId + " AND step>" + lastStep
				+ " ORDER BY step ASC");

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
				GameStep data = new GameStep();

				int n = 1;
				data.setStep(rs.getShort(n++));
				data.setColor(rs.getShort(n++));
				data.setX(rs.getShort(n++));
				data.setY(rs.getShort(n++));

				array.add(data);
			}
		});

		return array;
	}

	private final static Logger logger = LoggerFactory.getLogger(PlayerDaoImpl.class);

}
