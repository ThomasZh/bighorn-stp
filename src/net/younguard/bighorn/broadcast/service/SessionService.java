package net.younguard.bighorn.broadcast.service;

import java.util.HashMap;

import net.younguard.bighorn.broadcast.domain.SessionObject;

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
	public void online(String deviceId, String notifyToken, String username, long ioSessionId, int timestamp);
	
	/**
	 * load form database, then init memory cache.
	 * those user's state is not online, and no ioSessionId in mina framework.
	 */
	public void init(String deviceId, String notifyToken, String username, int timestamp);

	public void offline(String deviceId, int timestamp);

	public boolean isOnline(String deviceId);

	public HashMap<String, SessionObject> getSessionMap();

	// public List<SessionObject> getOnlineSessions();

	public SessionObject get(String deviceId);

	public int getOnlineNum();
}
