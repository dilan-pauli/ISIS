package WebSocketClasses;

//import com.sun.grizzly.tcp.Request;
//import com.sun.grizzly.tcp.Response;
//import com.sun.grizzly.websockets.BaseServerWebSocket;
//import com.sun.grizzly.websockets.DataFrame;

//import java.util.logging.Level;

import com.sun.grizzly.websockets.DataFrame;
import com.sun.grizzly.websockets.DefaultWebSocket;
import com.sun.grizzly.websockets.ProtocolHandler;
import com.sun.grizzly.websockets.WebSocketListener;

public class ISISWebSocket extends DefaultWebSocket implements Runnable {

	/*
	 * IGNORE THIS COMMENT NOW: NO LONGER RELEVANT!!!!!!!!!!!!!!!!!!!!!!!
	 * 
	 * Note: ISISWebSocket extends BaseServerWebSocket which in turn extends BaseWebSocket which
	 * implements the WebSocket interface
	 * 
	 * The BaseWebSocket class contains fields for the NetworkHandler, Logger, a list of
	 * WebSocketListeners, and an AtomicBoolean variable to keep track of whether the WebSocket
	 * connection is connected or not
	 * Methods include a constructor which takes a collection of WebSocketListeners, a getter
	 * method for the NetworkHandler, a setter for the NetworkHandler, a getter for the list of
	 * WebSocketListeners, a method to check whether the connection exists or not, and the
	 * required implemented methods as a result of implementing the WebSocket interface.
	 * These methods include: add(WebSocketListener), close(), isConnected(), onClose(), 
	 * remove(WebSocketListener), send(String), send(DataFrame), onConnect(), onMessage(DataFrame)
	 * 
	 * TheBaseServerWebSocket class simply adds 2 more methods when extending the BaseWebSocket class.
	 * These methods include: HttpServletRequest getRequest(), HttpServletResponse getResponse()
	 * 
	 * The WebSocket interface defines the aforementioned methods implemented by BaseWebSocket as
	 * listed above
	 */

	/**
	 * The application this WebSocket is registered with
	 * (Actually interested in the WebSocketListener)
	 */
	// SCRAP THIS!
	//private final ISISServerApplication app;

	/**
	 * Class constructor
	 * @param listeners
	 */
	public ISISWebSocket(ProtocolHandler handler, WebSocketListener... listeners) {
		/*
		 * Note: ISISServerApplication extends WebSocketApplication which in turn extends WebSocketAdapter
		 * which in turn implements WebSocketListener.
		 * Therefore, ISISServerApplication is also of type WebSocketListener through inheritance
		 * and therefore the listener is passed to the new ISISWebSocket
		 */

		// Invoke the constructor of the super class (BaseServerWebSocket)
		super(handler, listeners);

		// SCRAP THIS!
		// Store the listener for the new ISISWebSocket
		//app = listener;
	}

	@Override
	public void onConnect() {
		super.onConnect();
		
		// Create a new thread to run on this WebSocket connection
		Thread thread = new Thread(this);
		// Start running the thread
		thread.run();
	}

	@Override
	public void onClose(DataFrame frame) {
		super.onClose(frame);
	}

	@Override
	public void send(String data) {
		super.send(data);

		// Create log message
		/*java.util.logging.Logger.getAnonymousLogger().log(
				Level.INFO, "Time: " + new java.util.Date() + ", " + 
				"Message sent over WebSocket connection");*/
	}

	/**
	 * Routine to be run by this runnable object (WebSocket connection)
	 */
	@Override
	public synchronized void run() {
		// Run this thread as long as the connection is maintained
		
		int n = 0;
		
		// TODO: LET THIS DO A USELESS TASK FOR NOW
		while(this.isConnected()) {
			// Wait for 10 seconds
			try {
				this.wait(10*1000);
			} 
			catch (InterruptedException e) {
				e.printStackTrace();
			}
			// Push data out to the client
			this.send("Test push message from server " + n++);
		}
	}
}
