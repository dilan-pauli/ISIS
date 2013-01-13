package WebSocketClasses;

import java.util.logging.Level;

import com.sun.grizzly.tcp.Request;
//import com.sun.grizzly.websockets.DataFrame;
import com.sun.grizzly.websockets.WebSocket;
import com.sun.grizzly.websockets.WebSocketApplication;

public class ISISServerApplication extends WebSocketApplication {
	
	/** Implementation of abstract method
	 * Checks application specific criteria to determine if this application can process the Request as a 
	 * WebSocket connection
	 * @param request
	 * @return
	 * @see com.sun.grizzly.websockets.WebSocketApplication#isApplicationRequest(com.sun.grizzly.tcp.Request)
	 */
	@Override
	public boolean isApplicationRequest(Request request) {
		if(request.requestURI().toString().endsWith("/ISIS")) {
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 */
	@Override
	public void onMessage(WebSocket socket, String text) {
		super.onMessage(socket, text);
		/* 
		 * For now, just print the received message as part of a log
		 */
		java.util.logging.Logger.getAnonymousLogger().log(
    			Level.INFO, "Received server request: " + text);
		
		// Broadcast message to all connected clients
		//broadcast("Message was just received by the ISISWebSocket Server");
		
		// Echo back the received message to the client that sent the message
		send(socket, text);
		
		// TODO Process server request to do some action based on the received request
	}
	
	//public void onMessage(WebSocket socket, DataFrame frame) {
		/* 
		 * For now, just print the received message as part of a log
		 */
		/*java.util.logging.Logger.getAnonymousLogger().log(
    			Level.INFO, "Received server request: " + frame.getTextPayload());
		
		// Broadcast message to all connected clients
		//broadcast("Message was just received by the ISISWebSocket Server");
		
		// Echo back the received message to the client that sent the message
		send(socket, frame.getTextPayload());
		
		// TODO Process server request to do some action based on the received request*/
    //}
	
	/**
	 * 
	 */
	@Override
	public void onConnect(WebSocket socket) {
		super.onConnect(socket);
		java.util.logging.Logger.getAnonymousLogger().log(
    			Level.INFO, "Server connection established for WebSocket: ", socket.toString());
    }
	
	/**
	 * Close a single specified WebSocket connection
	 * @param socket
	 */
	public void onClose(WebSocket socket) {
		socket.close();
        this.getWebSockets().remove(socket);
    }
	
	/**
	 * Send text over a single WebSocket connection
	 * @param socket
	 * @param text
	 */
	private void send(WebSocket socket, String text) {
        try {
            socket.send(text);
        } catch (Exception e) {
        	java.util.logging.Logger.getAnonymousLogger().log(
        			Level.SEVERE, "Removing WebSocket client from collection: " + e.getMessage(), e);
            onClose(socket);
        }
    }
	
	/**
	 * Broadcast a message to all connected WebSocket clients (Simple WebSocket test)
	 * @param text
	 */
	/*private void broadcast(String text) {
        //System.out.println("Broadcasting message to all connected WebSocket clients : " + text);
		java.util.logging.Logger.getAnonymousLogger().log(
    			Level.INFO, "Broadcasting message to all connected WebSocket clients: " + text);
        for (WebSocket webSocket : this.getWebSockets()) {
            send(webSocket, text);
        }
    }*/
}
