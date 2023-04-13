package dynamicAnalysis;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.ToolItem;
import org.pcap4j.core.NotOpenException;
import org.pcap4j.core.PcapNativeException;
import org.pcap4j.packet.IpPacket;
import org.pcap4j.packet.Packet;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.jface.text.TextViewer;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.FormText;
import org.eclipse.swt.widgets.Button;

public class NetworkComposite extends Composite
{
	private PacketTrace packetTrace;
	private List networkInterfaces;
	private List addresses;
	private List packets;
	private HashMap<String, String> devices;
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private boolean addressSelected = false;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 * @throws PcapNativeException 
	 */
	public NetworkComposite(Composite parent, int style) throws PcapNativeException
	{
		super(parent, style);
		setLayout(new FormLayout());
		
		networkInterfaces = new List(this, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		FormData fd_networkInterfaces = new FormData();
		fd_networkInterfaces.top = new FormAttachment(0, 41);
		fd_networkInterfaces.right = new FormAttachment(0, 211);
		networkInterfaces.setLayoutData(fd_networkInterfaces);
		
		StyledText packetInfo = new StyledText(this, SWT.BORDER | SWT.WRAP);
		packetInfo.setEditable(false);
		FormData fd_packetInfo = new FormData();
		fd_packetInfo.left = new FormAttachment(networkInterfaces, 5);
		fd_packetInfo.right = new FormAttachment(100, -4);
		fd_packetInfo.bottom = new FormAttachment(100, -172);
		fd_packetInfo.top = new FormAttachment(0, 41);
		packetInfo.setLayoutData(fd_packetInfo);
		packetInfo.setFont(SWTResourceManager.getFont("Segoe UI", 8, SWT.NORMAL));
		formToolkit.adapt(packetInfo);
		formToolkit.paintBordersFor(packetInfo);
		
		addresses = new List(this, SWT.BORDER | SWT.V_SCROLL);
		fd_networkInterfaces.left = new FormAttachment(addresses, 0, SWT.LEFT);
		fd_networkInterfaces.bottom = new FormAttachment(addresses, -6);
		FormData fd_addresses = new FormData();
		fd_addresses.bottom = new FormAttachment(0, 203);
		fd_addresses.right = new FormAttachment(0, 211);
		fd_addresses.top = new FormAttachment(0, 126);
		fd_addresses.left = new FormAttachment(0, 5);
		addresses.setLayoutData(fd_addresses);
		
		packets = new List(this, SWT.BORDER | SWT.V_SCROLL);
		FormData fd_packets = new FormData();
		fd_packets.top = new FormAttachment(addresses, 5);
		fd_packets.bottom = new FormAttachment(100, -5);
		fd_packets.right = new FormAttachment(0, 211);
		fd_packets.left = new FormAttachment(0, 5);
		packets.setLayoutData(fd_packets);
		formToolkit.adapt(packets, true, true);
		Label label = new Label(this, SWT.NONE);
		FormData fd_label = new FormData();
		fd_label.top = new FormAttachment(0, 271);
		fd_label.left = new FormAttachment(0, 216);
		label.setLayoutData(fd_label);
		formToolkit.adapt(label, true, true);
		
		StyledText hexPayload = new StyledText(this, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		hexPayload.setEditable(false);
		FormData fd_hexPayload = new FormData();
		fd_hexPayload.right = new FormAttachment(packetInfo, 0, SWT.RIGHT);
		fd_hexPayload.bottom = new FormAttachment(packetInfo, 85, SWT.BOTTOM);
		fd_hexPayload.top = new FormAttachment(packetInfo, 6);
		hexPayload.setLayoutData(fd_hexPayload);
		formToolkit.adapt(hexPayload);
		formToolkit.paintBordersFor(hexPayload);
		
		StyledText asciiPayload = new StyledText(this, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		asciiPayload.setEditable(false);
		fd_hexPayload.left = new FormAttachment(asciiPayload, 0, SWT.LEFT);
		FormData fd_asciiPayload = new FormData();
		fd_asciiPayload.right = new FormAttachment(100, -4);
		fd_asciiPayload.top = new FormAttachment(hexPayload, 6);
		fd_asciiPayload.bottom = new FormAttachment(100, -5);
		asciiPayload.setLayoutData(fd_asciiPayload);
		formToolkit.adapt(asciiPayload);
		formToolkit.paintBordersFor(asciiPayload);
		
		Label lblNewLabel = formToolkit.createLabel(this, "Hex Payload", SWT.NONE);
		lblNewLabel.setBackground(SWTResourceManager.getColor(240, 240, 240));
		FormData fd_lblNewLabel = new FormData();
		fd_lblNewLabel.top = new FormAttachment(packetInfo, 6);
		fd_lblNewLabel.right = new FormAttachment(hexPayload, -6);
		lblNewLabel.setLayoutData(fd_lblNewLabel);
		
		Label lblAsciiPayload = formToolkit.createLabel(this, "ASCII Payload", SWT.NONE);
		lblAsciiPayload.setBackground(SWTResourceManager.getColor(240, 240, 240));
		fd_asciiPayload.left = new FormAttachment(0, 299);
		FormData fd_lblAsciiPayload = new FormData();
		fd_lblAsciiPayload.top = new FormAttachment(hexPayload, 6);
		fd_lblAsciiPayload.right = new FormAttachment(asciiPayload, -6);
		lblAsciiPayload.setLayoutData(fd_lblAsciiPayload);
		
		Button btnFilterByProcess = new Button(this, SWT.CHECK);
		btnFilterByProcess.setGrayed(true);
		btnFilterByProcess.setText("Filter by process");
		FormData fd_btnFilterByProcess = new FormData();
		fd_btnFilterByProcess.top = new FormAttachment(0, 10);
		fd_btnFilterByProcess.left = new FormAttachment(packetInfo, 0, SWT.LEFT);
		btnFilterByProcess.setLayoutData(fd_btnFilterByProcess);
		formToolkit.adapt(btnFilterByProcess, true, true);
		
		networkInterfaces.addListener(SWT.Selection, new Listener() {
		      public void handleEvent(Event e) {
		    	addresses.removeAll();
		        String deviceName = (String) networkInterfaces.getData(networkInterfaces.getSelection()[0]);
		        String[] addressesArr = packetTrace.getAddresses(deviceName);
		        System.out.println("addresses length: "+addressesArr.length);
		        for(int index = 0; index < addressesArr.length; index++)
		        {
		        	try
		        	{
		        		addresses.add(addressesArr[index]);
		        	}
		        	catch(IllegalArgumentException e1)
		        	{
		        		e1.printStackTrace();
		        		continue;
		        	}
		        }
		      }
		    });
		addresses.addListener(SWT.Selection, new Listener() {
		      public void handleEvent(Event e) {
		    	  packets.removeAll();
		    	  addressSelected = true;	    	  
		      }
		    });
		packets.addListener(SWT.Selection, new Listener() {
		      public void handleEvent(Event e) {
		    	  IpPacket packet = ((IpPacket)packets.getData(packets.getSelection()[0]));
		    	  packetInfo.setText(packet.getHeader().toString());
		    	  byte[] payloadBytes = packet.getPayload().getRawData();
	    		  String asciiPayloadStr = "";
	    		  String hexPayloadStr = "";
	    		  for(int index = 0; index < payloadBytes.length;index++)
	    		  {
	    			  asciiPayloadStr+=(char) payloadBytes[index];
	    			  hexPayloadStr+=String.format("%02X ", payloadBytes[index]);
	    		  }
	    		  asciiPayload.setText(asciiPayloadStr);
	    		  hexPayload.setText(hexPayloadStr);
		      }
		    });
		packetTrace = new PacketTrace();
		updateNetwork();
	}
	
	private synchronized void updateNetwork()
	{
		Thread updateThread = new Thread(() -> { // Set up thread for each network interface
            while (true)
            {           	
            	HashMap<String, String> newDevices = packetTrace.getDevices();
            	
            		Display.getDefault().asyncExec(new Runnable() {
                        public void run() {
                        	if(!newDevices.equals(devices))
                        	{
                        		devices = newDevices;
	                        	networkInterfaces.removeAll();
	                        	for(Entry<String, String> device : devices.entrySet())
	                    		{
	                    			networkInterfaces.add(device.getValue());
	                    			networkInterfaces.setData(device.getValue(), device.getKey());
	                    		}
                        	}
                        	if(addressSelected)
                        	{
                        	  ArrayList<IpPacket> packetList = packetTrace.getPackets(addresses.getSelection()[0]);
	          		    	  for(IpPacket packet : packetList)
	          		    	  {
	          		    		  String packetDescription = packet.getHeader().getProtocol().name()+" - "+packet.getPayload().getRawData().length+" bytes {"+packet.hashCode()+"}";
	          		    		  packets.add(packetDescription);
	          		    		  packets.setData(packetDescription, packet);
	          		    	  }
                        	}
                        }
                    });           	         	
            	try
				{
					TimeUnit.SECONDS.sleep(1);
				} catch (InterruptedException e)
				{
					e.printStackTrace();
				}
            }
        });
        updateThread.start();
	}
	
	@Override
	protected void checkSubclass()
	{
		// Disable the check that prevents subclassing of SWT components
	}
}

//list netstat info
//dropdowns for network interface, address, list packet
//show udp/tcp info