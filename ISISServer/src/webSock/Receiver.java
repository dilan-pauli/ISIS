package webSock;

import org.json.simple.JSONObject;

/**
 * Object to put all incoming messages on the WebSocket incoming queue
 * @author Dwight
 *
 */
public class Receiver {
	
	/**
	 * The WebSocket incoming messages queue
	 */
	private WebSocketIncomingQueueInterface wsInQueue;
	
	public Receiver(WebSocketIncomingQueueInterface wsInQ) {
		this.wsInQueue = wsInQ;
	}
	
	protected void handleIncomingMessage(JSONObject item) {
		this.wsInQueue.putItemOnIncomingQueue(item);
	}
}
