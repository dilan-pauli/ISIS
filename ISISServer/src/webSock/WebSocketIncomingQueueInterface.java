package webSock;

import org.json.simple.JSONObject;

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
	
	/**
	 * Insert an item onto the WebSocket incoming queue
	 * @param item
	 */
	//public void putItemOnIncomingQueue(JSONObject item);
}
