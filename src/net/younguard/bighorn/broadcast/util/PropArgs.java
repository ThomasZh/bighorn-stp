package net.younguard.bighorn.broadcast.util;

public class PropArgs
{
	public static String STP_VERSION = PropUtil.getString("config", "stp.version");
	public static int STP_PORT = PropUtil.getInt("config", "stp.port");
	public static int BUFFER_SIZE = PropUtil.getInt("config", "buffer.size");

}
