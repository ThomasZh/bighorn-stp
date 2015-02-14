package net.younguard.bighorn.broadcast.domain;

import java.io.Serializable;

public class DeviceBaseInfo
		implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7899890500385186619L;
	private String deviceId;
	private String osVersion;
	private String notifyToken;

	public String getDeviceId()
	{
		return deviceId;
	}

	public void setDeviceId(String deviceId)
	{
		this.deviceId = deviceId;
	}

	public String getNotifyToken()
	{
		return notifyToken;
	}

	public void setNotifyToken(String notifyToken)
	{
		this.notifyToken = notifyToken;
	}

	public String getOsVersion()
	{
		return osVersion;
	}

	public void setOsVersion(String osVersion)
	{
		this.osVersion = osVersion;
	}

}
