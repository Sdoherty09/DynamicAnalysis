/*
 * 
 */
package dynamicAnalysis;

/**
 * The Class DataDirectory.
 */
public class DataDirectory
{
	
	/** The bytes. */
	private byte[] bytes;
	
	/** The virtual address. */
	private int virtualAddress;
	
	/** The size. */
	private int size;
	
	/**
	 * Instantiates a new data directory.
	 *
	 * @param bytes the bytes
	 * @param virtualAddress the virtual address
	 * @param size the size
	 */
	public DataDirectory(byte[] bytes, int virtualAddress, int size)
	{
		setBytes(bytes);
		setVirtualAddress(virtualAddress);
		setSize(size);
	}
	
	/**
	 * Gets the bytes.
	 *
	 * @return the bytes
	 */
	public byte[] getBytes()
	{
		return bytes;
	}

	/**
	 * Sets the bytes.
	 *
	 * @param bytes the new bytes
	 */
	public void setBytes(byte[] bytes)
	{
		this.bytes = bytes;
	}

	/**
	 * Gets the virtual address.
	 *
	 * @return the virtual address
	 */
	public int getVirtualAddress()
	{
		return virtualAddress;
	}
	
	/**
	 * Sets the virtual address.
	 *
	 * @param virtualAddress the new virtual address
	 */
	public void setVirtualAddress(int virtualAddress)
	{
		this.virtualAddress = virtualAddress;
	}
	
	/**
	 * Gets the size.
	 *
	 * @return the size
	 */
	public int getSize()
	{
		return size;
	}
	
	/**
	 * Sets the size.
	 *
	 * @param size the new size
	 */
	public void setSize(int size)
	{
		this.size = size;
	}
	
	
}
