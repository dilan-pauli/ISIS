package webSock;

import org.json.simple.JSONObject;

public interface WebSocketOutgoingQueueInterface {
	/**
	 * Insert an item onto the WebSocket outgoing queue
	 * @param item
	 */
	public void putItemOnOutgoingQueue(JSONObject item);
	
	/**
	 * @return true if the WebSocket outgoing queue is empty
	 */
	public boolean isEmpty();
	
	/**
	 * Remove the next (front) item from the WebSocket outgoing queue
	 * @return The removed item
	 */
	public JSONObject removeNextItemFromOutgoingQueue();
}
