package net.younguard.bighorn.broadcast.dao;

import net.younguard.bighorn.broadcast.domain.DeviceMasterInfo;

public interface DeviceDao
{
	public void add(String deviceId, String notifyToken, String username, short state, int timestamp);

	public void update(String deviceId, String notifyToken, String username, short state, int timestamp);

	public DeviceMasterInfo select(String deviceId);

	public void delete(String deviceId);

	public boolean isExist(String deviceId);
}
