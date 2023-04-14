/*
 * 
 */
package dynamicAnalysis;

import java.io.EOFException;
import java.io.IOException;
import java.net.InetAddress;
import java.util.List;
import java.util.concurrent.TimeoutException;

import org.pcap4j.core.*;
import org.pcap4j.core.BpfProgram.BpfCompileMode;
import org.pcap4j.packet.IpPacket;
import org.pcap4j.packet.Packet;
import org.pcap4j.packet.TcpPacket;
import org.pcap4j.packet.namednumber.IpNumber;
import org.pcap4j.packet.namednumber.TcpPort;
import org.pcap4j.packet.namednumber.UdpPort;
import org.pcap4j.packet.IpV4Packet;


/**
 * The Class NetworkTraffic.
 */
public class NetworkTraffic {

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		NetworkStats netStats = new NetworkStats();
		System.out.println(netStats.getActiveConnections()[0]);
		try {
            // Get a list of all available network interfaces
            List<PcapNetworkInterface> devices = Pcaps.findAllDevs();

            // Create a separate PcapHandle for each interface and start capturing traffic
            for (PcapNetworkInterface device : devices) {
            	//System.out.println(device.getName());
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
                            	if (packet.contains(IpPacket.class)) {
                            	    IpPacket ipPacket = packet.get(IpPacket.class);
                            	    //System.out.println(ipPacket);
                            	    //System.out.println("Port: "+ port.);
                            	    // Now you can access the IP packet fields
                            	    String srcAddr = ipPacket.getHeader().getSrcAddr().getHostAddress();
                            	   /* byte[] data = ipPacket.getPayload().getRawData();
                            	    for(int index = 0;index<data.length;index++)
                            	    {
                            	    	System.out.print((char)data[index]+"");
                            	    }*/
                            	    //System.out.println(ipPacket.getHeader().getProtocol().name()+" - "+ipPacket.getPayload().getRawData().length+" bytes {"+ipPacket.hashCode()+"}");
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

        } catch (PcapNativeException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

}
// netstat -ano
// netstat -ano -p tcp - gets local address and port
// Destination port always 49389 (unknown)