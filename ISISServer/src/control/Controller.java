package control;

import java.util.HashMap;

import org.json.simple.JSONObject;

import remoteInterface.FromRemoteInterface;
import remoteInterface.RemoteData;
import remoteInterface.ToRemoteInterface;

import webSock.ISISServerApplication;
import webSock.ISISWebSocket;
import webSock.WebSocketIncomingQueueInterface;
import webSock.WebSocketOutgoingQueueInterface;
import xbee.XBeeHandler;
import control.Thread1;

public class Controller {

	/**
	 * Handlers (XBee network and WebSocket network)
	 */
	
	@SuppressWarnings("unused")
	private XBeeHandler handler;
	private ISISServerApplication isisServerApp;


	/**
	 * Controller Lists
	 */

	// List to store the states of all XBee devices
	protected HashMap<String, RemoteData> remoteStateList; // Key, Value => String(ControllerID),XbeeState

	// List to store the WebSocket clients that are currently connected to the ISIS server
	protected HashMap<String, ISISWebSocket> webSocketClientList; // Key, Value => String(WebSocketClientID),ISISWebSocket

	// List to store logical address / physical address pairs
	protected HashMap<Integer, String> physicalIDMap;
	protected HashMap<String, Integer> logicalIDMap;

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

	private Thread1 remoteToWeb;
	private Thread2 webToRemote;
	private Timer timer;


	/**
	 * Constructor
	 * @param handler
	 * @param serverApp
	 */

	public Controller(XBeeHandler handler, ISISServerApplication serverApp)
	{
		/*
		 * Set the handlers for XBee and WebSocket
		 */
		this.handler = handler;
		this.isisServerApp = serverApp;
		System.out.println("Time: " + new java.util.Date() + ", Initialized the XBee and WebSocket Handlers");

		/*
		 * Create the Controller lists
		 */
		this.remoteStateList = new HashMap<String, RemoteData>();
		this.webSocketClientList = new HashMap<String, ISISWebSocket>();
		this.physicalIDMap = new HashMap<Integer, String>();
		this.logicalIDMap = new HashMap<String, Integer>();
		System.out.println("Time: " + new java.util.Date() + ", Created XBee state list, WebSocket client list, " +
				"and Controller address mapping lists");

		/*
		 * Fill the controller address map with the Logical / Physical address mappings
		 */
		
		// Thread 1 will fill the controller map as it receices packets from XBees
		// Could we use XBee handler to get all physical addresses then assign logical addresses
		//this.controllerAddressMap.put(key, value);
		//System.out.println("Time: " + new java.util.Date() + ", Filled the logical->physical address map");

		/* 
		 * Create the Controller threads
		 */
		this.remoteToWeb = new Thread1((FromRemoteInterface)handler,
				(WebSocketOutgoingQueueInterface) isisServerApp.getOutgoingMsgQueue(),
				this);
		
		this.webToRemote = new Thread2((ToRemoteInterface) handler, 
				(WebSocketIncomingQueueInterface)isisServerApp.getIncomingMsgQueue(),
				this);
		
		this.timer = new Timer(); //TODO: Add parameters
		
		System.out.println("Time: " + new java.util.Date() + ", Created Controller Threads");

		/*
		 * Run the Controller threads
		 */
		this.remoteToWeb.run();
		this.webToRemote.run();
		this.timer.run();
		System.out.println("Time: " + new java.util.Date() + ", Started Controller Threads");
	}


	/**
	 * Controller Functions
	 */

	/**
	 *  Convert XBee Packet to JSON
	 *  This function will also call the physical to logical method and use the 
	 *  logical address for the client.
	 *  
	 * @param pkt
	 * @param destination if this is null it is a broad cast to all connected clients
	 * @return
	 */
	JSONObject convertPacketToJSON(RemoteData pkt, ISISWebSocket destination) {
		// TODO MAKE THIS LEGIT	
		//JSONObject obj = new JSONObject();

		// Figure out what kind of xbee response (packet) it is first
		/*switch(pkt.) {
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

	/**
	 * THis method will convert the JSON string into a command class. 
	 * Will be calling the logical to physical to get the physical address from 
	 * the logical one given from the browser client.
	 * 
	 * @param jsonObj
	 * @return
	 */
	RemoteData convertJSONToPacket(JSONObject jsonObj) {
		// TODO MAKE THIS LEGIT
		RemoteData pkt = null;

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
	 * @return The state of the XBee controller with the given logical ID
	 */
	RemoteData getStateForController(String id) {
		return this.remoteStateList.get(id);
	}

	// Mutator functions for XBee States list

	/**
	 * Add an XBee state to the list of XBee states. If the Remote does not exsit yet
	 * it will be created and added to the hash map. But if it is made already. The
	 * Existing state's values will be updated.
	 * @param state
	 */
	synchronized void addState(RemoteData state) {
		this.remoteStateList.put(state.getControllerID(), state);
	}
	/**
	 * Remove the XBee state for the controller with the given logical id from the list of XBee states
	 * @param id
	 */
	synchronized void removeState(String id) {
		this.remoteStateList.remove(id);
	}

	// Translator functions for XBee devices
	
	/**
	 * This function will add a physical address to the maps if it is not already in
	 * them Its logical address will the be 0 for the first one 1 for the second one and so on.
	 * If the physical address is already in the map it will return the logical address.
	 * 
	 * This function is the function that inits the maps.
	 */
	synchronized int physicalToLogical(String physical)
	{
		//The physical is already in the system return the logical.
		if(this.physicalIDMap.containsValue(physical) && this.logicalIDMap.containsKey(physical))
		{
			return this.logicalIDMap.get(physical);
		}
		//This is an init case if the physical address is not yet associated to a logical ID.
		else
		{
			this.physicalIDMap.put(physicalIDMap.size(), physical);
			this.logicalIDMap.put(physical, physicalIDMap.size());
			return this.physicalIDMap.size();
		}
	}

	/**
	 * Translate a logical controller address id to a physical XBee device address
	 * 
	 * This function does NOT ADD TO THE MAPS.
	 * 
	 * @return the corresponding physical address for a logical controller address
	 * @throws Exception 
	 */
	synchronized String logicalToPhysicalAddress(int logicalAddr) throws Exception 
	{
		//Make sure that both maps know about the logical address.
		if(this.physicalIDMap.containsKey(logicalAddr) && 
				this.logicalIDMap.containsValue(logicalAddr))
		{
			return this.physicalIDMap.get(logicalAddr);
		}
		//If one of them doesn't there is a problem.
		else
		{
			throw new Exception("The logical address is not in the map system...");
		}
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
