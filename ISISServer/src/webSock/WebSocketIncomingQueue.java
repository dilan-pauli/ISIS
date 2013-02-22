package webSock;

import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Level;

import org.json.simple.JSONObject;

public class WebSocketIncomingQueue implements WebSocketIncomingQueueInterface {
	/**
	 * Incoming queue items
	 */
	private Queue<JSONObject> items;

	/**
	 * Constructor
	 */
	public WebSocketIncomingQueue() {
		this.items = new LinkedList<JSONObject>();
	}

	public synchronized JSONObject removeNextIncomingWebSocketMsg() {
		JSONObject itemRemoved = null;
		try {
			itemRemoved = this.items.remove();
		}
		catch(Exception e) {
			// Log the Exception
			java.util.logging.Logger.getAnonymousLogger().log(
					Level.INFO, "Time: " + new java.util.Date() + ", " + 
					"Exception: tried to remove item from empty incoming WebSocket queue");
			e.printStackTrace();
		}
		return itemRemoved;
	}

	@Override
	public boolean isEmpty() {
		return this.items.isEmpty();
	}

	@Override
	public synchronized void putItemOnIncomingQueue(JSONObject item) {
		try {
			this.items.add(item);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return;
	}
}
