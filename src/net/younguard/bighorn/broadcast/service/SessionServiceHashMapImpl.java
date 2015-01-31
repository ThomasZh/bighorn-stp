package net.younguard.bighorn.broadcast.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.younguard.bighorn.broadcast.domain.SessionObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	public void online(String deviceId, String notifyToken, String username, long ioSessionId, int timestamp)
	{
		if (deviceId != null) {
			SessionObject so = new SessionObject(deviceId, notifyToken, username, true, ioSessionId, timestamp);
			deviceMap.put(deviceId, so);

			increase();
		}
	}

	@Override
	public void init(String deviceId, String notifyToken, String username, int timestamp)
	{
		long ioSessionId = 0;// no session

		SessionObject so = new SessionObject(deviceId, notifyToken, username, false, ioSessionId, timestamp);
		deviceMap.put(deviceId, so);
	}

	@Override
	public void offline(String deviceId, int timestamp)
	{
		if (deviceId != null) {
			SessionObject so = deviceMap.get(deviceId);
			if (so != null) {
				so.setOnline(false);
				so.setLastTrtTime(timestamp);
				deviceMap.put(deviceId, so);
			}

			decrease();
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
		int i = 0;

		for (Map.Entry<String, SessionObject> it : deviceMap.entrySet()) {
			SessionObject so = it.getValue();

			if (so.isOnline()) {
				logger.debug(so.getUsername() + "," + so.getDeviceId() + "," + so.getNotifyToken() + ","
						+ so.getIoSessionId());
				i++;
			}
		}

		return i;
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

	private final static Logger logger = LoggerFactory.getLogger(SessionServiceHashMapImpl.class);

}
