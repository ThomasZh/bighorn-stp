package net.younguard.bighorn.broadcast.dao.spring;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.younguard.bighorn.GlobalArgs;
import net.younguard.bighorn.broadcast.dao.AccountDao;
import net.younguard.bighorn.domain.AccountBaseInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

public class AccountDaoImpl
		extends JdbcDaoSupport
		implements AccountDao
{
	@Override
	public boolean isExist(String accountId)
	{
		String sql = "SELECT count(account_id) FROM bighorn_account WHERE account_id=?";
		logger.debug("SELECT count(account_id) FROM bighorn_account WHERE account_id=" + accountId);

		int count = this.getJdbcTemplate().queryForInt(sql, accountId);
		return count > 0 ? true : false;
	}

	@Override
	public void add(final String accountId, final String nickname, final String avatarUrl, final int timestamp)
	{
		String sql = "INSERT INTO bighorn_account (account_id,nickname,avatar_url,state,create_time,last_update_time) VALUES (?,?,?,?,?,?)";
		logger.debug("INSERT INTO bighorn_account (account_id,nickname,avatar_url,state,create_time,last_update_time) VALUES ("
				+ accountId
				+ ","
				+ nickname
				+ ","
				+ avatarUrl
				+ ","
				+ GlobalArgs.ACCOUNT_STATE_REGISTER
				+ ","
				+ timestamp + "," + timestamp + ")");

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				int i = 1;
				ps.setString(i++, accountId);
				ps.setString(i++, nickname);
				ps.setString(i++, avatarUrl);
				ps.setShort(i++, GlobalArgs.ACCOUNT_STATE_REGISTER);
				ps.setInt(i++, timestamp);
				ps.setInt(i++, timestamp);
			}
		});
	}

	@Override
	public void update(final String accountId, final String nickname, final String avatarUrl, final int timestamp)
	{
		String sql = "UPDATE bighorn_account SET nickname=?,avatar_url=?,last_update_time=? WHERE account_id=?";
		logger.debug("UPDATE bighorn_account SET nickname=" + nickname + ",avatar_url=" + avatarUrl
				+ ",last_update_time=" + timestamp + " WHERE account_id=" + accountId);

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				int i = 1;
				ps.setString(i++, nickname);
				ps.setString(i++, avatarUrl);
				ps.setInt(i++, timestamp);
				ps.setString(i++, accountId);
			}
		});
	}

	@Override
	public AccountBaseInfo select(final String accountId)
	{
		final AccountBaseInfo data = new AccountBaseInfo();

		String sql = "SELECT nickname,avatar_url FROM bighorn_account WHERE account_id=?";
		logger.debug("SELECT nickname,avatar_url FROM bighorn_account WHERE account_id=" + accountId);

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
				int i = 1;
				data.setAccountId(accountId);
				data.setNickname(rs.getString(i++));
				data.setAvatarUrl(rs.getString(i++));
			}
		});

		return data;
	}

	@Override
	public void delete(final String accountId)
	{
		String sql = "DELETE FROM bighorn_account WHERE account_id=?";
		logger.debug("DELETE FROM bighorn_account WHERE account_id=" + accountId);

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setString(1, accountId);
			}
		});
	}

	private final static Logger logger = LoggerFactory.getLogger(AccountDaoImpl.class);

}
