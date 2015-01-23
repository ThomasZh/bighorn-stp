package net.younguard.bighorn.broadcast.service;

import net.younguard.bighorn.broadcast.dao.DeviceDao;
import net.younguard.bighorn.broadcast.domain.DeviceMasterInfo;

public class DeviceServiceImpl
		implements DeviceService
{
	@Override
	public boolean isExist(String deviceId)
	{
		return deviceDao.isExist(deviceId);
	}

	@Override
	public void create(DeviceMasterInfo device, int timestamp)
	{
		deviceDao
				.add(device.getDeviceId(), device.getNotifyToken(), device.getUsername(), device.getState(), timestamp);
	}

	@Override
	public void modify(DeviceMasterInfo device, int timestamp)
	{
		deviceDao.update(device.getDeviceId(), device.getNotifyToken(), device.getUsername(), device.getState(),
				timestamp);
	}

	@Override
	public DeviceMasterInfo query(String deviceId)
	{
		return deviceDao.select(deviceId);
	}

	@Override
	public void remove(String deviceId)
	{
		deviceDao.delete(deviceId);
	}

	// ////////////////////////////////////////////////////////////

	private DeviceDao deviceDao;

	public DeviceDao getDeviceDao()
	{
		return deviceDao;
	}

	public void setDeviceDao(DeviceDao deviceDao)
	{
		this.deviceDao = deviceDao;
	}

}
