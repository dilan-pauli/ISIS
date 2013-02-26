package control;

import java.util.Queue;

import remoteInterface.FromRemoteInterface;
import remoteInterface.RemoteData;

import datastucts.XBeePacket;

/** 
 * Thread1 monitors the incoming queue of the XbeeHandler, translate items present on this queue 
 * from XBee packets to JSON Objects which are understood by browser clients (as specified by the Browser 
 * Response API), and then places these translated items on the Outgoing queue of the WebSocket Object.
 * 
 *  This tread will look at the remote queues and do two things when a new message is on the queue.
 *  If that message is a randomly generated button push the packet would be pushed out to all of the 
 *  client browsers and the remote state data structure will be updated.
 */
public class Thread1 implements Runnable {
	
	private FromRemoteInterface toXBeeNetworkQ;
	
	//TODO This would be the WebSocket Interface to send out to the client
	
	
	public Thread1(FromRemoteInterface xbee) {
		
		this.toXBeeNetworkQ = xbee;
		// TODO Add the WebSocket interface.
	}
	
	public void updateState(RemoteData dataPacket)
	{
			
	}
	
	public void sendToClients(RemoteData dataPaket)
	{
		
	}

	/**
	 * Running body of this thread it will monitor the remote networks for info and 
	 * update the master state list then send the update out to the websocket clients.
	 */
	@Override
	public void run() 
	{
		while(true) 
		{
			//If the queue has a message then get it
			if(this.toXBeeNetworkQ.hasMessages())
			{
				//Remove the packet
				RemoteData packet = this.toXBeeNetworkQ.getRemoteMessage();
				System.out.println("Thread1: Received a message from a remote:" + packet.getAddress());
				//update the state list and broadcast to all clients.
				updateState(packet);
				sendToClients(packet);				
			}
			//Else sleep for a bit
			else			
			{
				try {
					Thread.sleep(10*1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
