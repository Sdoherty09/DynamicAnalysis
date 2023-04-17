/*
 * 
 */
package dynamicAnalysis;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * The Class ProcessManager. Manages the created and accessed processes.
 */
public class ProcessManager
{
	
	/** The file that is accessed. */
	private File file;
	
	/** The process. */
	private Process process;
	
	/** The command line utility to send commands to. */
	private CommandLine commandLine;
	
	/** The name of the process. */
	private String name;
	
	/** The string array representation of DLLs accessed by the process. */
	private String[] dlls;
	
	/** The list of DLLs accessed by the process. */
	private DllFile[] dllFiles;
	
	/** The list of files accessed by the process. */
	private String[] files;
	
	/**
	 * Instantiates a new manager for processes when using a file.
	 *
	 * @param file the file to be accessed
	 */
	public ProcessManager(File file)
	{
		setFile(file);
		createProcess();
		commandLine = new CommandLine(getPid());
		setName();
		setDLLs();
	}

	/**
	 * Instantiates a new manager for processes when using an already running process.
	 *
	 * @param pid the unique identifier for the process
	 */
	public ProcessManager(int pid)
	{
		commandLine = new CommandLine(pid);
		setName();
		setDLLs();
		setFiles();
	}
	
	/**
	 * Gets the file being accessed.
	 *
	 * @return the file being accessed.
	 */
	public File getFile()
	{
		return file;
	}

	/**
	 * Sets the file being accessed.
	 *
	 * @param file the new file
	 */
	public void setFile(File file)
	{
		this.file = file;
	}
	
	/**
	 * Creates a new process.
	 *
	 * @return the process
	 */
	public Process createProcess()
	{
		ProcessBuilder builder = new ProcessBuilder(getFile().getAbsolutePath());
		process = null;
		try
		{
			process = builder.start();
		} catch (IOException e)
		{
			e.printStackTrace();
			
		}
		System.out.println("children: "+process.children());
		System.out.println("info: " + process.info());
		return process;
	}
	
	/**
	 * Gets the unique identifier of the process in use.
	 *
	 * @return the unique identifier of the process in use
	 */
	public long getPid()
	{
		return process.pid();
	}
	
	/**
	 * Gets the unique identifier of the process in use as a string.
	 *
	 * @return the unique identifier of the process in use as a string
	 */
	public String getPidAsString()
	{
		return Long.toString(process.pid());
	}
	
	/**
	 * Sets the name of the process. Retrieved from the command line.
	 */
	private void setName()
	{
		String name = commandLine.runName();
		try
		{
			this.name = name.substring(name.lastIndexOf("\"", name.indexOf(".exe"))+1, name.lastIndexOf("\"", name.indexOf(".exe")+4));
		}
		catch(StringIndexOutOfBoundsException e)
		{
			e.printStackTrace();
			this.name = "Error";
		}
	}
	
	/**
	 * Sets the DLLs used by the process. Retrieved from the command line.
	 */
	private void setDLLs()
	{
		String dllString = commandLine.runDLLs();
		int count=0;
		for(int index=dllString.indexOf('\n', dllString.indexOf("Path")+5)+1;index<dllString.length();index++)
		{
			if(dllString.charAt(index)=='\n')
			{
				count++;
			}
		}
		String DLLs[] = new String[count];
		int index=dllString.indexOf('\n', dllString.indexOf("Path")+5)+1;
		int dllIndex=0;		
		while(count!=1)
		{
			try
			{
				DLLs[dllIndex]=dllString.substring(dllString.lastIndexOf(' ', dllString.indexOf('\n', index)), dllString.lastIndexOf('\n', dllString.indexOf('\n', index)));
			} catch(StringIndexOutOfBoundsException | IllegalArgumentException e)
			{
				DLLs[dllIndex] = "";
				e.printStackTrace();
			}
			index=dllString.indexOf('\n', index+1);
			dllIndex++;
			count--;
		}
		DLLs[dllIndex]=dllString.substring(dllString.lastIndexOf(' '));
		dllFiles = new DllFile[DLLs.length];
		for(int j = 0;j<DLLs.length;j++)
		{
			dllFiles[j] = new DllFile(DLLs[j]);
		}
		this.dlls = DLLs;
	}
	
	/**
	 * Gets the name of the process.
	 *
	 * @return the name of the process
	 */
	public String getName()
	{
		return this.name;
	}
	
	/**
	 * Gets the string array representation of DLLs in use by the process.
	 *
	 * @return the string array representation of DLLs in use by the process
	 */
	public String[] getDLLs()
	{
		return this.dlls;
	}

	/**
	 * Gets the DLL files in use by the process.
	 *
	 * @return the DLL files in use by the process
	 */
	public DllFile[] getDllFiles()
	{
		return dllFiles;
	}
	
	/**
	 * Gets the files in use by the process.
	 *
	 * @return the files in use by the process
	 */
	public String[] getFiles()
	{
		return files;
	}

	/**
	 * Sets the files in use by the process. Retrieved from the command line.
	 */
	private void setFiles()
	{
		String filesOutput = commandLine.runFiles();
		int count=0;
		int index = filesOutput.indexOf("File,");
		while (index != -1)
		{
			count++;
			index = filesOutput.indexOf("File,", index+1);
		}
		String filesArr[] = new String[count];
		String file="";
		index=filesOutput.indexOf("Name")+7;
		int filesIndex=0;
		while(count!=0)
		{
			try
			{
				file=filesOutput.substring(filesOutput.indexOf("File,",index)+6, filesOutput.indexOf(10, index+1));
				System.out.println("file: "+file);
			}
			catch(StringIndexOutOfBoundsException e)
			{
				file=filesOutput.substring(filesOutput.indexOf("File,",index)+6);
			}
			filesArr[filesIndex]=file;
			filesIndex++;
			index=filesOutput.indexOf(10, index+1);
			count--;
		}
		this.files = filesArr;
	}
	
	/**
	 * Gets the process.
	 *
	 * @return the process
	 */
	public Process getProcess()
	{
		return process;
	}

	/**
	 * Sets the process.
	 *
	 * @param process the new process
	 */
	public void setProcess(Process process)
	{
		this.process = process;
	}

	/**
	 * Forcibly destroy process.
	 *
	 * @return true, if successfully destroyed
	 */
	public boolean destroyProcess()
	{
		Process process = getProcess();
		if(process == null) return false;
		process = process.destroyForcibly();
		setProcess(process);
		return true;
	}
	
	/**
	 * To string.
	 *
	 * @return the string containing values pertaining to the process manager
	 */
	@Override
	public String toString()
	{
		return "ProcessManager [file=" + file + ", process=" + process + ", commandLine=" + commandLine + ", name="
				+ name + ", dlls=" + Arrays.toString(dlls) + ", dllFiles=" + Arrays.toString(dllFiles) + "]";
	}
	
}
