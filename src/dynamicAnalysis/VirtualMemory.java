package dynamicAnalysis;

public class VirtualMemory
{
	private int processId;

	static {System.loadLibrary("memory");}
	
	private native char[] scanProcess(int processId);
	
	public VirtualMemory(int processId)
	{
		setProcessId(processId);
	}

	public int getProcessId()
	{
		return processId;
	}

	public void setProcessId(int processId)
	{
		this.processId = processId;
	}
	
	private static String getFile(String fileName)
	{
		return System.getProperty("user.dir")+"\\src\\dynamicAnalysis\\"+fileName;
	}
	
	public char[] readMemory()
	{
		VirtualMemory virtualMemory = new VirtualMemory(getProcessId());
		return virtualMemory.scanProcess(getProcessId());
	}
}

//javac -h . VirtualMemory.java
//g++ -I I:/jre/jre6/include -I I:/jre/jre6/include/win32 -shared -o memory.dll VirtualMemory.cpp