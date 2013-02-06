package webSock;

import org.json.simple.JSONObject;

public interface WebSocketIncomingQueueInterface {
	/**
	 * Remove the next item from the WebSocket incoming queue 
	 * @return
	 */
	public JSONObject removeNextIncomingWebSocketMsg();
}
