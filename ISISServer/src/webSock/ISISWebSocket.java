package webSock;

import com.sun.grizzly.websockets.DefaultWebSocket;
import com.sun.grizzly.websockets.ProtocolHandler;
import com.sun.grizzly.websockets.WebSocketListener;

/**
 * Representation of a WebSocket connection
 * @author Dwight
 *
 */
public class ISISWebSocket extends DefaultWebSocket {
	/**
	 * Class constructor
	 * @param listeners
	 */
	public ISISWebSocket(ProtocolHandler handler, WebSocketListener... listeners) {
		// Invoke the constructor of the super class
		super(handler, listeners);
	}
	
	@Override
	public String toString() {
		return super.toString();
	}
}
