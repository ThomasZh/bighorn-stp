package net.younguard.bighorn.broadcast.domain;

public class DeviceDetailInfo
		extends DeviceMasterInfo
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5702451490810954439L;
	private int lastTryTime;

	public int getLastTryTime()
	{
		return lastTryTime;
	}

	public void setLastTryTime(int lastTryTime)
	{
		this.lastTryTime = lastTryTime;
	}

}
