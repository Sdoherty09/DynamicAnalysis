package dynamicAnalysis;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;

import org.pcap4j.core.*;
import org.pcap4j.packet.Packet;

public class NetworkTraffic {

	public static void main(String[] args) {
		try {
            // Get a list of all available network interfaces
            List<PcapNetworkInterface> devices = Pcaps.findAllDevs();

            // Create a separate PcapHandle for each interface and start capturing traffic
            for (PcapNetworkInterface device : devices) {
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
                            if (packet != null) {
                                System.out.println(device.getName() + ": " + packet);
                            }
                        }
                    } catch (NotOpenException e) {
                        e.printStackTrace();
                    }
                });
                listenerThread.start();
            }

            // Wait for all listener threads to complete
            Thread.sleep(60000);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (PcapNativeException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

}
