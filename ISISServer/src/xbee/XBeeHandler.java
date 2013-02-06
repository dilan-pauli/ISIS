package xbee;

import java.util.LinkedList;
import java.util.Queue;

import com.rapplogic.xbee.api.ApiId;
import com.rapplogic.xbee.api.PacketListener;
import com.rapplogic.xbee.api.XBee;
import com.rapplogic.xbee.api.XBeeException;
import com.rapplogic.xbee.api.XBeeResponse;
import com.rapplogic.xbee.api.zigbee.ZNetRxIoSampleResponse;

import datastucts.XBeePacket;

/**
 * On creation this class will instantiate the incoming and outgoing queues that are used 
 * to buffer the messages that are received from the xbee cloud and the messages that are
 * about to be sent out. Along with the queues a XBee object is defined this object is the 
 * virtual connection to the coordinator.
 * 
 * @author noquarter
 *
 */
public class XBeeHandler {
	
	private Queue<XBeePacket> toNetwork;
	private Queue<XBeePacket> fromNetwork;
	private XBee coordinator;
	
	/**
	 *	This is the constructor class for the XBEE communication system.
	 *	It will create the queues that the XBee uses for communication and 
	 *	init the xbee system. It will also set up the packet listener. 
	 */
	public XBeeHandler()
	{
		toNetwork = new LinkedList<XBeePacket>();
		fromNetwork = new LinkedList<XBeePacket>();
		coordinator = new XBee();
		
		/*TODO - Add a user user editable com port.
		//Need to define the sender thread here too?
		//Ask the console
		System.out.println("Please specify the com port to poll for the coordinator:");
		*/
		
		//Open the connection to the XBee device.
		try {
			coordinator.open("COM3", 9600);
		} catch (XBeeException e) {
			System.out.println("Error: Opening XBee coordinator connection on specified serial port...");
			e.printStackTrace();
		}
		
		//Add the custom Listener to the XBee
		coordinator.addPacketListener(new PacketListener() {
			public void processResponse(XBeeResponse response) {
				/*
				 * This will be rather long.
				 */
				if (response.getApiId() == ApiId.ZNET_IO_SAMPLE_RESPONSE) 
				{
			        ZNetRxIoSampleResponse ioSample = (ZNetRxIoSampleResponse) response;
			        //Send a IO packet back to the device to turn the LED on ???
			        
			        //Convert to XBeePacket
			        
			        //Place on the Queue
				}
			}
		});
	}
	
	/**
	 * This function will be used by the threads that are polling the for data that is received
	 * from the xbee network. If there is data it is returned else the return in null.
	 * 
	 * @return XBeePacket
	 */
	public XBeePacket getXBeePacket()
	{
		if(fromNetwork.isEmpty())
			return null;
		else
			return fromNetwork.remove();
	}
	
	/**
	 * This function will place a Xbeepacket on to the outgoing queue so that it 
	 * can be sent to the xbee network. It return -1 on failure.
	 * 
	 * @param msg
	 */
	public int sendToXbeeNetwork(XBeePacket msg)
	{
		//Need error checking here?
		try {
			toNetwork.add(msg);
		} catch (Exception e) {
			//Catch any exception and try to continue execution.
			System.out.println(e.toString());
			return -1;
		}
		
		return 0;
	}

}
