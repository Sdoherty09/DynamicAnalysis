/*
 * 
 */
package dynamicAnalysis;

/**
 * Top level loader to call VirtualMemory.cpp through Java Native Interface. Retrieves the virtual memory from a given process ID.
 */
public class VirtualMemory
{
	
	/** The process id to read the virtual memory from. */
	private int processId;

	static {System.load(getFile("memory.dll"));}
	
	/**
	 * Scan the virtual memory of a process through JNI.
	 *
	 * @param processId the process id to retrieve the virtual memory from
	 * @return the byte array containing the virtual memory space
	 */
	private native byte[] scanProcess(int processId);
	
	/**
	 * Instantiates a new virtual memory with the process ID.
	 *
	 * @param processId the process ID to retrieve the virtual memory space from
	 */
	public VirtualMemory(int processId)
	{
		setProcessId(processId);
	}

	/**
	 * Gets the unique identifier from the selected process.
	 *
	 * @return the unique identifier of the process
	 */
	public int getProcessId()
	{
		return processId;
	}

	/**
	 * Sets the unique identifier for the process.
	 *
	 * @param processId the new unique identifier for the selected process.
	 */
	public void setProcessId(int processId)
	{
		this.processId = processId;
	}
	
	/**
	 * Gets the full path of the file.
	 *
	 * @param fileName the file name
	 * @return the file path
	 */
	private static String getFile(String fileName)
	{
		/*File file = new File(".\\"+fileName);
		return file.getAbsolutePath();*/ //JAR release
		return System.getProperty("user.dir")+"\\src\\dynamicAnalysis\\"+fileName;
	}
	
	/**
	 * Calls the C++ function to read the virtual memory space, given a process ID.
	 *
	 * @return the full virtual memory space of the process
	 */
	public byte[] readMemory()
	{
		VirtualMemory virtualMemory = new VirtualMemory(getProcessId());
		return virtualMemory.scanProcess(getProcessId());
	}
}

//javac -h . VirtualMemory.java
//g++ -I I:/jre/jre6/include -I I:/jre/jre6/include/win32 -shared -o memory.dll VirtualMemory.cpp