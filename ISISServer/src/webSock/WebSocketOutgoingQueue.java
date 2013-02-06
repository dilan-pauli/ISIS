package webSock;

import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Level;

import org.json.simple.JSONObject;

public class WebSocketOutgoingQueue implements WebSocketOutgoingQueueInterface {
	/**
	 * Outgoing queue items
	 */
	private Queue<JSONObject> items;
	
	/**
	 * Constructor
	 */
	public WebSocketOutgoingQueue() {
		this.items = new LinkedList<JSONObject>();
	}
	
	public void putItemOnOutgoingQueue(JSONObject item) {
		try {
			this.items.add(item);
		}
		catch(Exception e) {
			// Log the Exception
			java.util.logging.Logger.getAnonymousLogger().log(
					Level.INFO, "Time: " + new java.util.Date() + ", " + 
					"Exception: failed to add item to Outgoing WebSocket queue");
		}
		return;
	}
}