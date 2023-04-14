/*
 * 
 */
package dynamicAnalysis;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * The Class ProcessManager.
 */
public class ProcessManager
{
	
	/** The file. */
	private File file;
	
	/** The process. */
	private Process process;
	
	/** The command line. */
	private CommandLine commandLine;
	
	/** The name. */
	private String name;
	
	/** The dlls. */
	private String[] dlls;
	
	/** The dll files. */
	private DllFile[] dllFiles;
	
	/** The files. */
	private String[] files;
	
	/**
	 * Instantiates a new process manager.
	 *
	 * @param file the file
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
	 * Instantiates a new process manager.
	 *
	 * @param pid the pid
	 */
	public ProcessManager(int pid)
	{
		commandLine = new CommandLine(pid);
		setName();
		setDLLs();
		setFiles();
	}
	
	/**
	 * Gets the file.
	 *
	 * @return the file
	 */
	public File getFile()
	{
		return file;
	}

	/**
	 * Sets the file.
	 *
	 * @param file the new file
	 */
	public void setFile(File file)
	{
		this.file = file;
	}
	
	/**
	 * Creates the process.
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
	 * Gets the pid.
	 *
	 * @return the pid
	 */
	public long getPid()
	{
		return process.pid();
	}
	
	/**
	 * Gets the pid as string.
	 *
	 * @return the pid as string
	 */
	public String getPidAsString()
	{
		return Long.toString(process.pid());
	}
	
	/**
	 * Sets the name.
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
	 * Sets the DL ls.
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
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName()
	{
		return this.name;
	}
	
	/**
	 * Gets the DL ls.
	 *
	 * @return the DL ls
	 */
	public String[] getDLLs()
	{
		return this.dlls;
	}

	/**
	 * Gets the dll files.
	 *
	 * @return the dll files
	 */
	public DllFile[] getDllFiles()
	{
		return dllFiles;
	}
	
	/**
	 * Gets the files.
	 *
	 * @return the files
	 */
	public String[] getFiles()
	{
		return files;
	}

	/**
	 * Sets the files.
	 */
	private void setFiles()
	{
		String filesOutput = commandLine.runFiles();
		System.out.println("output: "+filesOutput);
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
			}
			catch(StringIndexOutOfBoundsException e)
			{
				file=filesOutput.substring(filesOutput.indexOf("File,",index)+6);
			}
			filesArr[filesIndex]=file;
			filesIndex++;
			index=filesOutput.indexOf(10, index+1);
			System.out.println("index: "+index);
			count--;
			System.out.println(count);
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
	 * Destroy process.
	 *
	 * @return true, if successful
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
	 * @return the string
	 */
	@Override
	public String toString()
	{
		return "ProcessManager [file=" + file + ", process=" + process + ", commandLine=" + commandLine + ", name="
				+ name + ", dlls=" + Arrays.toString(dlls) + ", dllFiles=" + Arrays.toString(dllFiles) + "]";
	}
	
}
