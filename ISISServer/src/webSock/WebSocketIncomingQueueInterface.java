package webSock;

import org.json.simple.JSONObject;

/**
 * Interface presented to the Controller in order to access the WebSocket Incoming message queue
 * @author Dwight
 *
 */
public interface WebSocketIncomingQueueInterface {
	/**
	 * Remove the next item from the WebSocket incoming queue 
	 * @return
	 */
	public JSONObject removeNextIncomingWebSocketMsg();
	
	/**
	 * @return true if the WebSocket incoming queue is empty
	 */
	public boolean isEmpty();
}
