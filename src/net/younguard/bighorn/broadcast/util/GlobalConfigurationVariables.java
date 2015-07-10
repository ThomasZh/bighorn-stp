package net.younguard.bighorn.broadcast.util;

public class GlobalConfigurationVariables
{
	private String stpVersion;
	private int stpPort;
	private int bufferSize;

	public String getStpVersion()
	{
		return stpVersion;
	}

	public void setStpVersion(String stpVersion)
	{
		this.stpVersion = stpVersion;
	}

	public int getStpPort()
	{
		return stpPort;
	}

	public void setStpPort(int stpPort)
	{
		this.stpPort = stpPort;
	}

	public int getBufferSize()
	{
		return bufferSize;
	}

	public void setBufferSize(int bufferSize)
	{
		this.bufferSize = bufferSize;
	}

}
