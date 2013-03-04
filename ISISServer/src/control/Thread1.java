package control;

import java.util.logging.Level;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import remoteInterface.FromRemoteInterface;
import remoteInterface.RemoteData;
import webSock.WebSocketOutgoingQueueInterface;
import control.Controller;

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

	private FromRemoteInterface FromXBeeNetworkQ;

	private WebSocketOutgoingQueueInterface toWebSocket;

	private Controller controller;


	public Thread1(FromRemoteInterface xbee, WebSocketOutgoingQueueInterface web, Controller ctrl) 
	{
		this.toWebSocket = web;
		this.FromXBeeNetworkQ = xbee;
		this.controller = ctrl;
	}

	public void sendToClients(RemoteData dataPacket)
	{
		//Convert the pkt to JSON and have the dst be null so that it ends up being a broadcast.
		JSONObject obj = controller.convertPacketToJSON(dataPacket, Controller.buttonEventStr); //null,
		java.util.logging.Logger.getAnonymousLogger().log(
				Level.INFO, "Time: " + new java.util.Date() + ", Thread1: Converted packet to JSON -> " + 
						JSONValue.toJSONString(obj));

		//Send the packet out to the world.
		toWebSocket.putItemOnOutgoingQueue(obj);
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
			if(this.FromXBeeNetworkQ.hasMessages())
			{
				//Remove the packet
				RemoteData packet = this.FromXBeeNetworkQ.getRemoteMessage();
				java.util.logging.Logger.getAnonymousLogger().log(
						Level.INFO, "Time: " + new java.util.Date() + ", Thread1: Received a " +
								"message from a remote:" + packet.getControllerID());

				//update the state list with the packets information.
				controller.addState(packet);
				java.util.logging.Logger.getAnonymousLogger().log(
						Level.INFO, "Time: " + new java.util.Date() + ", Thread1: added state to XBee " +
								"States list:" + packet.getControllerID());

				//Send to all clients
				sendToClients(packet);	
				java.util.logging.Logger.getAnonymousLogger().log(
						Level.INFO, "Time: " + new java.util.Date() + ", Thread1: Called function to " +
								"update client info" + packet.getControllerID());
			}
			//Else sleep for a bit
			else			
			{
				try {
					Thread.sleep(20);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
