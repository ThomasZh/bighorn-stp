package net.younguard.bighorn.broadcast.dao.spring;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.younguard.bighorn.broadcast.dao.PlayerDao;
import net.younguard.bighorn.broadcast.domain.Page;
import net.younguard.bighorn.broadcast.util.PaginationHelper;
import net.younguard.bighorn.comm.util.LogErrorMessage;
import net.younguard.bighorn.domain.PlayerDetailInfo;
import net.younguard.bighorn.domain.PlayerMasterInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

public class PlayerDaoImpl
		extends JdbcDaoSupport
		implements PlayerDao
{
	@Override
	public void add(final String accountId)
	{
		String sql = "INSERT INTO bighorn_player_summary (account_id) VALUES (?)";
		logger.debug("INSERT INTO bighorn_player_summary (account_id) VALUES (" + accountId + ")");

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setString(1, accountId);
			}
		});
	}

	@Override
	public void updateInviteNum(final String accountId, final short num)
	{
		String sql = "UPDATE bighorn_player_summary SET invite_num=? WHERE account_id=?";
		logger.debug("UPDATE bighorn_player_summary SET invite_num=" + num + " WHERE account_id=" + accountId);

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setInt(1, num);
				ps.setString(2, accountId);
			}
		});
	}

	@Override
	public void updatePlayingNum(final String accountId, final short num)
	{
		String sql = "UPDATE bighorn_player_summary SET playing_num=? WHERE account_id=?";
		logger.debug("UPDATE bighorn_player_summary SET playing_num=" + num + " WHERE account_id=" + accountId);

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setInt(1, num);
				ps.setString(2, accountId);
			}
		});
	}

	@Override
	public void updateCompletedNum(final String accountId, final short num)
	{
		String sql = "UPDATE bighorn_player_summary SET completed_num=? WHERE account_id=?";
		logger.debug("UPDATE bighorn_player_summary SET completed_num=" + num + " WHERE account_id=" + accountId);

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setInt(1, num);
				ps.setString(2, accountId);
			}
		});
	}

	@Override
	public short selectInviteNum(String accountId)
	{
		int count = 0;

		try {
			String sql = "SELECT invite_num FROM bighorn_player_summary WHERE account_id=?";
			logger.debug("SELECT invite_num FROM bighorn_player_summary WHERE account_id=" + accountId);

			Object[] params = new Object[] { accountId };
			count = this.getJdbcTemplate().queryForInt(sql, params);
		} catch (EmptyResultDataAccessException ee) {
		} catch (IncorrectResultSizeDataAccessException ie) {
			logger.warn("SELECT invite_num FROM bighorn_player_summary WHERE account_id=" + accountId);
			logger.warn(LogErrorMessage.getFullInfo(ie));
		}

		return (short) count;
	}

	@Override
	public short selectPlayingNum(String accountId)
	{
		int count = 0;

		try {
			String sql = "SELECT playing_num FROM bighorn_player_summary WHERE account_id=?";
			logger.debug("SELECT playing_num FROM bighorn_player_summary WHERE account_id=" + accountId);

			Object[] params = new Object[] { accountId };
			count = this.getJdbcTemplate().queryForInt(sql, params);
		} catch (EmptyResultDataAccessException ee) {
		} catch (IncorrectResultSizeDataAccessException ie) {
			logger.warn("SELECT playing_num FROM bighorn_player_summary WHERE account_id=" + accountId);
			logger.warn(LogErrorMessage.getFullInfo(ie));
		}

		return (short) count;
	}

	@Override
	public short selectCompletedNum(String accountId)
	{
		int count = 0;

		try {
			String sql = "SELECT completed_num FROM bighorn_player_summary WHERE account_id=?";
			logger.debug("SELECT completed_num FROM bighorn_player_summary WHERE account_id=" + accountId);

			Object[] params = new Object[] { accountId };
			count = this.getJdbcTemplate().queryForInt(sql, params);
		} catch (EmptyResultDataAccessException ee) {
		} catch (IncorrectResultSizeDataAccessException ie) {
			logger.warn("SELECT completed_num FROM bighorn_player_summary WHERE account_id=" + accountId);
			logger.warn(LogErrorMessage.getFullInfo(ie));
		}

		return (short) count;
	}

	@Override
	public Page<PlayerMasterInfo> selectPagination(short pageNum, short pageSize)
	{
		PaginationHelper<PlayerMasterInfo> ph = new PaginationHelper<PlayerMasterInfo>();

		String countSql = "SELECT count(account_id) FROM bighorn_player_summary";
		String sql = "SELECT account_id,invite_num,playing_num,completed_num "
				+ " FROM bighorn_player_summary ORDER BY completed_num DESC";
		logger.debug("SELECT account_id,invite_num,playing_num,completed_num "
				+ " FROM bighorn_player_summary ORDER BY completed_num DESC");

		return ph.fetchPage(this.getJdbcTemplate(), countSql, sql, new Object[] {}, pageNum, pageSize,
				new ParameterizedRowMapper<PlayerMasterInfo>()
				{
					public PlayerMasterInfo mapRow(ResultSet rs, int i)
							throws SQLException
					{
						PlayerMasterInfo data = new PlayerMasterInfo();

						int n = 1;
						data.setAccountId(rs.getString(n++));
						data.setInviteNum(rs.getShort(n++));
						data.setPlayingNum(rs.getShort(n++));
						data.setCompletedNum(rs.getShort(n++));

						return data;
					}
				});
	}

	@Override
	public PlayerDetailInfo select(final String accountId)
	{
		final PlayerDetailInfo data = new PlayerDetailInfo();

		String sql = "SELECT invite_num,playing_num,completed_num FROM bighorn_player_summary WHERE account_id=?";
		logger.debug("SELECT invite_num,playing_num,completed_num FROM bighorn_player_summary WHERE account_id="
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
				data.setCompletedNum(rs.getShort(n++));
			}
		});

		return data;
	}

	private final static Logger logger = LoggerFactory.getLogger(PlayerDaoImpl.class);

}
