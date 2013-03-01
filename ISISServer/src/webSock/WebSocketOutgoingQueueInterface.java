package webSock;

import org.json.simple.JSONObject;

/**
 * Interface presented to the Controller in order to access the WebSocket Outgoing message queue
 * @author Dwight
 *
 */
public interface WebSocketOutgoingQueueInterface {
	/**
	 * Insert an item onto the WebSocket outgoing queue
	 * @param item
	 */
	public void putItemOnOutgoingQueue(JSONObject item);
	
	/**
	 * @return true if the WebSocket outgoing queue is full
	 */
	/*TODO NO NEED FOR THIS. GET RID OF IT LATER public boolean isFull();*/
}
