package webSock;

import com.sun.grizzly.websockets.DefaultWebSocket;
import com.sun.grizzly.websockets.ProtocolHandler;
import com.sun.grizzly.websockets.WebSocketListener;

public class ISISWebSocket extends DefaultWebSocket {

	/**
	 * Temp data holder (for sending)
	 */
	//private String dataToSend;

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
	public String toString() {
		return super.toString();
	}
}
