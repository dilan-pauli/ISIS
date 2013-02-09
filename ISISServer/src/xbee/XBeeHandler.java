package xbee;

import java.util.LinkedList;
import java.util.Queue;

import org.apache.log4j.Logger;

import remoteInterface.RemoteData;
import remoteInterface.RemoteInterface;

import com.rapplogic.xbee.api.ApiId;
import com.rapplogic.xbee.api.PacketListener;
import com.rapplogic.xbee.api.RemoteAtRequest;
import com.rapplogic.xbee.api.XBee;
import com.rapplogic.xbee.api.XBeeException;
import com.rapplogic.xbee.api.XBeeResponse;
import com.rapplogic.xbee.api.zigbee.ZNetRxIoSampleResponse;
import com.rapplogic.xbee.examples.RemoteAtExample;

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
public class XBeeHandler implements RemoteInterface
{
	private final static Logger log = Logger.getLogger(RemoteAtExample.class);
	
	private Queue<XBeePacket> toNetwork;
	private Queue<XBeePacket> fromNetwork;
	private XBee coordinator;
	private Thread sendingThread;
	
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
		
		//TODO - User editable COM part?
		
		//Open the connection to the XBee device.
		try {
			coordinator.open("COM3", 9600);
		} catch (XBeeException e) {
			System.out.println("Error: Opening XBee coordinator connection on specified serial port...");
			e.printStackTrace();
		}
		
		//Add the custom Listener to the XBee
		coordinator.addPacketListener(new XBeeListener());
		
		//Create and start the sending Thread
		sendingThread = new Thread(new sendingThread());
		sendingThread.start();
	}
	
	/**
	 * Method that will be used to turn on or turn off the GREEN Ack led.
	 * THis will be called when a button is pressed and then it will be 
	 * called again with a level of 0 after the button is released to turn
	 * off the light.
	 */
	private void sendAckLight(int level)
	{
		try {
			RemoteAtRequest request = new RemoteAtRequest(addr, "D4", new int[] {level});
			coordinator.sendAsynchronous(request);

		} catch (Exception e) {
			log.error("unexpected error", e);	
		}
	}
	
	private class XBeeListener implements PacketListener
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
	
	private class sendingThread implements Runnable
	{
		@Override
		public void run() {
			// TODO Create the run order for the sending Thread.
		}
	}

	@Override
	public RemoteData getRemoteMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void sendDataToRemote(RemoteData msg) {
		// TODO Auto-generated method stub
		
	}
	
	private XBeePacket createXBeePacket(ZNetRxIoSampleResponse packet)
	{
		//TODO - Create XBeePacket.
		return null;

	}
}
