package net.younguard.bighorn.broadcast.dao.spring;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.younguard.bighorn.broadcast.dao.DeviceDao;
import net.younguard.bighorn.broadcast.domain.DeviceDetailInfo;
import net.younguard.bighorn.broadcast.domain.DeviceMasterInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

public class DeviceDaoImpl
		extends JdbcDaoSupport
		implements DeviceDao
{
	@Override
	public void add(final String deviceId, final String notifyToken, final String osVersion, final short state,
			final int timestamp)
	{
		String sql = "INSERT INTO bighorn_device (device_id,os_version,notify_token,state,create_time,last_update_time) VALUES (?,?,?,?,?,?)";
		logger.debug("INSERT INTO bighorn_device (device_id,os_version,notify_token,state,create_time,last_update_time) VALUES ("
				+ deviceId
				+ ","
				+ osVersion
				+ ","
				+ notifyToken
				+ ","
				+ state
				+ ","
				+ timestamp
				+ ","
				+ timestamp
				+ ")");

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				int i = 1;
				ps.setString(i++, deviceId);
				ps.setString(i++, osVersion);
				ps.setString(i++, notifyToken);
				ps.setShort(i++, state);
				ps.setInt(i++, timestamp);
				ps.setInt(i++, timestamp);
			}
		});
	}

	@Override
	public void update(final String deviceId, final String notifyToken, final String osVersion, final short state,
			final int timestamp)
	{
		String sql = "UPDATE bighorn_device SET os_version=?,notify_token=?,state=?,last_update_time=? WHERE device_id=?";
		logger.debug("UPDATE bighorn_device SET os_version=" + osVersion + ",notify_token=" + notifyToken + ",state="
				+ state + ",last_update_time=" + timestamp + " WHERE device_id=" + deviceId);

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				int i = 1;
				ps.setString(i++, osVersion);
				ps.setString(i++, notifyToken);
				ps.setShort(i++, state);
				ps.setInt(i++, timestamp);
				ps.setString(i++, deviceId);
			}
		});
	}

	@Override
	public void update(final String deviceId, final int timestamp)
	{
		String sql = "UPDATE bighorn_device SET last_update_time=? WHERE device_id=?";
		logger.debug("UPDATE bighorn_device SET last_update_time=" + timestamp + " WHERE device_id=" + deviceId);

		this.getJdbcTemplate().update(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				int i = 1;
				ps.setInt(i++, timestamp);
				ps.setString(i++, deviceId);
			}
		});
	}

	@Override
	public DeviceMasterInfo select(final String deviceId)
	{
		final DeviceMasterInfo device = new DeviceMasterInfo();

		String sql = "SELECT os_version,notify_token,state FROM bighorn_device WHERE device_id=?";
		logger.debug("SELECT os_version,notify_token,state FROM bighorn_device WHERE device_id=" + deviceId);

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
				device.setOsVersion(rs.getString(i++));
				device.setNotifyToken(rs.getString(i++));
				device.setState(rs.getShort(i++));
			}
		});

		return device;
	}

	@Override
	public List<DeviceDetailInfo> selectAll()
	{
		final List<DeviceDetailInfo> array = new ArrayList<DeviceDetailInfo>();

		String sql = "SELECT device_id,os_version,notify_token,state,last_update_time FROM bighorn_device";
		logger.debug("SELECT device_id,os_version,notify_token,state,last_update_time FROM bighorn_device");

		this.getJdbcTemplate().query(sql, new PreparedStatementSetter()
		{
			public void setValues(PreparedStatement ps)
					throws SQLException
			{
			}
		}, new RowCallbackHandler()
		{
			public void processRow(ResultSet rs)
					throws SQLException
			{
				DeviceDetailInfo device = new DeviceDetailInfo();

				int i = 1;
				device.setDeviceId(rs.getString(i++));
				device.setOsVersion(rs.getString(i++));
				device.setNotifyToken(rs.getString(i++));
				device.setState(rs.getShort(i++));
				device.setLastTryTime(rs.getInt(i++));

				array.add(device);
			}
		});

		return array;
	}

	@Override
	public void delete(final String deviceId)
	{
		String sql = "DELETE FROM bighorn_device WHERE device_id=?";
		logger.debug("DELETE FROM bighorn_device WHERE device_id=" + deviceId);

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
		String sql = "SELECT count(device_id) FROM bighorn_device WHERE device_id=?";
		logger.debug("SELECT count(device_id) FROM bighorn_device WHERE device_id=" + deviceId);

		int count = this.getJdbcTemplate().queryForInt(sql, deviceId);
		
		logger.debug("count: " + count);
		return count > 0 ? true : false;
	}

	private final static Logger logger = LoggerFactory.getLogger(DeviceDaoImpl.class);
}
