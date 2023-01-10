package dynamicAnalysis;

import java.io.File;
import java.io.IOException;

public class ProcessManager
{
	private File file;
	private Process process;
	private CommandLine commandLine;
	private String name;
	private String[] dlls;
	
	public ProcessManager(File file)
	{
		setFile(file);
		createProcess();
		commandLine = new CommandLine(getPid());
		setName();
		setDLLs();
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
			} catch(StringIndexOutOfBoundsException | IllegalArgumentException e) //MAY BLOCK SOME DLLS
			{
				DLLs[dllIndex] = "";
				e.printStackTrace();
			}
			index=dllString.indexOf('\n', index+1);
			dllIndex++;
			count--;
		}
		DLLs[dllIndex]=dllString.substring(dllString.lastIndexOf(' '));
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
	
}
