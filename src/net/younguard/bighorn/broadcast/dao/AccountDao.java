package net.younguard.bighorn.broadcast.dao;

import net.younguard.bighorn.domain.AccountBaseInfo;

public interface AccountDao
{
	public boolean isExist(String accountId);

	public void add(String accountId, String nickname, String avatarUrl, int timestamp);

	public void update(String accountId, String nickname, String avatarUrl, int timestamp);

	public AccountBaseInfo select(String accountId);
}
