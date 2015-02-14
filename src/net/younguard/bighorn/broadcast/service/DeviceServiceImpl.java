package net.younguard.bighorn.broadcast.service;

import java.util.List;

import net.younguard.bighorn.broadcast.dao.DeviceAccountDao;
import net.younguard.bighorn.broadcast.dao.DeviceDao;
import net.younguard.bighorn.broadcast.domain.DeviceDetailInfo;
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
		deviceDao.add(device.getDeviceId(), device.getNotifyToken(), device.getOsVersion(), device.getState(),
				timestamp);
	}

	@Override
	public void modify(DeviceMasterInfo device, int timestamp)
	{
		deviceDao.update(device.getDeviceId(), device.getNotifyToken(), device.getOsVersion(), device.getState(),
				timestamp);
	}

	@Override
	public void modifyLastUpdateTime(String deviceId, int timestamp)
	{
		deviceDao.update(deviceId, timestamp);
	}

	@Override
	public DeviceMasterInfo query(String deviceId)
	{
		return deviceDao.select(deviceId);
	}

	@Override
	public List<DeviceDetailInfo> queryAll()
	{
		return deviceDao.selectAll();
	}

	@Override
	public void remove(String deviceId)
	{
		deviceDao.delete(deviceId);
	}

	@Override
	public void bind(String deviceId, String accountId, int timestamp)
	{
		if (accountDeviceDao.isExist(deviceId, accountId)) {
			; // do nothing
		} else {
			accountDeviceDao.add(deviceId, accountId, timestamp);
		}
	}

	// ////////////////////////////////////////////////////////////

	private DeviceDao deviceDao;
	private DeviceAccountDao accountDeviceDao;

	public DeviceDao getDeviceDao()
	{
		return deviceDao;
	}

	public void setDeviceDao(DeviceDao deviceDao)
	{
		this.deviceDao = deviceDao;
	}

	public DeviceAccountDao getAccountDeviceDao()
	{
		return accountDeviceDao;
	}

	public void setAccountDeviceDao(DeviceAccountDao accountDeviceDao)
	{
		this.accountDeviceDao = accountDeviceDao;
	}

}
