/*
 * 
 */
package dynamicAnalysis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.List;

import org.pcap4j.core.BpfProgram;
import org.pcap4j.core.NotOpenException;
import org.pcap4j.core.PcapHandle;
import org.pcap4j.core.PcapNativeException;
import org.pcap4j.core.PcapNetworkInterface;
import org.pcap4j.core.Pcaps;
import org.pcap4j.packet.IpPacket;
import org.pcap4j.packet.Packet;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

/**
 * The Class PacketTrace. Uses several maps to tie packets to addresses, and addresses to network interfaces.
 */
public class PacketTrace
{
	
	/** The list of network devices available in the device. */
	private List<PcapNetworkInterface> devices;
	
	/** An array list that contains Google Guava multimaps which map multiple packets to a single address. */
	private ArrayList<Multimap<String, IpPacket>> packets = new ArrayList<Multimap<String, IpPacket>>();
	
	/** The Google Guava multimap that maps multiple addresses to single network interface. */
	private Multimap<String, String> addressMap = ArrayListMultimap.create();
	
	/** The interface hashmap that maps network interfaces to an incrementing index. */
	private HashMap<String, Integer> interfaceMap = new HashMap<String, Integer>();
	
	/** The id hashmap that maps network's unique ID to readable description. */
	private HashMap<String, String> idMap = new HashMap<String, String>();
	
	/** A separate thread to listen for updates to the network. */
	private Thread listenerThread;
	
	/**
	 * Instantiates a new packet trace class.
	 *
	 * @throws PcapNativeException the Pcap native exception
	 */
	public PacketTrace() throws PcapNativeException
	{
		devices = Pcaps.findAllDevs();
		for(int index = 0;index < devices.size();index++)
		{
			idMap.put(devices.get(index).getName(), devices.get(index).getDescription());
			interfaceMap.put(devices.get(index).getName(), index); // Separating from multithread to avoid synchronization issues
			packets.add(ArrayListMultimap.create());
		}
		for (PcapNetworkInterface device : devices)
		{
			PcapHandle handle = device.openLive(65536, PcapNetworkInterface.PromiscuousMode.PROMISCUOUS, 10);
            try {
				handle.setFilter("ip", BpfProgram.BpfCompileMode.OPTIMIZE);
			} catch (NotOpenException e1) {
				e1.printStackTrace();
			}
            listenerThread = new Thread(() -> { // Set up thread for each network interface
                try {
                    while (true) {
                    	Packet packet = handle.getNextPacket();
                        if(packet!=null)
                        {                 	
                        	if (packet.contains(IpPacket.class)) {
                        	    IpPacket ipPacket = packet.get(IpPacket.class);
                        	    if(!addressMap.containsKey(ipPacket.getHeader().getSrcAddr().getHostAddress())) addressMap.put(device.getName(), ipPacket.getHeader().getSrcAddr().getHostAddress());
                        	    packets.get(interfaceMap.get(device.getName())).put(ipPacket.getHeader().getSrcAddr().getHostAddress(), ipPacket); //needs address
                        	}
                        }
                        
                    }
                } catch (NotOpenException e) {
                    e.printStackTrace();
                }
            });
            listenerThread.start();
		}
	}
	
	/**
	 * Gets the hashmap of active devices. Will only contain devices that have packet activity.
	 *
	 * @return the devices
	 */
	public HashMap<String, String> getDevices()
	{
		HashMap<String, String> activeDevices = new HashMap<String, String>();
		for(PcapNetworkInterface device : devices)
		{
			if(addressMap.containsKey(device.getName()))
			{
				activeDevices.put(device.getName(), device.getDescription());
			}
		}
		return activeDevices;
	}
	
	/**
	 * Gets the string array containing all addresses from a network device.
	 *
	 * @param deviceName the name of the network device
	 * @return the addresses from the device
	 */
	public String[] getAddresses(String deviceName)
	{
		return addressMap.get(deviceName).toArray(new String[addressMap.size()]);
	}
	
	/**
	 * Gets all packets from all addresses.
	 *
	 * @return the full list of packets
	 */
	public ArrayList<Multimap<String, IpPacket>> getPackets()
	{
		return packets;
	}
	
	/**
	 * Gets the packets from a specific address. Has a timeout counter of 10 as it will be recursively called if there is a problem with concurrency.
	 *
	 * @param address the address to retrieve packets from
	 * @param attempts the incrementing number of attempts to be recursively incremented
	 * @return the packets from the chosen address
	 */
	public ArrayList<IpPacket> getPackets(String address, int attempts)
	{
		final int TIMEOUTCOUNTER = 10;
		ArrayList<IpPacket> packetList = new ArrayList<IpPacket>();
		Collection<IpPacket> packetCollection;
		for(int index = 0;index < packets.size();index++)
		{
			packetCollection = packets.get(index).get(address);
			try
			{
				for(IpPacket packet : packetCollection)
				{
					packetList.add(packet);
				}
			} catch (ConcurrentModificationException e)
			{
				if(attempts > TIMEOUTCOUNTER) return null;
				else return getPackets(address, attempts+1);
			}
			
		}
		return packetList;
	}

}
