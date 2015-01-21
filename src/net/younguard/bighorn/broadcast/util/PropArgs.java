package net.younguard.bighorn.broadcast.util;

/**
 * Global arguments
 * 
 * Copyright 2014-2015 by Young Guard Salon Community, China. All rights
 * reserved. http://www.younguard.net
 * 
 * NOTICE ! You can copy or redistribute this code freely, but you should not
 * remove the information about the copyright notice and the author.
 * 
 * @author ThomasZhang, thomas.zh@qq.com
 */
public class PropArgs
{
	public static String STP_VERSION = PropUtil.getString("config", "stp.version");
	public static int STP_PORT = PropUtil.getInt("config", "stp.port");
	public static int BUFFER_SIZE = PropUtil.getInt("config", "buffer.size");

}
