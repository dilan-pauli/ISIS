package control;

import org.json.simple.JSONObject;

import remoteInterface.RemoteData;
import remoteInterface.ToRemoteInterface;

import webSock.WebSocketIncomingQueueInterface;
import webSock.WebSocketOutgoingQueueInterface;

/**
 * Thread2 monitors the Incoming queue of the WebSocket Object. It will translate items present 
 * on this queue from JSON Objects (as specified by the Browser Request API) and query the XBee state 
 * list for the specific information that the client is after.
 */
public class Thread2 implements Runnable {

	private WebSocketIncomingQueueInterface fromWebSock;

	private WebSocketOutgoingQueueInterface toWebSock;

	@SuppressWarnings("unused")
	private ToRemoteInterface toRemoteNetwork;

	private Controller controller;

	public Thread2(ToRemoteInterface handler, 
			WebSocketIncomingQueueInterface web,
			WebSocketOutgoingQueueInterface webOut,
			Controller controller) {

		this.toRemoteNetwork = handler;
		this.fromWebSock = web;
		this.toWebSock = webOut;
		this.controller = controller;
	}

	@Override
	public void run() {
		while(true) {
			if(!this.fromWebSock.isEmpty()) {
				// Remove incoming message from WebSocket incoming message queue
				JSONObject msgIn = this.fromWebSock.removeNextIncomingWebSocketMsg();
				// Extract the type of request this message is (to be used later when creating JSON response)
				String msgInReqType = (String) msgIn.get(Controller.jsonCommandFieldStr);
				
				// Convert the incoming JSON message to XBee packet
				RemoteData pkt = this.controller.convertJSONToPacket(msgIn);

				// Access the XBee state list to look up required information (as requested via JSON)
				pkt = this.controller.getStateForController(pkt.getControllerID());

				// Create the appropriate JSON response for the incoming message (using earlier saved request type)
				JSONObject msgOut = this.controller.convertPacketToJSON(pkt, 
						this.controller.getAppropriateResponseType(msgInReqType));
				
				// Place the response on the outgoing WebSocket queue
				this.toWebSock.putItemOnOutgoingQueue(msgOut);
			}
			else
			{
				try {
					Thread.sleep(20);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

		/*
		 * All I want this thread to do right now is to query all of the devices at the start.
		 */
		/*RemoteCommand discover = new XBeeCommand(XBeeAddress64.BROADCAST, 0);
		this.toRemoteNetwork.sendDataToRemote(discover);
		System.out.println("Time: " + new java.util.Date() + "Thread2: Sending Discover command");*/

		/*while(true) 
		 * {
		 */
		// TODO WRITE THE TASK RUN BY THE THREAD (LOOP AS LONG AS THE PROGRAM HAS NOT EXITED)
		/*
		 * If WebSocket incoming queue is not empty, remove the front item, extract the JSON, figure out
		 * what the request is, access the XBee states list to serve this request, package up the response
		 * to be sent via WebSocket, place this JSON response on the WebSocket outgoing queue
		 * 
		 * Useful methods:

			//this.wsInQueue.removeNextIncomingWebSocketMsg();
			//this.controller.
			//this.wsOutQueue.putItemOnOutgoingQueue(item);

			System.out.println("Time: " + new java.util.Date() + ", THREAD 2");
			try {
				this.wait(14*1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}*/
	}
}
