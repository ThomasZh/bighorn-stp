package net.younguard.bighorn.broadcast.domain;

public class DeviceMasterInfo
		extends DeviceBaseInfo
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4524380880018812759L;
	private short state;

	public DeviceMasterInfo()
	{
	}

	public DeviceMasterInfo(String deviceId, String notifyToken, String osVersion, short state)
	{
		this.setDeviceId(deviceId);
		this.setNotifyToken(notifyToken);
		this.setOsVersion(osVersion);
		this.setState(state);
	}

	public short getState()
	{
		return state;
	}

	public void setState(short state)
	{
		this.state = state;
	}
}
