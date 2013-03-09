package control;

import java.util.logging.Level;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

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
				try {
					// Remove incoming message from WebSocket incoming message queue
					JSONObject msgIn = this.fromWebSock.removeNextIncomingWebSocketMsg();
					// Create log message
					java.util.logging.Logger.getAnonymousLogger().log(
							Level.INFO, "Time: " + new java.util.Date() + ", Removed incoming message: " + 
									JSONValue.toJSONString(msgIn) + " from WebSocket Incoming Queue");

					// Extract the type of request this message is (to be used later when creating JSON response)
					String msgInReqType = (String) msgIn.get(Controller.jsonCommandFieldStr);
					// Create log message
					java.util.logging.Logger.getAnonymousLogger().log(
							Level.INFO, "Time: " + new java.util.Date() + ", Extracted request type: " + 
									msgInReqType);

					// Convert the incoming JSON message to XBee packet
					RemoteData pkt = this.controller.convertJSONToPacket(msgIn);
					java.util.logging.Logger.getAnonymousLogger().log(
							Level.INFO, "Time: " + new java.util.Date() + ", Converted incoming request to packet: " + 
									pkt);

					if(pkt == null) {
						// For null returned packet, send back an error response
						JSONObject outMsg = this.controller.convertPacketToJSON(pkt, Controller.errorResponseStr);
						
						// TODO: SET ERROR MESSAGE?? REQUEST MUST HAVE BEEN INVALID
						//JSONObject paramObj = new JSONObject();
						//paramObj.put(Controller.errorMessageStr, "Server could not process invalid request");
						//outMsg.put(Controller.jsonResponseArgumentFieldStr, value);

						// Place the response on the outgoing WebSocket queue
						this.toWebSock.putItemOnOutgoingQueue(outMsg);
						java.util.logging.Logger.getAnonymousLogger().log(
								Level.INFO, "Time: " + new java.util.Date() + ", Placed response on Outgoing queue");
					}
					else {
						// Access the XBee state list to look up required information (as requested via JSON)
						RemoteData returnPkt = this.controller.getStateForController(pkt.getControllerID());
						java.util.logging.Logger.getAnonymousLogger().log(
								Level.INFO, "Time: " + new java.util.Date() + ", Corresponding packet obtained from XBee " +
										"states list: " + pkt);

						// Create the appropriate JSON response for the incoming message (using earlier saved request type)
						JSONObject msgOut = this.controller.convertPacketToJSON(returnPkt, 
								this.controller.getAppropriateResponseType(msgInReqType));
						java.util.logging.Logger.getAnonymousLogger().log(
								Level.INFO, "Time: " + new java.util.Date() + ", Response to be sent to client: " + msgOut);

						// Place the response on the outgoing WebSocket queue
						this.toWebSock.putItemOnOutgoingQueue(msgOut);
						java.util.logging.Logger.getAnonymousLogger().log(
								Level.INFO, "Time: " + new java.util.Date() + ", Placed response on Outgoing queue");
					}
				}
				catch(Exception e) {
					e.printStackTrace();
				}
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
				e.printStackTrace();
			}
			}*/
	}

	/*public static void main() {

		// Create queues
		WebSocketIncomingQueue inQ = new WebSocketIncomingQueue();
		WebSocketOutgoingQueue outQ = new WebSocketOutgoingQueue();

		// Create a new Controller
		Controller ctrl = new Controller(null, null, true);

		// Create the thread and start it
		Thread testThread2 = new Thread(new Thread2(null, inQ, outQ, null));
		testThread2.start();
	}*/
}
