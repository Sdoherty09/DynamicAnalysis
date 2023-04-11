package dynamicAnalysis;

import java.util.ArrayList;
import java.util.Collection;
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

import scala.collection.mutable.MultiMap;

public class PacketTrace
{
	private List<PcapNetworkInterface> devices;
	private Multimap<String, IpPacket> packets[]; // Maps multiple packets to single address
	private HashMap<String, String> addressMap = new HashMap<String, String>(); // Maps multiple addresses to single network interface
	private HashMap<String, Integer> interfaceMap = new HashMap<String, Integer>(); // Maps network interface to incrementing index
	private HashMap<String, String> idMap = new HashMap<String, String>(); // Maps network's unique ID to readable description
	public PacketTrace() throws PcapNativeException
	{
		devices = Pcaps.findAllDevs();
		for(int index = 0;index < devices.size();index++)
		{
			idMap.put(devices.get(index).getName(), devices.get(index).getDescription());
			interfaceMap.put(devices.get(index).getName(), index); // Separating from multithread to avoid synchronization issues
		}
		for (PcapNetworkInterface device : devices)
		{
			PcapHandle handle = device.openLive(65536, PcapNetworkInterface.PromiscuousMode.PROMISCUOUS, 10);
            try {
				handle.setFilter("ip", BpfProgram.BpfCompileMode.OPTIMIZE);
			} catch (NotOpenException e1) {
				e1.printStackTrace();
			}

            Thread listenerThread = new Thread(() -> { // Set up thread for each network interface
                try {
                    while (true) {
                    	Packet packet = handle.getNextPacket();
                        if(packet!=null)
                        {                 	
                        	if (packet.contains(IpPacket.class)) {
                        	    IpPacket ipPacket = packet.get(IpPacket.class);
                        	    this.wait();
                        	    addressMap.put(device.getName(), ipPacket.getHeader().getSrcAddr().getHostAddress());
                        	    packets[interfaceMap.get(device.getName())].put(ipPacket.getHeader().getSrcAddr().getHostAddress(), ipPacket); //needs address
                        	    this.notify();
                        	}
                        }
                        
                    }
                } catch (NotOpenException | InterruptedException e) {
                    e.printStackTrace();
                }
            });
            listenerThread.start();
		}
	}
	
	public HashMap<String, String> getDevices()
	{
		return idMap;
	}
	
	public Multimap<String, IpPacket>[] getPackets()
	{
		return packets;
	}
	
	public ArrayList<IpPacket> getPackets(String address)
	{
		ArrayList<IpPacket> packetList = new ArrayList<IpPacket>();
		Collection<IpPacket> packetCollection;
		for(int index = 0;index < packets.length;index++)
		{
			packetCollection = packets[index].get(address);
			for(IpPacket packet : packetCollection)
			{
				packetList.add(packet);
			}
		}
		return packetList;
	}

}
