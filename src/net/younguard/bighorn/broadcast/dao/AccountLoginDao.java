package net.younguard.bighorn.broadcast.dao;

public interface AccountLoginDao
{
	public boolean isExist(short loginType, String loginName);

	public void add(short loginType, String loginName, String accountId, int timestamp);

	public void add(short loginType, String loginName, String accountId, String ecryptPwd, String salt, int timestamp);

	/**
	 * @param loginType
	 *            :name, deivceId
	 * @return salt
	 */
	public String selectSalt(short loginType, String loginName);

	/**
	 * @param loginType
	 *            :name, deivceId
	 * @return accountId
	 */
	public String select(short loginType, String loginName);

	/**
	 * @param loginType
	 *            :name, deivceId
	 * @return accountId
	 */
	public String select(short loginType, String loginName, String ecryptPwd);
}
