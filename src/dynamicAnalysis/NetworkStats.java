package dynamicAnalysis;

import java.util.ArrayList;

public class NetworkStats
{
	private ActiveConnection[] activeConnections;

	public NetworkStats()
	{
		CommandLine commandLine = new CommandLine();
		String stats = commandLine.getNetstat();
		System.out.println(stats);
		stats=stats.substring(stats.indexOf("PID")+6);
		ArrayList<ActiveConnection> activeConnectionsList = new ArrayList<ActiveConnection>();
		while(!stats.equals(""))
		{
			try
			{
				String protocol=stats.substring(0, 3);		
				stats = spaceBreak(stats);
				String localAddress = stats.substring(0, stats.indexOf(' '));
				stats = spaceBreak(stats);
				String foreignAddress = stats.substring(0, stats.indexOf(' '));
				stats = spaceBreak(stats);
				String state = stats.substring(0, stats.indexOf(' '));
				System.out.println(state);
				long pid = 0;
				try
				{
					pid = Long.parseLong(state.substring(0, state.length()-1));
				}
				catch(NumberFormatException e)
				{
					stats = spaceBreak(stats);
					pid = Long.parseLong(stats.substring(0, stats.indexOf('\n')));
				}
				
				ActiveConnection activeConnection = new ActiveConnection(protocol, localAddress, foreignAddress, state, pid);
				activeConnectionsList.add(activeConnection);
				stats = stats.substring(stats.indexOf('\n')+3);
			}
			catch(StringIndexOutOfBoundsException e)
			{
				e.printStackTrace();
				break;
			}
		}
		ActiveConnection[] activeConnections = new ActiveConnection[activeConnectionsList.size()];
		for(int index = 0;index<activeConnections.length;index++)
		{
			activeConnections[index]=activeConnectionsList.get(index);
		}
		setActiveConnections(activeConnections);
	}
	
	private String spaceBreak(String stats)
	{
		int index=stats.indexOf(' ')+1;
		while(stats.charAt(index)==' ') index++;
		return stats.substring(index);
	}

	public ActiveConnection[] getActiveConnections()
	{
		return activeConnections;
	}

	public void setActiveConnections(ActiveConnection[] activeConnections)
	{
		this.activeConnections = activeConnections;
	}
	
}
