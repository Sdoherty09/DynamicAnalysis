/*
 * 
 */
package dynamicAnalysis;

import java.io.File;

/**
 * The Class VirtualMemory.
 */
public class VirtualMemory
{
	
	/** The process id. */
	private int processId;

	static {System.load(getFile("memory.dll"));}
	
	/**
	 * Scan process.
	 *
	 * @param processId the process id
	 * @return the byte[]
	 */
	private native byte[] scanProcess(int processId);
	
	/**
	 * Instantiates a new virtual memory.
	 *
	 * @param processId the process id
	 */
	public VirtualMemory(int processId)
	{
		setProcessId(processId);
	}

	/**
	 * Gets the process id.
	 *
	 * @return the process id
	 */
	public int getProcessId()
	{
		return processId;
	}

	/**
	 * Sets the process id.
	 *
	 * @param processId the new process id
	 */
	public void setProcessId(int processId)
	{
		this.processId = processId;
	}
	
	/**
	 * Gets the file.
	 *
	 * @param fileName the file name
	 * @return the file
	 */
	private static String getFile(String fileName)
	{
		/*File file = new File(".\\"+fileName);
		return file.getAbsolutePath();*/ //JAR release
		return System.getProperty("user.dir")+"\\src\\dynamicAnalysis\\"+fileName;
	}
	
	/**
	 * Read memory.
	 *
	 * @return the byte[]
	 */
	public byte[] readMemory()
	{
		VirtualMemory virtualMemory = new VirtualMemory(getProcessId());
		return virtualMemory.scanProcess(getProcessId());
	}
}

//javac -h . VirtualMemory.java
//g++ -I I:/jre/jre6/include -I I:/jre/jre6/include/win32 -shared -o memory.dll VirtualMemory.cpp