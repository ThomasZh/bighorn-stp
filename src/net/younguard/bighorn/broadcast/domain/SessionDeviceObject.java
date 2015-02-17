package net.younguard.bighorn.broadcast.domain;

import java.io.Serializable;

/**
 * store this object(deviceId,notifyToken,username,isOnline,minaIoSessionId)
 * into session map.
 * 
 * Copyright 2014-2015 by Young Guard Salon Community, China. All rights
 * reserved. http://www.younguard.net
 * 
 * NOTICE ! You can copy or redistribute this code freely, but you should not
 * remove the information about the copyright notice and the author.
 * 
 * @author ThomasZhang, thomas.zh@qq.com
 */
public class SessionDeviceObject
		implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8733069412616001764L;
	private String osVersion;
	private String notifyToken;
	private boolean online;
	private int lastTryTime;
	private long ioSessionId;
	private String accountId;

	public SessionDeviceObject(String osVersion, String notifyToken)
	{
		this.setOsVersion(osVersion);
		this.setNotifyToken(notifyToken);
	}

	public SessionDeviceObject(String osVersion, String notifyToken, boolean online, int timestamp, long ioSessionId)
	{
		this(osVersion, notifyToken);

		this.setOnline(online);
		this.setLastTryTime(timestamp);
		this.setIoSessionId(ioSessionId);
	}

	public SessionDeviceObject(String osVersion, String notifyToken, boolean online, int timestamp, long ioSessionId,
			String accountId)
	{
		this(osVersion, notifyToken, online, timestamp, ioSessionId);

		this.setAccountId(accountId);
	}

	public String getOsVersion()
	{
		return osVersion;
	}

	public void setOsVersion(String osVersion)
	{
		this.osVersion = osVersion;
	}

	public String getNotifyToken()
	{
		return notifyToken;
	}

	public void setNotifyToken(String notifyToken)
	{
		this.notifyToken = notifyToken;
	}

	public boolean isOnline()
	{
		return online;
	}

	public void setOnline(boolean online)
	{
		this.online = online;
	}

	public int getLastTryTime()
	{
		return lastTryTime;
	}

	public void setLastTryTime(int lastTryTime)
	{
		this.lastTryTime = lastTryTime;
	}

	public long getIoSessionId()
	{
		return ioSessionId;
	}

	public void setIoSessionId(long ioSessionId)
	{
		this.ioSessionId = ioSessionId;
	}

	public String getAccountId()
	{
		return accountId;
	}

	public void setAccountId(String accountId)
	{
		this.accountId = accountId;
	}

}
