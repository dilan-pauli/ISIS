package WebSocketClasses;

//import com.sun.grizzly.tcp.Request;
//import com.sun.grizzly.tcp.Response;
import com.sun.grizzly.websockets.BaseServerWebSocket;
//import com.sun.grizzly.websockets.DataFrame;

public class ISISWebSocket extends BaseServerWebSocket {
	
	/*
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
    @SuppressWarnings("unused")
	private final ISISServerApplication app;
    
    /**
     * Class constructor
     * @param listener
     */
    public ISISWebSocket(ISISServerApplication listener) {
    	/*
    	 * Note: ISISServerApplication extends WebSocketApplication which in turn extends WebSocketAdapter
    	 * which in turn implements WebSocketListener.
    	 * Therefore, ISISServerApplication is also of type WebSocketListener through inheritance
    	 * and therefore the listener is passed to the new ISISWebSocket
    	 */
    	
    	// Invoke the constructor of the super class (BaseServerWebSocket)
    	super(listener);
    	// Store the listener for the new ISISWebSocket
    	app = listener;
    }
    
    /*public void onMessage(String text) { //DataFrame frame
    	//super.
    }*/
}
