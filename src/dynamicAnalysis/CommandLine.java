/*
 * 
 */
package dynamicAnalysis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * The Class CommandLine. Used to communicate with the command line whenever it is required.
 */
public class CommandLine
{
	
	/** The process ID. May not be required for certain commands. */
	private long pid;
	
	/**
	 * Instantiates a new command line.
	 *
	 * @param pid the process ID
	 */
	public CommandLine(long pid)
	{
		setPid(pid);
	}

	/**
	 * Instantiates a new command line.
	 */
	public CommandLine()
	{
		
	}
	
	/**
	 * Gets the pid.
	 *
	 * @return the pid
	 */
	public long getPid()
	{
		return pid;
	}

	/**
	 * Sets the pid.
	 *
	 * @param pid the new pid
	 */
	public void setPid(long pid)
	{
		this.pid = pid;
	}
	
	/**
	 * Parser for sending a command to the command line.
	 *
	 * @param command the command to be sent to the command line
	 * @param cmdCheck determines whether a command is native to Windows or bundled with the program
	 * @return the result of the command from the command line
	 */
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
	
	/**
	 * Uses tasklist to get relevant process information from the process ID.
	 *
	 * @return the cmd result from running the command
	 */
	//replace with listdlls
	public String runName()
	{
		return run("tasklist /fi \"pid eq "+getPid()+"\" /fo csv", true);
	}
	
	/**
	 * Lists DLLS used by the process. Credit to Mark Russinovich of Microsoft for the utility.
	 *
	 * @return the cmd result from running the command
	 */
	public String runDLLs()
	{
		return run("listdlls "+getPid(), false);
	}
	
	/**
	 * Run files.
	 *
	 * @return the cmd result from running the command
	 */
	public String runFiles()
	{
		return run("handle -p "+getPid()+" -v", false);
	}
	
	/**
	 * Gets all processes currently running.
	 *
	 * @return the cmd result from running the command
	 */
	public String getAll()
	{
		return run("tasklist /fo csv", true);
	}
	
	/**
	 * Gets the network information from all processes.
	 *
	 * @return the cmd result from running the command
	 */
	public String getNetstat()
	{
		return run("netstat -ano", true);
	}
	
	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString()
	{
		return "CommandLine [pid=" + pid + "]";
	}
	
}
