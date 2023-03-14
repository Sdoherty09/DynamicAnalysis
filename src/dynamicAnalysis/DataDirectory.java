package dynamicAnalysis;

public class DataDirectory
{
	private int virtualAddress;
	private int size;
	
	public DataDirectory(int virtualAddress, int size)
	{
		setVirtualAddress(virtualAddress);
		setSize(size);
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
