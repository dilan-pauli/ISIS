package webSock;

import java.io.IOException;
import java.util.logging.Level;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import control.Controller;
import webSock.WebSocketIncomingQueueInterface;
import webSock.WebSocketOutgoingQueueInterface;
import xbee.XBeeHandler;

import com.sun.grizzly.tcp.Request;
import com.sun.grizzly.websockets.DataFrame;
import com.sun.grizzly.websockets.ProtocolHandler;
import com.sun.grizzly.websockets.WebSocket;
import com.sun.grizzly.websockets.WebSocketApplication;
import com.sun.grizzly.websockets.WebSocketListener;

public class ISISServerApplication extends WebSocketApplication {

	/**
	 * Queue for placing incoming WebSocket items (from Internet clients)
	 */
	private WebSocketIncomingQueue inQueue;

	/**
	 * Queue for placing outgoing WebSocket items (bound for Internet clients)
	 */
	private WebSocketOutgoingQueue outQueue;
	
	/**
	 * Receiver to handle incoming messages
	 */
	private Receiver msgReceiver;

	/**
	 * Sender thread to send outgoing messages
	 */
	private Sender senderThread;

	
	
	
	/**
	 * Handler for XBee incoming communications
	 */
	private XBeeHandler handler;

	/**
	 * Controller for ISIS Server Application
	 */
	private Controller controller;
	
	
	

	/**
	 * Class constructor
	 */
	public ISISServerApplication() {
		super();

		// Create Incoming Queue
		this.inQueue = new WebSocketIncomingQueue();
		// Log message
		java.util.logging.Logger.getAnonymousLogger().log(
				Level.INFO, "Time: " + new java.util.Date() + ", " + 
				"Created incoming queue for the WebSocket server component");

		// Create Outgoing Queue
		this.outQueue = new WebSocketOutgoingQueue();
		// Log message
		java.util.logging.Logger.getAnonymousLogger().log(
				Level.INFO, "Time: " + new java.util.Date() + ", " + 
				"Created outgoing queue for the WebSocket server component");

		// Create XBee Handler
		try {
			this.handler = new XBeeHandler();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Log message
		java.util.logging.Logger.getAnonymousLogger().log(
				Level.INFO, "Time: " + new java.util.Date() + ", " + 
				"Created XBeeHandler for the ISIS Server Application");

		// Create Controller
		this.controller = new Controller(this.handler, this);
		// Log message
		java.util.logging.Logger.getAnonymousLogger().log(
				Level.INFO, "Time: " + new java.util.Date() + ", " + 
				"Created controller for the ISIS Server Application");

		// Create message Receiver
		this.msgReceiver = new Receiver(this.getIncomingMsgQueue());
		// Log message
		java.util.logging.Logger.getAnonymousLogger().log(
				Level.INFO, "Time: " + new java.util.Date() + ", " + 
				"Created ISISServerApplication message Receiver");

		// Create Sender Thread
		this.senderThread = new Sender(this.getOutgoingMsgQueue(), this);
		// Log message
		java.util.logging.Logger.getAnonymousLogger().log(
				Level.INFO, "Time: " + new java.util.Date() + ", " + 
				"Created ISISServerApplication Sender thread");

		// Start Sender Thread
		//this.senderThread.run();//TODO: START THIS THREAD (COMMENT BACK IN)
		// Log message
		java.util.logging.Logger.getAnonymousLogger().log(
				Level.INFO, "Time: " + new java.util.Date() + ", " + 
				"Started ISISServerApplication Sender thread");
	}




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

		// TODO : MAY WANT TO MAKE THIS MORE SPECIFIC TO NARROW DOWN ALLOWED REQUESTS TYPES LATER
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
		
		// TODO: LET THIS BE THE SOLE RESPONSE TO AN INCOMING MESSAGE LATER (COMMENT IN LATER)
		/*// Create JSON object
		JSONParser parser = new JSONParser();
		// Convert the received string to JSON
		JSONObject jsonObj = null;
		try {
			jsonObj = (JSONObject) parser.parse(text);
			// Receive the new JSON object
			this.msgReceiver.handleIncomingMessage(jsonObj);
		} catch (ParseException e) {
			e.printStackTrace();
		}*/
		
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
	protected void broadcast(String text) {
		//System.out.println("Broadcasting message to all connected WebSocket clients : " + text);
		for (WebSocket webSocket : this.getWebSockets()) {
			try {
				webSocket.send(text);
			}
			catch(Exception e) {
				e.printStackTrace();
				webSocket.close();
			}
		}
		java.util.logging.Logger.getAnonymousLogger().log(
				Level.INFO, "Broadcasted message to all connected WebSocket clients: " + text);
	}




	/**
	 * Obtain the WebSocket incoming message queue
	 */
	public WebSocketIncomingQueue getIncomingMsgQueue() {
		/*
		 * Note: the interface is returned to restrict access to select few methods
		 * to manipulate the queue
		 */
		return this.inQueue;
	}

	/**
	 * Obtain the WebSocket outgoing message queue
	 */
	public WebSocketOutgoingQueue getOutgoingMsgQueue() {
		/*
		 * Note: the interface is returned to restrict access to select few methods
		 * to manipulate the queue
		 */
		return this.outQueue;
	}
}

