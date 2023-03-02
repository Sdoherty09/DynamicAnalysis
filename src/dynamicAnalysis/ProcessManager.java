package dynamicAnalysis;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class ProcessManager
{
	private File file;
	private Process process;
	private CommandLine commandLine;
	private String name;
	private String[] dlls;
	private DllFile[] dllFiles;
	private String[] files;
	
	public ProcessManager(File file)
	{
		setFile(file);
		createProcess();
		commandLine = new CommandLine(getPid());
		setName();
		setDLLs();
	}

	public ProcessManager(int pid)
	{
		commandLine = new CommandLine(pid);
		setName();
		setDLLs();
		setFiles();
	}
	
	public File getFile()
	{
		return file;
	}

	public void setFile(File file)
	{
		this.file = file;
	}
	
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
		return process;
	}
	
	public long getPid()
	{
		return process.pid();
	}
	
	public String getPidAsString()
	{
		return Long.toString(process.pid());
	}
	private void setName()
	{
		String name = commandLine.runName();
		this.name = name.substring(name.lastIndexOf("\"", name.indexOf(".exe"))+1, name.lastIndexOf("\"", name.indexOf(".exe")+4));
	}
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
	public String getName()
	{
		return this.name;
	}
	public String[] getDLLs()
	{
		return this.dlls;
	}

	public DllFile[] getDllFiles()
	{
		return dllFiles;
	}
	
	public String[] getFiles()
	{
		return files;
	}

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

	@Override
	public String toString()
	{
		return "ProcessManager [file=" + file + ", process=" + process + ", commandLine=" + commandLine + ", name="
				+ name + ", dlls=" + Arrays.toString(dlls) + ", dllFiles=" + Arrays.toString(dllFiles) + "]";
	}
	
}
