package dynamicAnalysis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CommandLine
{
	private long pid;
	public CommandLine(long pid)
	{
		setPid(pid);
	}

	public CommandLine()
	{
		
	}
	
	public long getPid()
	{
		return pid;
	}

	public void setPid(long pid)
	{
		this.pid = pid;
	}
	
	private String run(String command, boolean cmdCheck)
	{
		String response = "";
		ProcessBuilder builder;
		if(cmdCheck) builder = new ProcessBuilder("cmd.exe", "/c", command);
		else builder = new ProcessBuilder("cmd.exe", "/c", "cd /d \""+System.getProperty("user.dir")+"\\lib\" && "+command);
		builder.redirectErrorStream(true);
        Process p = null;
        try {
            p = builder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line = "";
        while (true) {
            try {
                line = r.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (line == null) {
                break;
            }
            response += line + "\n";
        }
        return response;
	}
	
	//replace with listdlls
	public String runName()
	{
		return run("tasklist /fi \"pid eq "+getPid()+"\" /fo csv", true);
	}
	
	public String runDLLs()
	{
		return run("listdlls "+getPid(), false);
	}
	
	public String getAll()
	{
		return run("tasklist /fo csv", true);
	}
	
	@Override
	public String toString()
	{
		return "CommandLine [pid=" + pid + "]";
	}
	
}
