package net.younguard.bighorn.broadcast.dao;

public interface DeviceAccountDao
{
	public boolean isExist(String deviceId, String accountId);

	public boolean isActive(String deviceId, String accountId);

	public void delete(String deviceId, String accountId);

	public void add(String deviceId, String accountId, int timestamp);

	public void update(String deviceId, String accountId, short state, int timestamp);
}
