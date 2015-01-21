package net.younguard.bighorn.broadcast.session;

import java.util.HashMap;

/**
 * use hashmap implements session service interface.
 * 
 * Copyright 2014-2015 by Young Guard Salon Community, China. All rights reserved.
 * http://www.younguard.net
 * 
 * NOTICE ! You can copy or redistribute this code freely, but you should not
 * remove the information about the copyright notice and the author.
 * 
 * @author ThomasZhang, thomas.zh@qq.com
 */
public class SessionMap
		implements SessionService
{
	@Override
	public void online(String deviceId, String notifyToken, String username, long ioSessionId)
	{
		SessionObject so = new SessionObject(deviceId, notifyToken, username, true, ioSessionId);
		deviceMap.put(deviceId, so);
	}

	@Override
	public void offline(String deviceId)
	{
		SessionObject so = deviceMap.get(deviceId);
		if (so != null) {
			so.setOnline(false);
			deviceMap.put(deviceId, so);
		}
	}

	@Override
	public boolean isOnline(String deviceId)
	{
		SessionObject so = deviceMap.get(deviceId);
		if (so == null) {
			return false;
		} else {
			return so.isOnline();
		}
	}

	@Override
	public HashMap<String, SessionObject> getSessionMap()
	{
		return deviceMap;
	}

	// //////////////////////////////////////////////////////

	private HashMap<String, SessionObject> deviceMap = new HashMap<String, SessionObject>();

}
