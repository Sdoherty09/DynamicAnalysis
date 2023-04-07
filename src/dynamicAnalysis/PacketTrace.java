package dynamicAnalysis;

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
	Multimap<Integer, IpPacket> packets[]; // Maps 
	Multimap<String, MultiMap<Integer, String>> addresses[]; // Maps network interface to the sending packet
	HashMap<String, Integer> interfaceMap = new HashMap<String, Integer>(); // Maps network interface to incrementing index
	HashMap<String, String> idMap = new HashMap<String, String>(); // Maps network's unique ID to readable description
	@SuppressWarnings("unchecked")
	public PacketTrace() throws PcapNativeException
	{
		devices = Pcaps.findAllDevs();
		addresses = new Multimap[devices.size()];
		for(int index = 0;index < devices.size();index++)
		{
			idMap.put(devices.get(index).getName(), devices.get(index).getDescription());
			interfaceMap.put(devices.get(index).getName(), index); // Separating to avoid synchronization issues
		}
		for (PcapNetworkInterface device : devices)
		{
			PcapHandle handle = device.openLive(65536, PcapNetworkInterface.PromiscuousMode.PROMISCUOUS, 10);
            try {
				handle.setFilter("ip", BpfProgram.BpfCompileMode.OPTIMIZE);
			} catch (NotOpenException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

            Thread listenerThread = new Thread(() -> {
                try {
                    while (true) {
                    	Packet packet = handle.getNextPacket();
                        if(packet!=null)
                        {                 	
                        	//packets[)]
                        	if (packet.contains(IpPacket.class)) {
                        	    IpPacket ipPacket = packet.get(IpPacket.class);
                        	    packets[interfaceMap.get(device.getName())].put(interfaceMap.get(device.getName()), ipPacket);
                        	    //System.out.println(ipPacket);
                        	    //System.out.println("Port: "+ port.);
                        	    // Now you can access the IP packet fields
                        	    String srcAddr = ipPacket.getHeader().getSrcAddr().getHostAddress();
                        	    //System.out.println(srcAddr);
                        	    //System.out.println(srcAddr);
                        	    String dstAddr = ipPacket.getHeader().getDstAddr().getHostAddress();
                        	    int protocol = ipPacket.getHeader().getProtocol().value();
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
	
}
