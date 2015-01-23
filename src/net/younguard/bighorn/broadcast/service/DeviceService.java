package net.younguard.bighorn.broadcast.service;

import net.younguard.bighorn.broadcast.domain.DeviceMasterInfo;

public interface DeviceService
{
	public boolean isExist(String deviceId);

	public void create(DeviceMasterInfo device, int timestamp);

	public void modify(DeviceMasterInfo device, int timestamp);

	public DeviceMasterInfo query(String deviceId);

	public void remove(String deviceId);
}
