package net.younguard.bighorn.broadcast.dao.spring;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.younguard.bighorn.broadcast.dao.DeviceDao;
import net.younguard.bighorn.broadcast.domain.DeviceMasterInfo;

import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

public class DeviceDaoImpl
		extends JdbcDaoSupport
		implements DeviceDao
{
	@Override
	public void add(final String deviceId, final String notifyToken, final String username, final short state,
			final int timestamp)
	{
		String sql = "INSERT INTO user_device (device_id,username,notify_token,state,create_time,last_update_time) VALUES (?,?,?,?,?,?)";
		logger.debug("INSERT INTO user_device (device_id,username,notify_token,state,create_time,last_update_time) VALUES ("
				+ deviceId + "," + username + "," + notifyToken + "," + state + "," + timestamp + "," + timestamp + ")");

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				int i = 1;
				ps.setString(i++, deviceId);
				ps.setString(i++, username);
				ps.setString(i++, notifyToken);
				ps.setShort(i++, state);
				ps.setInt(i++, timestamp);
				ps.setInt(i++, timestamp);
			}
		});
	}

	@Override
	public void update(final String deviceId, final String notifyToken, final String username, final short state,
			final int timestamp)
	{
		String sql = "UPDATE user_device SET username=?,notify_token=?,state=?,last_update_time=? WHERE device_id=?";
		logger.debug("UPDATE user_device SET username=" + username + ",notify_token=" + notifyToken + ",state=" + state
				+ ",last_update_time=" + timestamp + " WHERE device_id=" + deviceId);

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				int i = 1;
				ps.setString(i++, username);
				ps.setString(i++, notifyToken);
				ps.setShort(i++, state);
				ps.setInt(i++, timestamp);
				ps.setString(i++, deviceId);
			}
		});
	}

	@Override
	public DeviceMasterInfo select(final String deviceId)
	{
		final DeviceMasterInfo device = new DeviceMasterInfo();

		String sql = "SELECT username,notify_token,state FROM user_device WHERE device_id=?";
		logger.debug("SELECT username,notify_token,state FROM user_device WHERE device_id=" + deviceId);

		this.getJdbcTemplate().query(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setString(1, deviceId);
			}
		}, new RowCallbackHandler()
		{
			public void processRow(ResultSet rs)
					throws SQLException
			{
				int i = 1;
				device.setDeviceId(deviceId);
				device.setUsername(rs.getString(i++));
				device.setNotifyToken(rs.getString(i++));
				device.setState(rs.getShort(i++));
			}
		});

		return device;
	}

	@Override
	public void delete(final String deviceId)
	{
		String sql = "DELETE FROM user_device WHERE device_id=?";
		logger.debug("DELETE FROM user_device WHERE device_id=" + deviceId);

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setString(1, deviceId);
			}
		});
	}

	@Override
	public boolean isExist(String deviceId)
	{
		String sql = "SELECT count(device_id) FROM user_device WHERE device_id=?";
		logger.debug("SELECT count(device_id) FROM user_device WHERE device_id=" + deviceId);

		int count = this.getJdbcTemplate().queryForObject(sql, Integer.class);
		return count > 0 ? true : false;
	}

}
