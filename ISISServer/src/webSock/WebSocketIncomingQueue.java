package webSock;

import java.util.LinkedList;
import java.util.Queue;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

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

	@Override
	public synchronized JSONObject removeNextIncomingWebSocketMsg() {
		if(!this.items.isEmpty()) {
			return this.items.remove();
		}
		return null;
		/*JSONObject itemRemoved = null;
		try {
			itemRemoved = this.items.remove();
		}
		catch(Exception e) {
			// Log the Exception
			java.util.logging.Logger.getAnonymousLogger().log(
					Level.INFO, "Time: " + new java.util.Date() + ", " + 
					"Exception caught: tried to remove item from empty incoming WebSocket queue");
			e.printStackTrace();
			// Set the removed item to null
			itemRemoved = null;
		}
		return itemRemoved;*/
	}

	@Override
	public boolean isEmpty() {
		return this.items.isEmpty();
	}

	public synchronized void putItemOnIncomingQueue(JSONObject item) {
		try {
			this.items.add(item);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return;
	}
	
	/**
	 * @return the collection of Incoming items
	 */
	protected Queue<JSONObject> getItems() {
		return this.items;
	}

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		// Create new WebSocket incoming queue
		WebSocketIncomingQueue myQ = new WebSocketIncomingQueue();
		//System.out.println("Initialized WebSocket Incoming test queue");


		// Create JSON Objects to put on the WebSocket incoming queue
		JSONObject obj1 = new JSONObject();
		obj1.put("name","Object1");
		obj1.put("number","11");
		//System.out.println("Created JSONObject: " + JSONValue.toJSONString(obj1));

		JSONObject obj2 = new JSONObject();
		obj2.put("name","Object2");
		obj2.put("number","12");
		//System.out.println("Created JSONObject: " + JSONValue.toJSONString(obj2));

		JSONObject obj3 = new JSONObject();
		obj3.put("name","Object3");
		obj3.put("number","13");
		//System.out.println("Created JSONObject: " + JSONValue.toJSONString(obj3));


		// Add the items to an array
		JSONObject[] testObjs = {obj1, obj2, obj3};
		// Print the test queue items
		String testItemsStr = "";
		for(int i = 0; i < testObjs.length; i++) {
			testItemsStr = testItemsStr + JSONValue.toJSONString(testObjs[i]);	
		}
		System.out.println("Test items:\n" + testItemsStr);////


		// Put all items into the queue
		for(int i = 0; i < testObjs.length; i++) {
			myQ.putItemOnIncomingQueue(testObjs[i]);
		}


		// Check to ensure that all items were inserted into the incoming queue
		if(myQ.items.isEmpty() || myQ.isEmpty()) {
			System.out.println("Error in WebSocket Incoming Queue. Queue must not be empty");
		}
		if(myQ.items.size() != 3) {
			System.out.println("Error in WebSocket Incoming Queue. Queue must contain 3 items");
		}
		for(int i = 0; i < myQ.items.size(); i++) {
			if(!myQ.items.contains(testObjs[i])) {
				System.out.println("Error in WebSocket Incoming Queue. " +
						"Missing queue item: " + testObjs[i].toJSONString());
			}
		}


		// Remove the items in order and check to ensure that they are removed in the expected order
		int j = 0;
		int j_max = testObjs.length;
		while(!myQ.items.isEmpty() && (j < j_max)) {
			if(myQ.removeNextIncomingWebSocketMsg() != testObjs[j]) {
				System.out.println("Error in WebSocket Incoming Queue. Item removed in wrong order");
			}
			j++;
		}
		/*String a = "";
		for(int i = 0; i < myQ.items.size(); i++) {
			a = a + myQ.items.toArray()[i] + " ";
		}
		System.out.println("Queue now: " + a);*/
		//System.out.println("myQ is now empty?: " + myQ.items.isEmpty());
		// Remove item from empty queue to ensure this is handled
		if(myQ.removeNextIncomingWebSocketMsg() != null) {
			System.out.println("Error in WebSocket Incoming Queue. Null was expected when attempting " +
					"to remove an item from empty queue");
		}

		//-------------------------------------------------------------------------------------------------

		// Test an interface-restricted Incoming queue (as seen by the Controller)

		// Add items back to first queue
		for(int i = 0; i < testObjs.length; i++) {
			myQ.putItemOnIncomingQueue(testObjs[i]);
		}


		// Pass the first queue to an interface-restricted incoming queue (simulates the interaction of the
		// Controller and the WebSocket Incoming Queue)
		WebSocketIncomingQueueInterface myQ2 = myQ;

		// Remove all the interface-restricted queue's items (only access allowed in the final application)
		// Also check to ensure correct order of removed items
		int k = 0;
		int k_max = testObjs.length;
		while(!myQ2.isEmpty() && (k < k_max)) {
			if(myQ2.removeNextIncomingWebSocketMsg() != testObjs[k]) {
				System.out.println("Error in interface-restricted WebSocket Incoming Queue. " +
						"Item removed in wrong order");
			}
			k++;
		}
		// Remove item from empty queue to ensure this is handled
		if(myQ2.removeNextIncomingWebSocketMsg() != null) {
			System.out.println("Error in WebSocket Incoming Queue. Null was expected when attempting " +
					"to remove an item from empty queue");
		}
		// Make sure the WebSocket Incoming Queue as seen by both objects with references to it is empty
		if(!myQ.isEmpty() || !myQ2.isEmpty()) {
			System.out.println("Error in WebSocket Incoming Queue. Queue should be seen as empty by " +
					"both object with references to it");
		}

		//-------------------------------------------------------------------------------------------------

		System.out.println("\nWebSocket Incoming Queue Test Completed!");
	}
}
