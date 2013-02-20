package webSock;

import java.util.logging.Level;

import webSock.WebSocketIncomingQueueInterface;
import webSock.WebSocketOutgoingQueueInterface;

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
	/*public ISISServerApplication(WebSocketIncomingQueueInterface a, WebSocketOutgoingQueueInterface b) {
		super();
	}*/
	
	/**
	 * 
	 */
	@Override
	public WebSocket createWebSocket(ProtocolHandler handler, WebSocketListener... listeners) {
		return new ISISWebSocket(handler, listeners);
	}

	/**
	 * Implementation of abstract method
	 * Checks application specific criteria to determine if this application can process the Request as a 
	 * WebSocket connection
	 * @param request
	 * @return
	 * @see com.sun.grizzly.websockets.WebSocketApplication#isApplicationRequest(com.sun.grizzly.tcp.Request)
	 */
	@Override
	public boolean isApplicationRequest(Request request) {
		/*
		 * Decide whether the request that's received is one that the application is interested in.
		 * Grizzly essentially iterates through all registered applications and asks then whether
		 * the incoming request in one that they are interested in (Grizzly calls this)
		 */
		 // ISISServerApplication ???
		/*if(request.requestURI().toString().endsWith("/ISIS")) {
			return true;
		}
		else {
			return false;
		}*/
		// TODO : MAY WANT TO MAKE THIS MORE SPECIFIC TO NARROW DOWN ALLOWED REQUESTS TYPES
		return true;
	}

	/**
	 * Called whenever the server receives a message from a client
	 */
	@Override
	public void onMessage(WebSocket socket, String text) {
		super.onMessage(socket, text);

		// Send the received message back to the client from which it originated
		socket.send("<br />Hello Client " + socket.toString() + ". <br/>I received the " +
				"following message from you:<br />" + text);

		// Log the received message
		java.util.logging.Logger.getAnonymousLogger().log(
				Level.INFO, "Time: " + new java.util.Date() + ", " + 
						"Application received request from client " + socket.toString() + 
						". Msg: " + text);
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
						"Connection established with new WebSocket client: " + socket.toString());
	}

	/**
	 * Event handler to handle when a WebSocket connection has been closed
	 * @param socket
	 * @param frame
	 */
	@Override
	public void onClose(WebSocket socket, DataFrame frame) {
		super.onClose(socket, frame);
		
		// Create a log message
		java.util.logging.Logger.getAnonymousLogger().log(
				Level.INFO, "Time: " + new java.util.Date() + ", " + 
						"Terminated connection with WebSocket client: " + socket.toString());
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

