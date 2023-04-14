package dynamicAnalysis;

/**
 * Information relating to the sent packet. Contains information that can be used to determine what the packet contains.
 */
public class ActiveConnection
{
	
	/** Establishes whether a packet is TCP or UDP. */
	private String protocol;
	
	/** The address that sent the packet. */
	private String localAddress;
	
	/** The foreign address of the packet. */
	private String foreignAddress;
	
	/** The state that the packet is in. */
	private String state;
	
	/** The associated process ID of the backet. */
	private long pid;
	
	/**
	 * Instantiates a new active connection.
	 *
	 * @param protocol the protocol of the packet
	 * @param localAddress the local address of the packet
	 * @param foreignAddress the foreign address of the packet
	 * @param state the state of the packet
	 * @param pid the pid that the packet was sent from
	 */
	public ActiveConnection(String protocol, String localAddress, String foreignAddress, String state, long pid)
	{
		setProtocol(protocol);
		setLocalAddress(localAddress);
		setForeignAddress(foreignAddress);
		setState(state);
		setPid(pid);
	}
	
	/**
	 * Gets the protocol.
	 *
	 * @return the protocol
	 */
	public String getProtocol()
	{
		return protocol;
	}
	
	/**
	 * Sets the protocol.
	 *
	 * @param protocol the new protocol
	 */
	public void setProtocol(String protocol)
	{
		this.protocol = protocol;
	}
	
	/**
	 * Gets the local address.
	 *
	 * @return the local address
	 */
	public String getLocalAddress()
	{
		return localAddress;
	}
	
	/**
	 * Sets the local address.
	 *
	 * @param localAddress the new local address
	 */
	public void setLocalAddress(String localAddress)
	{
		this.localAddress = localAddress;
	}
	
	/**
	 * Gets the foreign address.
	 *
	 * @return the foreign address
	 */
	public String getForeignAddress()
	{
		return foreignAddress;
	}
	
	/**
	 * Sets the foreign address.
	 *
	 * @param foreignAddress the new foreign address
	 */
	public void setForeignAddress(String foreignAddress)
	{
		this.foreignAddress = foreignAddress;
	}
	
	/**
	 * Gets the state.
	 *
	 * @return the state
	 */
	public String getState()
	{
		return state;
	}
	
	/**
	 * Sets the state.
	 *
	 * @param state the new state
	 */
	public void setState(String state)
	{
		this.state = state;
	}
	
	/**
	 * Gets the pid.
	 *
	 * @return the pid
	 */
	public long getPid()
	{
		return pid;
	}
	
	/**
	 * Sets the pid.
	 *
	 * @param pid the new pid
	 */
	public void setPid(long pid)
	{
		this.pid = pid;
	}
	
	/**
	 * To string.
	 *
	 * @return the full information of the packet
	 */
	@Override
	public String toString()
	{
		return "ActiveConnection [protocol=" + protocol + ", localAddress=" + localAddress + ", foreignAddress="
				+ foreignAddress + ", state=" + state + ", pid=" + pid + "]";
	}
	
}
