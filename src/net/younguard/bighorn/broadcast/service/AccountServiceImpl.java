package net.younguard.bighorn.broadcast.service;

import java.util.UUID;

import net.younguard.bighorn.broadcast.dao.AccountDao;
import net.younguard.bighorn.broadcast.dao.AccountLoginDao;
import net.younguard.bighorn.comm.util.EcryptUtil;
import net.younguard.bighorn.domain.AccountBaseInfo;

public class AccountServiceImpl
		implements AccountService
{
	@Override
	public boolean isExist(String accountId)
	{
		return accountDao.isExist(accountId);
	}

	@Override
	public void create(String accountId, String nickname, String avatarUrl, int timestamp)
	{
		accountDao.add(accountId, nickname, avatarUrl, timestamp);
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

	@Override
	public boolean isExistLogin(short loginType, String loginName)
	{
		return accountLoginDao.isExist(loginType, loginName);
	}

	@Override
	public AccountBaseInfo queryAccount(short loginType, String loginName)
	{
		String accountId = accountLoginDao.select(loginType, loginName);
		return accountDao.select(accountId);
	}

	@Override
	public String createLogin(short loginType, String loginName, int timestamp)
	{
		String accountId = UUID.randomUUID().toString();
		accountLoginDao.add(loginType, loginName, accountId, timestamp);
		return accountId;
	}

	@Override
	public String createLogin(short loginType, String loginName, String md5Pwd, int timestamp)
	{
		String accountId = UUID.randomUUID().toString();
		String salt = EcryptUtil.salt();
		String ecryptPwd = EcryptUtil.md5(md5Pwd + EcryptUtil.md5(salt));
		accountLoginDao.add(loginType, loginName, accountId, ecryptPwd, salt, timestamp);
		return accountId;
	}

	@Override
	public boolean login(short loginType, String loginName, String md5Pwd)
	{
		if (accountLoginDao.isExist(loginType, loginName)) {
			String salt = accountLoginDao.selectSalt(loginType, loginName);
			String ecryptPwd = null;
			if (salt != null && salt.length() > 0) {
				ecryptPwd = EcryptUtil.md5(md5Pwd + EcryptUtil.md5(salt));
			} else {
				ecryptPwd = md5Pwd;
			}

			String accountId = accountLoginDao.select(loginType, loginName, ecryptPwd);
			if (accountId != null && accountId.length() > 0) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	// ////////////////////////////////////////////////////////////

	private AccountDao accountDao;
	private AccountLoginDao accountLoginDao;

	public AccountDao getAccountDao()
	{
		return accountDao;
	}

	public void setAccountDao(AccountDao accountDao)
	{
		this.accountDao = accountDao;
	}

	public AccountLoginDao getAccountLoginDao()
	{
		return accountLoginDao;
	}

	public void setAccountLoginDao(AccountLoginDao accountLoginDao)
	{
		this.accountLoginDao = accountLoginDao;
	}

}
