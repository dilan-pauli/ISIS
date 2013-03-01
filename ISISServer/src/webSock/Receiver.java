package webSock;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 * Component to put all WebSocket incoming messages on the WebSocket incoming queue
 * @author Dwight
 *
 */
public class Receiver {

	/**
	 * The WebSocket incoming messages queue
	 */
	private WebSocketIncomingQueue wsInQueue;

	public Receiver(WebSocketIncomingQueue wsInQ) {
		this.wsInQueue = wsInQ;
	}

	protected void handleIncomingMessage(JSONObject item) {
		this.wsInQueue.putItemOnIncomingQueue(item);
	}

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		// Create JSON Objects to put on the WebSocket incoming queue (these items simulate items that
		// come in over WebSocket connections)
		JSONObject obj1 = new JSONObject();
		obj1.put("name","Object1");
		obj1.put("number","31");

		JSONObject obj2 = new JSONObject();
		obj2.put("name","Object2");
		obj2.put("number","32");

		JSONObject obj3 = new JSONObject();
		obj3.put("name","Object3");
		obj3.put("number","33");

		// Add the items to an array
		JSONObject[] testObjs = {obj1, obj2, obj3};
		// Print the test queue items
		String testItemsStr = "";
		for(int i = 0; i < testObjs.length; i++) {
			testItemsStr = testItemsStr + JSONValue.toJSONString(testObjs[i]);	
		}
		System.out.println("Test items:\n" + testItemsStr);////


		// Create new WebSocket Incoming Queue to which the Receiver provides incoming messages
		WebSocketIncomingQueue inQ = new WebSocketIncomingQueue();

		// Create a Receiver to handle incoming WebSocket messages
		Receiver myReceiver = new Receiver(inQ);

		// Add test items to the WebSocket Incoming Queue using the Receiver's message handling
		for(int i = 0; i < testObjs.length; i++) {
			myReceiver.handleIncomingMessage(testObjs[i]);
		}

		// Check to ensure that all items were placed on the WebSocket incoming queue
		for(int i = 0; i < testObjs.length; i++) {
			if(!inQ.getItems().contains(testObjs[i])) {
				System.out.println("Error in Receiver. Item: " + testObjs[i] + " was not successfully " +
						"added to the WebSocket Incoming Queue.");
			}
		}

		//-------------------------------------------------------------------------------------------------

		System.out.println("\nReceiver Test Completed!");
	}
}
