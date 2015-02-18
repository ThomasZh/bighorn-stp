package net.younguard.bighorn.broadcast.dao.spring;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import net.younguard.bighorn.GlobalArgs;
import net.younguard.bighorn.broadcast.dao.DeviceAccountDao;
import net.younguard.bighorn.comm.util.LogErrorMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

public class DeviceAccountDaoImpl
		extends JdbcDaoSupport
		implements DeviceAccountDao
{
	@Override
	public boolean isExist(String deviceId, String accountId)
	{
		String sql = "SELECT count(device_id) FROM bighorn_device_account WHERE device_id=? AND account_id=?";
		logger.debug("SELECT count(device_id) FROM bighorn_device_account WHERE device_id=" + deviceId
				+ " AND account_id=" + accountId);

		int count = this.getJdbcTemplate().queryForInt(sql, new Object[] { deviceId, accountId });
		return count > 0 ? true : false;
	}

	@Override
	public boolean isActive(String deviceId, String accountId)
	{
		int count = 0;

		try {
			String sql = "SELECT state FROM bighorn_device_account WHERE device_id=? AND account_id=?";
			logger.debug("SELECT state FROM bighorn_device_account WHERE device_id=" + deviceId + " AND account_id="
					+ accountId);

			Object[] params = new Object[] { deviceId, accountId };
			count = this.getJdbcTemplate().queryForInt(sql, params);
		} catch (EmptyResultDataAccessException ee) {
		} catch (IncorrectResultSizeDataAccessException ie) {
			logger.warn("SELECT state FROM bighorn_device_account WHERE device_id=" + deviceId + " AND account_id="
					+ accountId);
			logger.warn(LogErrorMessage.getFullInfo(ie));
		}

		return count == GlobalArgs.DEVICE_ACCOUNT_STATE_ACTIVE ? true : false;
	}

	@Override
	public void delete(final String deviceId, final String accountId)
	{
		String sql = "DELETE FROM bighorn_device_account WHERE device_id=? AND account_id=?";
		logger.debug("DELETE FROM bighorn_device_account WHERE device_id=" + deviceId + " AND account_id=" + accountId);

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setString(1, deviceId);
				ps.setString(2, accountId);
			}
		});
	}

	@Override
	public void add(final String deviceId, final String accountId, final int timestamp)
	{
		String sql = "INSERT INTO bighorn_device_account (device_id,account_id,state,create_time,last_update_time) VALUES (?,?,?,?,?)";
		logger.debug("INSERT INTO bighorn_device_account (device_id,account_id,state,create_time,last_update_time) VALUES ("
				+ deviceId
				+ ","
				+ accountId
				+ ","
				+ GlobalArgs.DEVICE_ACCOUNT_STATE_ACTIVE
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
				ps.setString(i++, deviceId);
				ps.setString(i++, accountId);
				ps.setShort(i++, GlobalArgs.DEVICE_ACCOUNT_STATE_ACTIVE);
				ps.setInt(i++, timestamp);
				ps.setInt(i++, timestamp);
			}
		});
	}

	@Override
	public void update(final String deviceId, final String accountId, final short state, final int timestamp)
	{
		String sql = "UPDATE bighorn_device_account SET state=?,last_update_time=? WHERE device_id=? AND account_id=?";
		logger.debug("UPDATE bighorn_device_account SET state=" + state + ",last_update_time=" + timestamp
				+ " WHERE device_id=" + deviceId + " AND account_id=" + accountId);

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				int i = 1;
				ps.setShort(i++, state);
				ps.setInt(i++, timestamp);
				ps.setString(i++, deviceId);
				ps.setString(i++, accountId);
			}
		});
	}

	private final static Logger logger = LoggerFactory.getLogger(DeviceAccountDaoImpl.class);

}
