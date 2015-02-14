package net.younguard.bighorn.broadcast.service;

import net.younguard.bighorn.broadcast.domain.AccountBaseInfo;

public interface AccountService
{
	public boolean isExist(String accountId);

	public String create(String nickname, String avatarUrl, int timestamp);

	public void modify(String accountId, String nickname, String avatarUrl, int timestamp);

	public AccountBaseInfo query(String accountId);
}
