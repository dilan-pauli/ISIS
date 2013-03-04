package webSock;

import java.io.IOException;
import java.util.logging.Level;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 * Component (Thread) to monitor WebSocket outgoing queue and send items on that queue to clients via WebSocket
 * @author Dwight
 */
public class Sender implements Runnable {

	/**
	 * Outgoing queue for WebSocket (items on here are monitored and sent to clients)
	 */
	private WebSocketOutgoingQueue wsOutQueue;

	/**
	 * WebSocket application
	 */
	private ISISServerApplication isisApp;

	/**
	 * Class constructor
	 * @param wsOutQ
	 */
	public Sender(WebSocketOutgoingQueue wsOutQ, ISISServerApplication isisApp) {
		this.wsOutQueue = wsOutQ;
		this.isisApp = isisApp;
	}

	@Override
	public void run() {
		/*while(true) {
			// TODO WRITE THE TASK RUN BY THE THREAD (LOOP AS LONG AS THE PROGRAM HAS NOT EXITED)
			System.out.println("Time: " + new java.util.Date() + ", WS SENDER THREAD");
			// broadcastMessage();
			try {
				Thread.sleep(10*1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}*/
		// TODO THIS TASK IS THE USEFUL THING THIS THREAD DOES
		/*while(!this.wsOutQueue.isEmpty()) {
			broadcastMessage();
		}*/
		while(true) {
			if(!this.wsOutQueue.isEmpty()) {
				sendMessage();
			}
			else {
				try {
					Thread.sleep(20);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		/*while(!this.wsOutQueue.isEmpty()) {
			sendMessage();
		}*/

		//System.out.println("Sender thread exited");
	}

	/**
	 * Work the Sender thread does (heavy lifting done here)
	 */
	private void sendMessage() {
		JSONObject removedItem = null;
		String strToSend = "";
		try {
			removedItem = this.wsOutQueue.removeNextItemFromOutgoingQueue();
			strToSend = JSONValue.toJSONString(removedItem);
			this.isisApp.broadcast(strToSend);
			java.util.logging.Logger.getAnonymousLogger().log(
					Level.INFO, "Time: " + new java.util.Date() + ", WebSocket Sender: " +
							"Broadcast message to clients: " + strToSend);;
		}
		catch(Exception e) {
			// Broadcast should occur
			System.out.print("Exception caught. Tried to broadcast message: " + 
					strToSend + " to clients\n");
		}
	}

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		// Create JSON Objects to put on the WebSocket incoming queue (these items simulate items that
		// come in over WebSocket connections)
		JSONObject obj1 = new JSONObject();
		obj1.put("name","Object1");
		obj1.put("number","41");

		JSONObject obj2 = new JSONObject();
		obj2.put("name","Object2");
		obj2.put("number","42");

		JSONObject obj3 = new JSONObject();
		obj3.put("name","Object3");
		obj3.put("number","43");

		JSONObject obj4 = new JSONObject();
		obj4.put("name","Object4");
		obj4.put("number","44");

		JSONObject obj5 = new JSONObject();
		obj5.put("name","Object5");
		obj5.put("number","45");

		JSONObject obj6 = new JSONObject();
		obj6.put("name","Object6");
		obj6.put("number","46");

		JSONObject obj7 = new JSONObject();
		obj7.put("name","Object7");
		obj7.put("number","47");

		JSONObject obj8 = new JSONObject();
		obj8.put("name","Object8");
		obj8.put("number","48");

		JSONObject obj9 = new JSONObject();
		obj9.put("name","Object9");
		obj9.put("number","49");

		JSONObject obj10 = new JSONObject();
		obj10.put("name","Object10");
		obj10.put("number","50");

		// Add the items to an array
		JSONObject[] testObjs = {obj1, obj2, obj3, obj4, obj5, obj6, obj7, obj8, obj9, obj10};
		// Print the test queue items
		String testItemsStr = "";
		for(int i = 0; i < testObjs.length; i++) {
			testItemsStr = testItemsStr + JSONValue.toJSONString(testObjs[i]) + "\n";	
		}
		System.out.println("Test items:\n" + testItemsStr);////


		// Create test WebSocket Outgoing message queue
		WebSocketOutgoingQueue q1 = new WebSocketOutgoingQueue();
		// Add items to the test queue
		for(int i = 0; i < testObjs.length; i++) {
			q1.putItemOnOutgoingQueue(testObjs[i]);
		}


		// Create test Sender thread (ISISApplication is not passed in here but the real code which consists
		// of calling ISISApplication:send(text) will be simulated by printing a message at that point
		Thread senderThread = new Thread(new Sender(q1, null));
		senderThread.setName("TEST_SENDER_THREAD");
		/*Thread senderThread2 = new Thread(new Sender(q1, null));
		senderThread2.setName("TEST_SENDER_THREAD2");*/

		
		// Start the sender thread
		try {
			senderThread.start();
		}
		catch(Exception e) {
			System.out.println("Error occured while starting thread: " + senderThread.getName());
			e.printStackTrace();
		}
		/*// Start the sender thread
		try {
			senderThread2.start();
		}////
		catch(Exception e) {
			System.out.println("Error occurred while starting thread: " + senderThread2.getName());
			e.printStackTrace();
		}*/

		//-------------------------------------------------------------------------------------------------

		// Wait for the sender thread to exit
		System.out.println("**Enter any character and hit enter to exit the program**");
		while(senderThread.isAlive()) { //|| senderThread2.isAlive()
			try {
				if(System.in.read() > 0) {
					System.out.println("\nSender Test Completed");
					System.exit(0);
				}
			} 
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
