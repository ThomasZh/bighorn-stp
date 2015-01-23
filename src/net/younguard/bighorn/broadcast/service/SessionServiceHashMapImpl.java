package net.younguard.bighorn.broadcast.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.younguard.bighorn.broadcast.domain.SessionObject;

/**
 * use hashmap implements session service interface.
 * 
 * Copyright 2014-2015 by Young Guard Salon Community, China. All rights
 * reserved. http://www.younguard.net
 * 
 * NOTICE ! You can copy or redistribute this code freely, but you should not
 * remove the information about the copyright notice and the author.
 * 
 * @author ThomasZhang, thomas.zh@qq.com
 */
public class SessionServiceHashMapImpl
		implements SessionService
{
	@Override
	public void online(String deviceId, String notifyToken, String username, long ioSessionId)
	{
		SessionObject so = new SessionObject(deviceId, notifyToken, username, true, ioSessionId);
		deviceMap.put(deviceId, so);

		increase();
	}

	@Override
	public void offline(String deviceId)
	{
		SessionObject so = deviceMap.get(deviceId);
		if (so != null) {
			so.setOnline(false);
			deviceMap.put(deviceId, so);
		}

		if (deviceId != null)
			decrease();
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

	// @Override
	public List<SessionObject> getOnlineSessions()
	{
		List<SessionObject> sessions = new ArrayList<SessionObject>();

		for (Map.Entry<String, SessionObject> it : deviceMap.entrySet()) {
			SessionObject so = it.getValue();

			if (so.isOnline())
				sessions.add(so);
		}

		return sessions;
	}

	@Override
	public SessionObject get(String deviceId)
	{
		return deviceMap.get(deviceId);
	}

	@Override
	public int getOnlineNum()
	{
		// int num = 0;
		//
		// for (Map.Entry<String, SessionObject> it : deviceMap.entrySet()) {
		// SessionObject so = it.getValue();
		//
		// if (so.isOnline())
		// num++;
		// }

		return num;
	}

	// //////////////////////////////////////////////////////

	private void increase()
	{
		num++;
	}

	private void decrease()
	{
		num--;
		if (num < 0)
			num = 0;
	}

	private HashMap<String, SessionObject> deviceMap = new HashMap<String, SessionObject>();
	private int num = 0;

}
