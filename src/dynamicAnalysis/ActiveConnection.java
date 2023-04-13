package dynamicAnalysis;

public class ActiveConnection
{
	private String protocol;
	private String localAddress;
	private String foreignAddress;
	private String state;
	private long pid;
	
	public ActiveConnection(String protocol, String localAddress, String foreignAddress, String state, long pid)
	{
		setProtocol(protocol);
		setLocalAddress(localAddress);
		setForeignAddress(foreignAddress);
		setState(state);
		setPid(pid);
	}
	public String getProtocol()
	{
		return protocol;
	}
	public void setProtocol(String protocol)
	{
		this.protocol = protocol;
	}
	public String getLocalAddress()
	{
		return localAddress;
	}
	public void setLocalAddress(String localAddress)
	{
		this.localAddress = localAddress;
	}
	public String getForeignAddress()
	{
		return foreignAddress;
	}
	public void setForeignAddress(String foreignAddress)
	{
		this.foreignAddress = foreignAddress;
	}
	public String getState()
	{
		return state;
	}
	public void setState(String state)
	{
		this.state = state;
	}
	public long getPid()
	{
		return pid;
	}
	public void setPid(long pid)
	{
		this.pid = pid;
	}
	@Override
	public String toString()
	{
		return "ActiveConnection [protocol=" + protocol + ", localAddress=" + localAddress + ", foreignAddress="
				+ foreignAddress + ", state=" + state + ", pid=" + pid + "]";
	}
	
}
