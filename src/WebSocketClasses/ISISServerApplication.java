package WebSocketClasses;

import java.util.logging.Level;

import com.sun.grizzly.tcp.Request;
import com.sun.grizzly.websockets.DataFrame;
import com.sun.grizzly.websockets.ProtocolHandler;
import com.sun.grizzly.websockets.WebSocket;
import com.sun.grizzly.websockets.WebSocketApplication;
import com.sun.grizzly.websockets.WebSocketListener;

public class ISISServerApplication extends WebSocketApplication {

	/**
	 * 
	 */
	@Override
	public WebSocket createWebSocket(ProtocolHandler handler, WebSocketListener... listeners) {
		return new ISISWebSocket(handler, listeners);
	}


	/** Implementation of abstract method
	 * Checks application specific criteria to determine if this application can process the Request as a 
	 * WebSocket connection
	 * @param request
	 * @return
	 * @see com.sun.grizzly.websockets.WebSocketApplication#isApplicationRequest(com.sun.grizzly.tcp.Request)
	 */
	@Override
	public boolean isApplicationRequest(Request request) {
		/*if(request.requestURI().toString().endsWith("/ISIS")) {
			return true;
		}
		return false;*/

		//TODO FOR NOW
		return true;
	}

	/**
	 * 
	 */
	@Override
	public void onMessage(WebSocket socket, String text) {
		super.onMessage(socket, text);

		// Log the received message
		java.util.logging.Logger.getAnonymousLogger().log(
				Level.INFO, "Time: " + new java.util.Date() + ", " + 
						"Received request from client " + socket.toString() + " : " + text);

		// Broadcast message to all connected clients
		//broadcast("Message was just received by the ISISWebSocket Server");
		
		String responseMsg;
		
		try {
			// Compose the response message
			// Echo back the received message to the client that sent the message
			responseMsg = "Hello, this is the ISIS server, " +
					"I have received the following message from you: " + text;
			// TODO: Need to figure out how this push should work
			socket.send(responseMsg);
			
			java.util.logging.Logger.getAnonymousLogger().log(
					Level.INFO, "Time: " + new java.util.Date() + ", " + 
							"Sent back response and client's original message: " + responseMsg);
		}
		catch(Exception e) {
			e.printStackTrace();
			socket.close();
		}
		
		java.util.logging.Logger.getAnonymousLogger().log(
				Level.WARNING, "Time: " + new java.util.Date() + ", " + 
						"THIS MSG IS JUST TO INDICATE THAT EXECUTION GOT PAST THE socket.send(...) CODE " +
						"IN ISISServerApplication.onMessage(...)");

		// TODO Process server request to do some action based on the received request
	}

	/**
	 * 
	 */
	@Override
	public void onConnect(WebSocket socket) {
		super.onConnect(socket);

		// Create a log message
		java.util.logging.Logger.getAnonymousLogger().log(
				Level.INFO, "Time: " + new java.util.Date() + ", " + 
						"Connection established with new WebSocket client: ", socket.toString());
	}

	/**
	 * Event handler to handle when a WebSocket connection has been closed
	 * @param socket
	 * @param frame
	 */
	@Override
	public void onClose(WebSocket socket, DataFrame frame) {
		super.onClose(socket, frame);
		
		// Close the connection
		//socket.close();
		// Remove the connection from the list of maintained connections
		//this.getWebSockets().remove(socket);

		// Create a log message
		java.util.logging.Logger.getAnonymousLogger().log(
				Level.INFO, "Time: " + new java.util.Date() + ", " +
						"Received closing msg: " + frame.getTextPayload() +
						"Connection between WebSocket client and the server " +
						"has been closed: ", socket.toString());
	}

	/**
	 * Send text over a WebSocket connection to a single client
	 * @param socket
	 * @param text
	 */
	/*private void send(WebSocket socket, String text) {
		try {
			socket.send(text);
		} catch (Exception e) {
			java.util.logging.Logger.getAnonymousLogger().log(
					Level.SEVERE, "Time: " + new java.util.Date() + ", Exception thrown. " + 
							"Removing WebSocket client from collection of WebSockets: " + e.getMessage(), e);
			e.printStackTrace();
			//TODO (PROBABLY DON'T CLOSE THIS... MIGHT BE THE PROBLEM) onClose(socket);
		}
	}*/

	/**
	 * Broadcast a message to all connected WebSocket clients (Simple WebSocket test)
	 * @param text
	 */
	/*private void broadcast(String text) {
        //System.out.println("Broadcasting message to all connected WebSocket clients : " + text);
		java.util.logging.Logger.getAnonymousLogger().log(
    			Level.INFO, "Broadcasting message to all connected WebSocket clients: " + text);
        for (WebSocket webSocket : this.getWebSockets()) {
        	try {
            	webSocket.send(text);
            }
            catch(Exception e) {
            	e.printStackTrace();
				webSocket.close();
            }
        }
    }*/
}
