package net.younguard.bighorn.broadcast.util;

import net.younguard.bighorn.broadcast.service.AccountService;
import net.younguard.bighorn.broadcast.service.DeviceService;
import net.younguard.bighorn.broadcast.service.GameService;
import net.younguard.bighorn.broadcast.service.PlayerService;
import net.younguard.bighorn.broadcast.service.SessionService;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

public class BighornApplicationContextUtil
{
	private static ApplicationContext context;

	public static void setApplicationContext(ApplicationContext contex)
			throws BeansException
	{
		context = contex;
	}

	public static ApplicationContext getContext()
	{
		return context;
	}

	public static SessionService getSessionService()
	{
		return (SessionService) context.getBean("broadcastSessionService");
	}

	public static DeviceService getDeviceService()
	{
		return (DeviceService) context.getBean("broadcastDeviceService");
	}

	public static AccountService getAccountService()
	{
		return (AccountService) context.getBean("broadcastAccountService");
	}

	public static GameService getGameService()
	{
		return (GameService) context.getBean("broadcastGameService");
	}

	public static PlayerService getPlayerService()
	{
		return (PlayerService) context.getBean("broadcastPlayerService");
	}

}
