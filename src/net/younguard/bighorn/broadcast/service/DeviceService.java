package net.younguard.bighorn.broadcast.service;

import java.util.List;

import net.younguard.bighorn.broadcast.domain.DeviceDetailInfo;
import net.younguard.bighorn.broadcast.domain.DeviceMasterInfo;

public interface DeviceService
{
	public boolean isExist(String deviceId);

	public void create(DeviceMasterInfo device, int timestamp);

	public void modify(DeviceMasterInfo device, int timestamp);

	public void modifyLastUpdateTime(String deviceId, int timestamp);

	public DeviceMasterInfo query(String deviceId);

	public List<DeviceDetailInfo> queryAll();

	public void remove(String deviceId);
}
