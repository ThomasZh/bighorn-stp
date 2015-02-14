package net.younguard.bighorn.broadcast.domain;

import java.io.Serializable;

public class AccountBaseInfo
		implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6584206417937297198L;
	private String accountId;
	private String nickname;
	private String avatarUrl;

	public String getAccountId()
	{
		return accountId;
	}

	public void setAccountId(String accountId)
	{
		this.accountId = accountId;
	}

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

}
