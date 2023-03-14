package dynamicAnalysis;

public class DataDirectory
{
	private byte[] bytes;
	private int virtualAddress;
	private int size;
	
	public DataDirectory(byte[] bytes, int virtualAddress, int size)
	{
		setBytes(bytes);
		setVirtualAddress(virtualAddress);
		setSize(size);
	}
	
	public byte[] getBytes()
	{
		return bytes;
	}

	public void setBytes(byte[] bytes)
	{
		this.bytes = bytes;
	}

	public int getVirtualAddress()
	{
		return virtualAddress;
	}
	
	public void setVirtualAddress(int virtualAddress)
	{
		this.virtualAddress = virtualAddress;
	}
	
	public int getSize()
	{
		return size;
	}
	
	public void setSize(int size)
	{
		this.size = size;
	}
	
	
}
