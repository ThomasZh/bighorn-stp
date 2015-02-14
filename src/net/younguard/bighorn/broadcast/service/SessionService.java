package net.younguard.bighorn.broadcast.service;

import java.util.HashMap;

import net.younguard.bighorn.broadcast.domain.SessionAccountObject;
import net.younguard.bighorn.broadcast.domain.SessionDeviceObject;

/**
 * session service interface.
 * 
 * Copyright 2014-2015 by Young Guard Salon Community, China. All rights
 * reserved. http://www.younguard.net
 * 
 * NOTICE ! You can copy or redistribute this code freely, but you should not
 * remove the information about the copyright notice and the author.
 * 
 * @author ThomasZhang, thomas.zh@qq.com
 */
public interface SessionService
{
	public void online(String accountId, String deviceId, String osVersion, String notifyToken, long ioSessionId,
			int timestamp);

	public SessionDeviceObject getDevice(String deviceId);

	public void offline(String deviceId, int timestamp);

	public void register(String accountId, String deviceId, String nickname, String avatarUrl);

	public SessionAccountObject getAccount(String accountId);

	public boolean isOnline(String deviceId);

	/**
	 * load form database, then init memory cache. those user's state is not
	 * online, and no ioSessionId in mina framework.
	 */
	public void init(String deviceId, String notifyToken, String username, int timestamp);

	public HashMap<String, SessionDeviceObject> getSessionMap();

	// public List<SessionDeviceObject> getOnlineSessions();

	public int getOnlineNum();
}
