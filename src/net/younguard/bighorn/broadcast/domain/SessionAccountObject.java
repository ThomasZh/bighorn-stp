package net.younguard.bighorn.broadcast.domain;

import java.io.Serializable;

public class SessionAccountObject
		implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5310552352527189196L;
	private String nickname;
	private String avatarUrl;
	private String deviceId;

	public String getNickname()
	{
		return nickname;
	}

	public void setNickname(String nickname)
	{
		this.nickname = nickname;
	}

	public String getAvatarUrl()
	{
		return avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl)
	{
		this.avatarUrl = avatarUrl;
	}

	public String getDeviceId()
	{
		return deviceId;
	}

	public void setDeviceId(String deviceId)
	{
		this.deviceId = deviceId;
	}

}
