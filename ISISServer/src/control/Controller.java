package control;

import java.util.LinkedList;

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
	private Thread1 xbeeInMonitor;
	private Thread2 webSocketInMonitor;
	private Timer timer;
	
	/**
	 * Controller Object lists
	 */
	// List to store the states of all Xbee devices
	private LinkedList<XBeeState> xbeeStateList;
	// List to store the WebSocket clients that are currently connected to the ISIS server
	private LinkedList<ISISWebSocket> webSocketClientList;
	
	/**
	 * Constructor
	 * @param handler
	 * @param server
	 */
	public Controller(XBeeHandler handler, WebSocketServer server)
	{
		/* 
		 * Create Thread1 to monitor the incoming queue of the XbeeHandler, translate items present on this queue 
		 * from XBee packets to JSON Objects which are understood by browser clients (as specified by the Browser 
		 * Response API), and then place these translated items on the Outgoing queue of the WebSocket Object 
		 */
		
		/*
		 * Create Thread2 to monitor the Incoming queue of the WebSocket Object. It will translate items present 
		 * on this queue from JSON Objects (as specified by the Browser Request API) and query the XBee state 
		 * list for the specific information that the client is after.
		 */
		
		/*
		 * Create Timer thread that sleeps and periodically wakes up to put XBee packet commands on the XBee 
		 * Object’s Outgoing queue to check whether controllers are present and updates the XBee state list 
		 * accordingly
		 */
	}
	
	/**
	 * Controller Object functions
	 */
	// Convert XBee Packet to JSON
	private JSONObject convertXbeePacketToJSON(XBeePacket xbeePkt) {
		//TODO
		return null;
	}
	// Convert JSON to XBee Packet
	private XBeePacket convertJSONToXbeePacket(JSONObject jsonObj) {
		// TODO
		return null;
	}
	// Accessor functions for Xbee Module list and Xbee Client list
	
	// Mutator functions for Xbee Module list and Xbee Client list
}
