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
	
	private ToNetworkQ toNetwork;
	private FromNetworkQ fromNetwork;
	private XBee coordinator;
	
	/**
	 *	This is the constructor class for the XBEE communication system.
	 *	It will create the queues that the XBee uses for communication and 
	 *	init the xbee system. It will also set up the packet listener. 
	 */
	public XBeeHandler()
	{
		toNetwork = new ToNetworkQ();
		fromNetwork = new FromNetworkQ();
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
		coordinator.addPacketListener(new XBeeListener());
	}
	
	/**
	 * Method that will be used to turn on or turn off the GREEN Ack led.
	 * THis will be called when a button is pressed and then it will be 
	 * called again with a level of 0 after the button is released to turn
	 * off the light.
	 */
	private void sendAckLight(int level)
	{
		//TODO
	}
	
	public class XBeeListener implements PacketListener
	{
		@Override
		public void processResponse(XBeeResponse response) {
			
			if (response.getApiId() == ApiId.ZNET_IO_SAMPLE_RESPONSE) 
			{
		        ZNetRxIoSampleResponse ioSample = (ZNetRxIoSampleResponse) response;
		        //Send a IO packet back to the device to turn the LED on ???
		        
		        //Convert to XBeePacket
		        
		        //Place on the Queue
			}
			
		}
	}
	
	public class sendingThread extends Thread
	{
		
	}

}
