package webSock;

import org.json.simple.JSONObject;

public interface WebSocketOutgoingQueueInterface {
	/**
	 * Insert an item onto the WebSocket outgoing queue
	 * @param item
	 */
	public void putItemOnOutgoingQueue(JSONObject item);
}
