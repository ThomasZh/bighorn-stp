package net.younguard.bighorn.broadcast.dao.spring;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.younguard.bighorn.broadcast.dao.AccountLoginDao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

public class AccountLoginDaoImpl
		extends JdbcDaoSupport
		implements AccountLoginDao
{
	@Override
	public boolean isExist(short loginType, String loginName)
	{
		String sql = "SELECT count(login_name) FROM bighorn_account_login WHERE login_type=? AND login_name=?";
		logger.debug("SELECT count(login_name) FROM bighorn_account_login WHERE login_type=" + loginType
				+ " AND login_name=" + loginName);

		int count = this.getJdbcTemplate().queryForInt(sql, loginType, loginName);
		return count > 0 ? true : false;
	}

	@Override
	public void add(final short loginType, final String loginName, final String accountId, final int timestamp)
	{
		String sql = "INSERT INTO bighorn_account_login (login_type,login_name,account_id,create_time,last_update_time) VALUES (?,?,?,?,?)";
		logger.debug("INSERT INTO bighorn_account_login (login_type,login_name,account_id,create_time,last_update_time) VALUES ("
				+ loginType + "," + loginName + "," + accountId + "," + timestamp + "," + timestamp + ")");

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				int i = 1;

				ps.setShort(i++, loginType);
				ps.setString(i++, loginName);
				ps.setString(i++, accountId);
				ps.setInt(i++, timestamp);
				ps.setInt(i++, timestamp);
			}
		});
	}

	@Override
	public void add(final short loginType, final String loginName, final String accountId, final String ecryptPwd,
			final String salt, final int timestamp)
	{
		String sql = "INSERT INTO bighorn_account_login (login_type,login_name,account_id,ecrypt_pwd,salt,create_time,last_update_time) VALUES (?,?,?,?,?,?,?)";
		logger.debug("INSERT INTO bighorn_account_login (login_type,login_name,account_id,ecrypt_pwd,salt,create_time,last_update_time) VALUES ("
				+ loginType + "," + loginName + "," + accountId + ",?,?," + timestamp + "," + timestamp + ")");

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				int i = 1;

				ps.setShort(i++, loginType);
				ps.setString(i++, loginName);
				ps.setString(i++, accountId);
				ps.setString(i++, ecryptPwd);
				ps.setString(i++, salt);
				ps.setInt(i++, timestamp);
				ps.setInt(i++, timestamp);
			}
		});
	}

	@Override
	public String selectSalt(final short loginType, final String loginName)
	{
		final List<String> array = new ArrayList<String>();

		String sql = "SELECT salt FROM bighorn_account_login WHERE login_type=? AND login_name=?";
		logger.debug("SELECT salt FROM bighorn_account_login WHERE login_type=" + loginType + " AND login_name="
				+ loginName);

		this.getJdbcTemplate().query(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setShort(1, loginType);
				ps.setString(2, loginName);
			}
		}, new RowCallbackHandler()
		{
			public void processRow(ResultSet rs)
					throws SQLException
			{
				String userId = rs.getString(1);

				array.add(userId);
			}
		});

		if (array.size() > 0)
			return array.get(0);
		else
			return "";
	}

	@Override
	public String select(final short loginType, final String loginName)
	{
		final List<String> array = new ArrayList<String>();

		String sql = "SELECT account_id FROM bighorn_account_login WHERE login_type=? AND login_name=?";
		logger.debug("SELECT account_id FROM bighorn_account_login WHERE login_type=" + loginType + " AND login_name="
				+ loginName);

		this.getJdbcTemplate().query(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setShort(1, loginType);
				ps.setString(2, loginName);
			}
		}, new RowCallbackHandler()
		{
			public void processRow(ResultSet rs)
					throws SQLException
			{
				String userId = rs.getString(1);

				array.add(userId);
			}
		});

		if (array.size() > 0)
			return array.get(0);
		else
			return "";
	}

	@Override
	public String select(final short loginType, final String loginName, final String ecryptPwd)
	{
		final List<String> array = new ArrayList<String>();

		String sql = "SELECT account_id FROM bighorn_account_login WHERE login_type=? AND login_name=? AND ecrypt_pwd=?";
		logger.debug("SELECT account_id FROM bighorn_account_login WHERE login_type=" + loginType + " AND login_name="
				+ loginName + " AND ecrypt_pwd=" + ecryptPwd);

		this.getJdbcTemplate().query(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setShort(1, loginType);
				ps.setString(2, loginName);
				ps.setString(3, ecryptPwd);
			}
		}, new RowCallbackHandler()
		{
			public void processRow(ResultSet rs)
					throws SQLException
			{
				String userId = rs.getString(1);

				array.add(userId);
			}
		});

		if (array.size() > 0)
			return array.get(0);
		else
			return "";
	}

	private final static Logger logger = LoggerFactory.getLogger(AccountLoginDaoImpl.class);

}
