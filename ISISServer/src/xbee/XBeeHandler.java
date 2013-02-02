package xbee;

import java.util.LinkedList;
import java.util.Queue;

import com.rapplogic.xbee.api.XBee;

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
	
	public XBeeHandler()
	{
		toNetwork = new LinkedList<XBeePacket>();
		fromNetwork = new LinkedList<XBeePacket>();
		coordinator = new XBee();
		//Need to define the sender thread here too?
	}
	
	/**
	 * This fucntion will be used by the threads that are polling the for data that is received
	 * from the xbee network. If there is data it is reterned else the retuen in null.
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
