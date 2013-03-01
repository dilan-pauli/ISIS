package webSock;

import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Level;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

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

	@Override
	public synchronized void putItemOnOutgoingQueue(JSONObject item) {
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

	public boolean isEmpty() {
		return this.items.isEmpty();
	}

	public synchronized JSONObject removeNextItemFromOutgoingQueue() {
		if(!this.items.isEmpty()) {
			return this.items.remove();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		// Create new WebSocket outgoing queue
		WebSocketOutgoingQueue myQ = new WebSocketOutgoingQueue();
		//System.out.println("Initialized WebSocket Outgoing test queue");


		// Create JSON Objects to put on the WebSocket incoming queue
		JSONObject obj1 = new JSONObject();
		obj1.put("name","Item1");
		obj1.put("number", "21");
		//System.out.println("Created JSONObject: " + JSONValue.toJSONString(obj1));

		JSONObject obj2 = new JSONObject();
		obj2.put("name","Item2");
		obj2.put("number","22");
		//System.out.println("Created JSONObject: " + JSONValue.toJSONString(obj2));

		JSONObject obj3 = new JSONObject();
		obj3.put("name","Item3");
		obj3.put("number","23");
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
			myQ.putItemOnOutgoingQueue(testObjs[i]);
		}

		// Check to ensure that all items were inserted into the incoming queue
		if(myQ.items.isEmpty() || myQ.isEmpty()) {
			System.out.println("Error in WebSocket Outgoing Queue. Queue must not be empty");
		}
		if(myQ.items.size() != 3) {
			System.out.println("Error in WebSocket Outgoing Queue. Queue must contain 3 items");
		}
		for(int i = 0; i < myQ.items.size(); i++) {
			if(!myQ.items.contains(testObjs[i])) {
				System.out.println("Error in WebSocket Outgoing Queue. " +
						"Missing queue item: " + testObjs[i].toJSONString());
			}
		}


		// Remove the items in order and check to ensure that they are removed in the expected order
		int j = 0;
		int j_max = testObjs.length;
		while(!myQ.items.isEmpty() && (j < j_max)) {
			if(myQ.removeNextItemFromOutgoingQueue() != testObjs[j]) {
				System.out.println("Error in WebSocket Outgoing Queue. Item removed in wrong order");
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
		if(myQ.removeNextItemFromOutgoingQueue() != null) {
			System.out.println("Error in WebSocket Outgoing Queue. Null was expected when attempting " +
					"to remove an item from empty queue");
		}


		//-------------------------------------------------------------------------------------------------

		// Test an interface-restricted Outgoing queue (as seen by the Controller)

		// Pass the first queue to an interface-restricted outgoing queue (simulates the interaction of the
		// Controller and the WebSocket Outgoing Queue)
		WebSocketOutgoingQueueInterface myQ2 = myQ;

		// Make sure the first queue is empty
		if(!myQ.isEmpty()) {
			System.out.println("Error in WebSocket Outgoing Queue. Expected empty queue at the start " +
					"of testing item addition by interface-restricted WebSocket Outgoing Queue");
		}

		// Add all items to the interface-restricted queue (only access allowed in the final application)
		// Also check whether additions were successful by having the non-restricted queue remove the items
		for(int i = 0; i < testObjs.length; i++) {
			myQ2.putItemOnOutgoingQueue(testObjs[i]);
		}
		int k = 0;
		int k_max = testObjs.length;
		while(!myQ.isEmpty() && (k < k_max)) {
			if(myQ.removeNextItemFromOutgoingQueue() != testObjs[k]) {
				System.out.println("Error in interface-restricted WebSocket Outgoing Queue. " +
						"Item removed by non-restricted queue in wrong order");
			}
			k++;
		}

		// Make sure the WebSocket Outgoing Queue is empty
		if(!myQ.isEmpty()) {
			System.out.println("Error in WebSocket Outgoing Queue. Queue should be empty at the end of test");
		}

		//-------------------------------------------------------------------------------------------------

		System.out.println("\nWebSocket Outgoing Queue Test Completed!");
	}
}
