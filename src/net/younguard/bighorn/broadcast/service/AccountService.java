package net.younguard.bighorn.broadcast.service;

import net.younguard.bighorn.domain.AccountBaseInfo;

public interface AccountService
{
	public boolean isExist(String accountId);

	public void create(String accountId, String nickname, String avatarUrl, int timestamp);

	public void modify(String accountId, String nickname, String avatarUrl, int timestamp);

	public AccountBaseInfo query(String accountId);

	// ///////////////////////////////////////////////////

	/**
	 * 
	 * @param loginType
	 *            :name, deivceId
	 */
	public boolean isExistLogin(short loginType, String loginName);

	public boolean login(short loginType, String loginName, String md5Pwd);

	/**
	 * query account base info by loginname
	 * 
	 * @param loginType
	 *            :name, deivceId
	 */
	public AccountBaseInfo queryAccount(short loginType, String loginName);

	/**
	 * create a new login & account
	 * 
	 * @param loginType
	 *            :name, deivceId
	 * @return accountId
	 */
	public String createLogin(short loginType, String loginName, int timestamp);

	/**
	 * create a new login & account
	 * 
	 * @param loginType
	 *            :name, deivceId
	 * @return accountId
	 */
	public String createLogin(short loginType, String loginName, String md5Pwd, int timestamp);

}
