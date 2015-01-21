package net.younguard.bighorn.broadcast.session;

import java.util.HashMap;

/**
 * session service interface.
 * 
 * Copyright 2014-2015 by Young Guard Salon Community, China. All rights reserved.
 * http://www.younguard.net
 * 
 * NOTICE ! You can copy or redistribute this code freely, but you should not
 * remove the information about the copyright notice and the author.
 * 
 * @author ThomasZhang, thomas.zh@qq.com
 */
public interface SessionService
{
	public void online(String deviceId, String notifyToken, String username, long ioSessionId);

	public void offline(String deviceId);

	public boolean isOnline(String deviceId);

	public HashMap<String, SessionObject> getSessionMap();
}
