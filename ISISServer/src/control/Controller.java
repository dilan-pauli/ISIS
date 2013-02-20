package control;

import java.util.HashMap;

import org.json.simple.JSONObject;

import datastucts.XBeePacket;
import webSock.ISISWebSocket;
import webSock.WebSocketServer;
import xbee.XBeeHandler;

public class Controller {
	/**
	 * Handlers (Xbee network and WebSocket network)
	 */
	private XBeeHandler handler;
	private WebSocketServer server;

	/**
	 * Controller Threads
	 */
	/* 
	 * Thread1 monitors the incoming queue of the XbeeHandler, translate items present on this queue 
	 * from XBee packets to JSON Objects which are understood by browser clients (as specified by the Browser 
	 * Response API), and then place these translated items on the Outgoing queue of the WebSocket Object 
	 */
	private Thread1 xbeeInMonitor;
	/*
	 * Thread2 monitors the Incoming queue of the WebSocket Object. It will translate items present 
	 * on this queue from JSON Objects (as specified by the Browser Request API) and query the XBee state 
	 * list for the specific information that the client is after.
	 */
	private Thread2 webSocketInMonitor;
	/*
	 * Timer thread sleeps and periodically wakes up to put XBee packet commands on the XBee 
	 * Object’s Outgoing queue to check whether controllers are present and updates the XBee state list 
	 * accordingly
	 */
	private Timer timer;

	/**
	 * Controller Object lists
	 */
	// List to store the states of all XBee devices
	//private LinkedList<XBeeState> xbeeStateList;
	private HashMap<String, XBeeState> xbeeStateList;//K,V => String(ControllerID),XbeeState
	// List to store the WebSocket clients that are currently connected to the ISIS server
	//private LinkedList<ISISWebSocket> webSocketClientList;
	private HashMap<String, ISISWebSocket> webSocketClientList;//K,V => String(WebSocketClientID),ISISWebSocket

	/**
	 * Constructor
	 * @param handler
	 * @param server
	 */
	public Controller(XBeeHandler handler, WebSocketServer server)
	{
		/*
		 * Set the handlers for Xbee and WebSocket
		 */
		this.handler = handler;
		this.server = server;

		/*
		 * Create the Controller lists
		 */
		this.xbeeStateList = new HashMap<String, XBeeState>();
		this.webSocketClientList = new HashMap<String, ISISWebSocket>();

		/* 
		 * Create the Controller threads
		 */
		this.xbeeInMonitor = new Thread1();
		this.webSocketInMonitor = new Thread2();
		this.timer = new Timer();

		/*
		 * Run the Controller threads
		 */
		this.xbeeInMonitor.run();
		this.webSocketInMonitor.run();
		this.timer.run();
	}

	/**
	 * Controller Object functions
	 */
	
	// Convert XBee Packet to JSON
	private JSONObject convertXbeePacketToJSON(XBeePacket xbeePkt) {
		// TODO MAKE THIS LEGIT	
		// Figure out what kind of xbee response (packet) it is first
		return null;
	}
	
	// Convert JSON to XBee Packet
	private XBeePacket convertJSONToXbeePacket(JSONObject jsonObj) {
		// TODO MAKE THIS LEGIT
		// Figure out what kind of client request (json) it is first
		return null;
	}
	
	// Accessor functions for XBee States list
	
	/**
	 * @param id
	 * @return The state of the Xbee controller with the given ID
	 */
	private XBeeState getXBeeStateForController(String id) {
		return this.xbeeStateList.get(id);
	}
	
	// Accessor functions for WebSocket Client list
	
	/**
	 * @param wsClientId
	 * @return The WebSocket object for the client with the given ID
	 */
	private ISISWebSocket getWebSocketForClient(String wsClientId) {
		return this.webSocketClientList.get(wsClientId);
	}
	
	// Mutator functions for XBee States list
	
	/**
	 * Add an XBee state the the list of XBee states
	 * @param state
	 */
	private void addXBeeState(XBeeState state) {
		this.xbeeStateList.put(state.getControllerID(), state);
	}
	/**
	 * Remove the XBee state for the controller with the given id from the list of XBee states
	 * @param id
	 */
	private void removeXBeeState(String id) {
		this.xbeeStateList.remove(id);
	}
	
	// Mutator functions for WebSocket Client list
	
	/**
	 * Add s WebSocket client to the list of WebSocket clients
	 * @param client
	 */
	private void addWebSocketClient(ISISWebSocket client) {
		this.webSocketClientList.put(client.toString(), client);
	}
	/**
	 * Remove the WebSocket client with the given id from the list of WebSocket clients
	 * @param clientId
	 */
	private void removeWebSocketClient(String clientId) {
		this.webSocketClientList.remove(clientId);
	}
}
