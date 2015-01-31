package net.younguard.bighorn.broadcast.domain;

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
public class SessionObject
		extends DeviceBaseInfo
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8733069412616001764L;

	public SessionObject(String deviceId, String notifyToken, String username, boolean online, long ioSessionId,
			int timestamp)
	{
		this.setDeviceId(deviceId);
		this.setNotifyToken(notifyToken);
		this.setUsername(username);
		this.setOnline(online);
		this.setIoSessionId(ioSessionId);
		this.setLastTrtTime(timestamp);
	}

	private boolean online;
	private long ioSessionId;
	private int lastTrtTime;

	public boolean isOnline()
	{
		return online;
	}

	public void setOnline(boolean online)
	{
		this.online = online;
	}

	public long getIoSessionId()
	{
		return ioSessionId;
	}

	public void setIoSessionId(long ioSessionId)
	{
		this.ioSessionId = ioSessionId;
	}

	public int getLastTrtTime()
	{
		return lastTrtTime;
	}

	public void setLastTrtTime(int lastTrtTime)
	{
		this.lastTrtTime = lastTrtTime;
	}

}
