package net.younguard.bighorn.broadcast.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.younguard.bighorn.broadcast.domain.SessionAccountObject;
import net.younguard.bighorn.broadcast.domain.SessionDeviceObject;

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
	public void online(String accountId, String deviceId, String notifyToken, String osVersion, long ioSessionId,
			int timestamp)
	{
		if (deviceId != null) {
			SessionDeviceObject so = new SessionDeviceObject(osVersion, notifyToken, true, timestamp, ioSessionId,
					accountId);
			deviceMap.put(deviceId, so);

			increase();
		}
	}

	@Override
	public void init(String deviceId, String notifyToken, String osVersion, int timestamp)
	{
		long ioSessionId = 0;// no session

		SessionDeviceObject so = new SessionDeviceObject(osVersion, notifyToken, false, timestamp, ioSessionId);
		deviceMap.put(deviceId, so);
	}

	@Override
	public void offline(String deviceId, int timestamp)
	{
		if (deviceId != null) {
			SessionDeviceObject so = deviceMap.get(deviceId);
			if (so != null) {
				so.setOnline(false);
				so.setLastTryTime(timestamp);
				deviceMap.put(deviceId, so);
			}

			decrease();
		}
	}

	@Override
	public boolean isOnline(String deviceId)
	{
		SessionDeviceObject so = deviceMap.get(deviceId);
		if (so == null) {
			return false;
		} else {
			return so.isOnline();
		}
	}

	@Override
	public HashMap<String, SessionDeviceObject> getSessionMap()
	{
		return deviceMap;
	}

	// @Override
	public List<SessionDeviceObject> getOnlineSessions()
	{
		List<SessionDeviceObject> sessions = new ArrayList<SessionDeviceObject>();

		for (Map.Entry<String, SessionDeviceObject> it : deviceMap.entrySet()) {
			SessionDeviceObject so = it.getValue();

			if (so.isOnline())
				sessions.add(so);
		}

		return sessions;
	}

	@Override
	public SessionDeviceObject getDevice(String deviceId)
	{
		return deviceMap.get(deviceId);
	}

	@Override
	public int getOnlineNum()
	{
		int i = 0;

		for (Map.Entry<String, SessionDeviceObject> it : deviceMap.entrySet()) {
			SessionDeviceObject so = it.getValue();

			if (so.isOnline()) {
				logger.debug(so.getOsVersion() + "," + so.getNotifyToken() + "," + so.getIoSessionId());
				i++;
			}
		}

		return i;
	}

	// //////////////////////////////////////////////////////
	// account bind device

	@Override
	public void register(String accountId, String deviceId, String nickname, String avatarUrl)
	{
		SessionAccountObject sao = new SessionAccountObject();
		sao.setNickname(nickname);
		sao.setAvatarUrl(avatarUrl);
		sao.setDeviceId(deviceId);

		accountDeviceMap.put(accountId, sao);
	}

	@Override
	public SessionAccountObject getAccount(String accountId)
	{
		return accountDeviceMap.get(accountId);
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

	private HashMap<String, SessionDeviceObject> deviceMap = new HashMap<String, SessionDeviceObject>();
	private HashMap<String, SessionAccountObject> accountDeviceMap = new HashMap<String, SessionAccountObject>();
	private int num = 0;

	private final static Logger logger = LoggerFactory.getLogger(SessionServiceHashMapImpl.class);

}
