package net.younguard.bighorn.broadcast.session;

/**
 * store this object(deviceId,notifyToken,username,isOnline,minaIoSessionId) into session map.
 * 
 * Copyright 2014-2015 by Young Guard Salon Community, China. All rights reserved.
 * http://www.younguard.net
 * 
 * NOTICE ! You can copy or redistribute this code freely, but you should not
 * remove the information about the copyright notice and the author.
 * 
 * @author ThomasZhang, thomas.zh@qq.com
 */
public class SessionObject
{
	public SessionObject(String deviceId, String notifyToken, String username, boolean online, long ioSessionId)
	{
		this.setDeviceId(deviceId);
		this.setNotifyToken(notifyToken);
		this.setUsername(username);
		this.setOnline(online);
		this.setIoSessionId(ioSessionId);
	}

	private String deviceId;
	private String notifyToken;
	private String username;
	private boolean online;
	private long ioSessionId;

	public String getDeviceId()
	{
		return deviceId;
	}

	public void setDeviceId(String deviceId)
	{
		this.deviceId = deviceId;
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
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

	public long getIoSessionId()
	{
		return ioSessionId;
	}

	public void setIoSessionId(long ioSessionId)
	{
		this.ioSessionId = ioSessionId;
	}

}
