package net.younguard.bighorn.badgenum.dao.spring;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import net.younguard.bighorn.badgenum.dao.BadgeNumDao;
import net.younguard.bighorn.comm.util.LogErrorMessage;
import net.younguard.bighorn.domain.badge.ListBadgeNumber;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

public class BadgeNumDaoImpl
		extends JdbcDaoSupport
		implements BadgeNumDao
{
	@Override
	public boolean isExist(String accountId)
	{
		String sql = "SELECT count(account_id) FROM bighorn_badge_num WHERE account_id=?";
		logger.debug("SELECT count(account_id) FROM bighorn_badge_num WHERE account_id=" + accountId);

		Object[] params = new Object[] { accountId };
		int count = this.getJdbcTemplate().queryForInt(sql, params);
		return count > 0 ? true : false;
	}

	@Override
	public void add(final String accountId)
	{
		String sql = "INSERT INTO bighorn_badge_num (account_id) VALUES (?)";
		logger.debug("INSERT INTO bighorn_badge_num (account_id) VALUES (" + accountId + ")");

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
	public short selectInviteNum(String accountId)
	{
		int count = 0;

		try {
			String sql = "SELECT invite_num FROM bighorn_badge_num WHERE account_id=?";
			logger.debug("SELECT invite_num FROM bighorn_badge_num WHERE account_id=" + accountId);

			Object[] params = new Object[] { accountId };
			count = this.getJdbcTemplate().queryForInt(sql, params);
		} catch (EmptyResultDataAccessException ee) {
		} catch (IncorrectResultSizeDataAccessException ie) {
			logger.warn("SELECT invite_num FROM bighorn_badge_num WHERE account_id=" + accountId);
			logger.warn(LogErrorMessage.getFullInfo(ie));
		}

		return (short) count;
	}

	@Override
	public short selectPlayingNum(String accountId)
	{
		int count = 0;

		try {
			String sql = "SELECT playing_num FROM bighorn_badge_num WHERE account_id=?";
			logger.debug("SELECT playing_num FROM bighorn_badge_num WHERE account_id=" + accountId);

			Object[] params = new Object[] { accountId };
			count = this.getJdbcTemplate().queryForInt(sql, params);
		} catch (EmptyResultDataAccessException ee) {
		} catch (IncorrectResultSizeDataAccessException ie) {
			logger.warn("SELECT playing_num FROM bighorn_badge_num WHERE account_id=" + accountId);
			logger.warn(LogErrorMessage.getFullInfo(ie));
		}

		return (short) count;
	}

	@Override
	public short selectHistoryNum(String accountId)
	{
		int count = 0;

		try {
			String sql = "SELECT history_num FROM bighorn_badge_num WHERE account_id=?";
			logger.debug("SELECT history_num FROM bighorn_badge_num WHERE account_id=" + accountId);

			Object[] params = new Object[] { accountId };
			count = this.getJdbcTemplate().queryForInt(sql, params);
		} catch (EmptyResultDataAccessException ee) {
		} catch (IncorrectResultSizeDataAccessException ie) {
			logger.warn("SELECT history_num FROM bighorn_badge_num WHERE account_id=" + accountId);
			logger.warn(LogErrorMessage.getFullInfo(ie));
		}

		return (short) count;
	}

	@Override
	public void updateInviteNum(final String accountId,final short num)
	{
		String sql = "UPDATE bighorn_badge_num SET invite_num=? WHERE account_id=?";
		logger.debug("UPDATE bighorn_badge_num SET invite_num=" + num + " WHERE account_id=" + accountId);

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setShort(1, num);
				ps.setString(2, accountId);
			}
		});
	}

	@Override
	public void updatePlayingNum(final String accountId,final short num)
	{
		String sql = "UPDATE bighorn_badge_num SET playing_num=? WHERE account_id=?";
		logger.debug("UPDATE bighorn_badge_num SET playing_num=" + num + " WHERE account_id=" + accountId);

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setShort(1, num);
				ps.setString(2, accountId);
			}
		});
	}

	@Override
	public void updateHistoryNum(final String accountId,final short num)
	{
		String sql = "UPDATE bighorn_badge_num SET history_num=? WHERE account_id=?";
		logger.debug("UPDATE bighorn_badge_num SET history_num=" + num + " WHERE account_id=" + accountId);

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setShort(1, num);
				ps.setString(2, accountId);
			}
		});
	}

	@Override
	public short countInviteNum(String accountId)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public short countPlayingNum(String accountId)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public short countHistoryNum(String accountId)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<ListBadgeNumber> queryInviteNumList(String accountId)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ListBadgeNumber> queryPlayingNumList(String accountId)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ListBadgeNumber> queryHistoryNumList(String accountId)
	{
		// TODO Auto-generated method stub
		return null;
	}

	private final static Logger logger = LoggerFactory.getLogger(BadgeNumDaoImpl.class);
}
