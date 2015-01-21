package net.younguard.bighorn.broadcast.session;

import java.util.HashMap;

public interface SessionService
{
	public void online(String deviceId, String notifyToken, String username, long ioSessionId);

	public void offline(String deviceId);

	public boolean isOnline(String deviceId);

	public HashMap<String, SessionObject> getSessionMap();
}
