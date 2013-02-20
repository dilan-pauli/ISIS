package control;

import java.util.HashMap;

import org.json.simple.JSONObject;

import com.rapplogic.xbee.api.XBeeAddress64;

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
	 * Controller Lists
	 */

	// List to store the states of all XBee devices
	//private LinkedList<XBeeState> xbeeStateList;
	private HashMap<String, XBeeState> xbeeStateList; // Key, Value => String(ControllerID),XbeeState

	// List to store the WebSocket clients that are currently connected to the ISIS server
	//private LinkedList<ISISWebSocket> webSocketClientList;
	private HashMap<String, ISISWebSocket> webSocketClientList; // Key, Value => String(WebSocketClientID),ISISWebSocket

	// List to store logical address / physical address pairs
	private HashMap<String, String> controllerAddressMap; // Key, Value => String(Logical ID), String(Physical address)


	/**
	 * WebSocket client request strings and codes
	 */

	private String jsonCommandFieldStr = "CommandCode";
	private String jsonArgumentFieldStr = "Argument";
	private enum RequestCommand { IO_CODE, DIAG_CODE, INIT_CODE };

	
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
		this.controllerAddressMap = new HashMap<String, String>();

		/*
		 * Fill the controller address map with the Logical / Physical address mappings
		 */
		// TODO Maybe read from file?? or just hardcode key-value pairs for now??
		// Could we use XBee handler to get all physical addresses then assign logical addresses
		//this.controllerAddressMap.put(key, value);

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
	 * Controller Functions
	 */

	// Convert XBee Packet to JSON
	private JSONObject convertXbeePacketToJSON(XBeePacket xbeePkt) {
		// TODO MAKE THIS LEGIT	
		JSONObject obj = new JSONObject();
		
		// Figure out what kind of xbee response (packet) it is first
		/*switch(xbeePkt.) {
		case 1:
			break;
		default:
			break;
		}*/

		//JSONObject obj=new JSONObject();
		//obj.put("name","foo");
		//OR
		//Map obj=new LinkedHashMap();
		//String jsonText = JSONValue.toJSONString(obj);

		return null;
	}

	// Convert JSON to XBee Packet
	private XBeePacket convertJSONToXbeePacket(JSONObject jsonObj) {
		// TODO MAKE THIS LEGIT
		XBeePacket pkt;
		
		// Figure out what kind of client request (json) it is
		switch((RequestCommand) jsonObj.get(this.jsonCommandFieldStr)) {
		case IO_CODE:
			//Read the value at => this.jsonArgumentFieldStr;
			//pkt = new XBeePacket(XBeeAddress64 address);
			break;
		case DIAG_CODE:
			//Read the value at => this.jsonArgumentFieldStr;
			//pkt = new XBeePacket(XBeeAddress64 address);
			break;
		case INIT_CODE:
			//Read the value at => this.jsonArgumentFieldStr;
			//pkt = new XBeePacket(XBeeAddress64 address);
			break;
		default:
			break;
		}

		//XBeePacket pkt = new XBeePacket(XBeeAddress64 address);

		return null;
	}

	// Accessor functions for XBee States list

	/**
	 * @param id
	 * @return The state of the Xbee controller with the given logical ID
	 */
	private XBeeState getXBeeStateForController(String id) {
		return this.xbeeStateList.get(id);
	}

	// Mutator functions for XBee States list

	/**
	 * Add an XBee state to the list of XBee states
	 * @param state
	 */
	private void addXBeeState(XBeeState state) {
		this.xbeeStateList.put(state.getControllerID(), state);
	}
	/**
	 * Remove the XBee state for the controller with the given logical id from the list of XBee states
	 * @param id
	 */
	private void removeXBeeState(String id) {
		this.xbeeStateList.remove(id);
	}

	// Translator functions for Xbee devices

	/**
	 * Translate a logical controller address id to a physical XBee device address
	 */
	private String logicalToPhysicalXBeeAddress(String logicalAddr) {
		//TODO
		return this.controllerAddressMap.get(logicalAddr);
	}

	// Accessor functions for WebSocket Client list

	/**
	 * @param clientId
	 * @return The WebSocket object for the client with the given ID
	 */
	private ISISWebSocket getWebSocketForClient(String clientId) {
		return this.webSocketClientList.get(clientId);
	}

	// Mutator functions for WebSocket Client list

	/**
	 * Add a new WebSocket client to the list of WebSocket clients
	 * @param client
	 */
	private void addWebSocketClient(ISISWebSocket client) {
		this.webSocketClientList.put(client.toString(), client);
	}
	/**
	 * Remove the WebSocket client with the given client id from the list of WebSocket clients
	 * @param clientId
	 */
	private void removeWebSocketClient(String clientId) {
		this.webSocketClientList.remove(clientId);
	}

	// TODO: CONSIDER MOVING Thread1, Thread2, and Timer INSIDE THIS CLASS AS INNER CLASSES FOR EASIER ACCESS TO CONTROLLER METHODS 
}
