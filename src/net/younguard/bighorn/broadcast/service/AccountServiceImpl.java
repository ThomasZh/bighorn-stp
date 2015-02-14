package net.younguard.bighorn.broadcast.service;

import java.util.UUID;

import net.younguard.bighorn.broadcast.dao.AccountDao;
import net.younguard.bighorn.broadcast.domain.AccountBaseInfo;

public class AccountServiceImpl
		implements AccountService
{
	@Override
	public boolean isExist(String accountId)
	{
		return accountDao.isExist(accountId);
	}

	@Override
	public String create(String nickname, String avatarUrl, int timestamp)
	{
		String accountId = UUID.randomUUID().toString();

		accountDao.add(accountId, nickname, avatarUrl, timestamp);

		return accountId;
	}

	@Override
	public void modify(String accountId, String nickname, String avatarUrl, int timestamp)
	{
		accountDao.update(accountId, nickname, avatarUrl, timestamp);
	}

	@Override
	public AccountBaseInfo query(String accountId)
	{
		return accountDao.select(accountId);
	}

	// ////////////////////////////////////////////////////////////

	private AccountDao accountDao;

	public AccountDao getAccountDao()
	{
		return accountDao;
	}

	public void setAccountDao(AccountDao accountDao)
	{
		this.accountDao = accountDao;
	}

}
