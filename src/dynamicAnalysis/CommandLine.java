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

	public long getPid()
	{
		return pid;
	}

	public void setPid(long pid)
	{
		this.pid = pid;
	}
	
	private String run(String command)
	{
		String response = "";
		ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", command);
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
	
	public String runName()
	{
		return run("tasklist /fi \"pid eq "+getPid()+"\" /fo csv");
	}
	
	public String runDLLs()
	{
		return run("tasklist /m /fi \"pid eq "+getPid()+"\" /fo list");
	}
}
