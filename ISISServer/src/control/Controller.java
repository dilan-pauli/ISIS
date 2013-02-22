package control;

import java.util.HashMap;

import org.json.simple.JSONObject;

import com.rapplogic.xbee.api.XBeeAddress64;

import datastucts.XBeePacket;
import webSock.ISISServerApplication;
import webSock.ISISWebSocket;
import xbee.XBeeHandler;

public class Controller {

	/**
	 * Handlers (Xbee network and WebSocket network)
	 */

	private XBeeHandler handler;
	private ISISServerApplication isisServerApp;


	/**
	 * Controller Lists
	 */

	// List to store the states of all XBee devices
	private HashMap<String, XBeeState> xbeeStateList; // Key, Value => String(ControllerID),XbeeState

	// List to store the WebSocket clients that are currently connected to the ISIS server
	private HashMap<String, ISISWebSocket> webSocketClientList; // Key, Value => String(WebSocketClientID),ISISWebSocket

	// List to store logical address / physical address pairs
	private HashMap<String, String> controllerAddressMap; // Key, Value => String(Logical ID), String(Physical address)


	/**
	 * WebSocket client request strings and codes
	 */

	// Keys that should appear in JSON requests (in order)
	String jsonCommandFieldStr = "CommandCode";
	String jsonRequestArgumentFieldStr = "Argument";

	// Possible values for request types
	enum RequestCommand { IO_CODE, DIAG_CODE, INIT_CODE };


	/**
	 * WebSocket client response strings and codes
	 */

	// Keys that should appear in JSON responses (in order)
	String jsonResponseFieldStr = "ResponseCode";
	String jsonResponseArgumentFieldStr = "Object";

	// Possible values for response types
	enum ResponseCode { BUTTON_EVENT, IO_RESPONSE, DIAG_RESPONSE, INIT_RESPONSE, ERROR_RESPONSE }


	/**
	 * Controller Threads
	 */

	private Thread1 xbeeInMonitor;
	private Thread2 webSocketInMonitor;
	private Timer timer;


	/**
	 * Constructor
	 * @param handler
	 * @param serverApp
	 */

	public Controller(XBeeHandler handler, ISISServerApplication serverApp)
	{
		/*
		 * Set the handlers for Xbee and WebSocket
		 */
		this.handler = handler;
		this.isisServerApp = serverApp;
		System.out.println("Time: " + new java.util.Date() + ", Initialized the XBee and WebSocket Handlers");

		/*
		 * Create the Controller lists
		 */
		this.xbeeStateList = new HashMap<String, XBeeState>();
		this.webSocketClientList = new HashMap<String, ISISWebSocket>();
		this.controllerAddressMap = new HashMap<String, String>();
		System.out.println("Time: " + new java.util.Date() + ", Created XBee state list, WebSocket client list, " +
				"and Controller address mapping list");

		/*
		 * Fill the controller address map with the Logical / Physical address mappings
		 */
		// TODO Maybe read from file?? or just hardcode key-value pairs for now??
		// Could we use XBee handler to get all physical addresses then assign logical addresses
		//this.controllerAddressMap.put(key, value);
		//System.out.println("Time: " + new java.util.Date() + ", Filled the logical->physical address map");

		/* 
		 * Create the Controller threads
		 */
		this.xbeeInMonitor = new Thread1();	//TODO: Add parameters
		this.webSocketInMonitor = new Thread2(this.isisServerApp.getIncomingMsgQueue(), 
				this.isisServerApp.getOutgoingMsgQueue(), this);
		this.timer = new Timer(); //TODO: Add parameters
		System.out.println("Time: " + new java.util.Date() + ", Created Controller Threads");

		/*
		 * Run the Controller threads
		 */
		this.xbeeInMonitor.run();
		this.webSocketInMonitor.run();
		this.timer.run();
		System.out.println("Time: " + new java.util.Date() + ", Started Controller Threads");
	}


	/**
	 * Controller Functions
	 */

	// Convert XBee Packet to JSON
	JSONObject convertXbeePacketToJSON(XBeePacket xbeePkt) {
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
	XBeePacket convertJSONToXbeePacket(JSONObject jsonObj) {
		// TODO MAKE THIS LEGIT
		XBeePacket pkt = null;

		// Figure out what kind of client request (json) it is
		/*switch((RequestCommand) jsonObj.get(this.jsonCommandFieldStr)) {
		case IO_CODE:
			//Read the value at => this.jsonArgumentFieldStr: Controller logical address
			pkt = new XBeePacket(XBeeAddress64 address);
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
		}*/

		//XBeePacket pkt = new XBeePacket(XBeeAddress64 address);

		return pkt;
	}

	// Accessor functions for XBee States list

	/**
	 * @param id
	 * @return The state of the Xbee controller with the given logical ID
	 */
	XBeeState getXBeeStateForController(String id) {
		return this.xbeeStateList.get(id);
	}

	// Mutator functions for XBee States list

	/**
	 * Add an XBee state to the list of XBee states
	 * @param state
	 */
	synchronized void addXBeeState(XBeeState state) {
		this.xbeeStateList.put(state.getControllerID(), state);
	}
	/**
	 * Remove the XBee state for the controller with the given logical id from the list of XBee states
	 * @param id
	 */
	synchronized void removeXBeeState(String id) {
		this.xbeeStateList.remove(id);
	}

	// Translator functions for XBee devices

	/**
	 * Translate a logical controller address id to a physical XBee device address
	 * @return the corresponding physical address for a logical controller address
	 */
	String logicalToPhysicalXBeeAddress(String logicalAddr) {
		return this.controllerAddressMap.get(logicalAddr);
	}

	// Accessor functions for WebSocket Client list

	/**
	 * @param clientId
	 * @return The WebSocket object for the client with the given ID
	 */
	ISISWebSocket getWebSocketForClient(String clientId) {
		return this.webSocketClientList.get(clientId);
	}

	// Mutator functions for WebSocket Client list

	/**
	 * Add a new WebSocket client to the list of WebSocket clients
	 * @param client
	 */
	synchronized void addWebSocketClient(ISISWebSocket client) {
		this.webSocketClientList.put(client.toString(), client);
	}
	/**
	 * Remove the WebSocket client with the given client id from the list of WebSocket clients
	 * @param clientId
	 */
	synchronized void removeWebSocketClient(String clientId) {
		this.webSocketClientList.remove(clientId);
	} 
}
