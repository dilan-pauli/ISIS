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
import xbee.XBeePacket;
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
	public static final String jsonCommandFieldStr = "CommandCode";
	public static final String jsonRequestArgumentFieldStr = "Argument";
	//-------------------------------------------------------------------------
	// Possible values for request types / codes
	public static final String ioCodeStr = "IO_CODE";
	public static final String diagCodeStr = "DIAG_CODE";
	public static final String initCodeStr = "INIT_CODE";


	/**
	 * WebSocket client response strings and codes
	 */

	// Keys that should appear in JSON responses (in order)
	public static final String jsonResponseFieldStr = "ResponseCode";
	public static final String jsonResponseArgumentFieldStr = "Object";
	//-------------------------------------------------------------------------
	// Possible values for response types / codes
	public static final String buttonEventStr = "BUTTON_EVENT";
	public static final String ioResponseStr = "IO_RESPONSE";
	public static final String diagResponseStr = "DIAG_RESPONSE";
	public static final String initResponseStr = "INIT_RESPONSE";
	public static final String errorResponseStr = "ERROR_RESPONSE";
	//-------------------------------------------------------------------------
	// Field names for Initialize Objects
	public static final String initFieldStr = "Initialization";
	public static final String initControllerParam = "All";

	// Field names for IO Objects
	public static final String ioControllerAddressStr = "ControllerAddress";
	public static final String ioDataArrayStr = "DataArray";

	// Field names for Diagnostic Objects
	public static final String diagControllerAddressStr = "ControllerAddress";
	public static final String diagDataArrayStr = "DataArray";
	public static final String diagIsOnStr = "ControllerOn";
	public static final String diagPinVoltageArrayStr = "PinVoltages";
	//public static final String diagPowerRemainingStr = "PowerRemaining";
	//public static final String diagWirelessStrength = "WirelessStrength";
	//public static final String diagErrorRate = "ErrorRate";

	// Field names for Button Event Objects
	public static final String buttonEventControllerAddressStr = "ControllerAddress";
	public static final String buttonEventDataArrayStr = "DataArray";

	// Field names for Error Objects
	public static final String errorMessageStr = "ErrorMessage";





	/**
	 * Controller Threads
	 */

	private Thread remoteToWeb;
	private Thread webToRemote;
	private Thread timer;


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
		// Thread 1 will fill the controller map as it receives packets from XBees
		// Could we use XBee handler to get all physical addresses then assign logical addresses
		//this.controllerAddressMap.put(key, value);
		//System.out.println("Time: " + new java.util.Date() + ", Filled the logical->physical address map");

		/* 
		 * Create the Controller threads
		 */
		this.remoteToWeb = new Thread(new Thread1((FromRemoteInterface)handler,
				(WebSocketOutgoingQueueInterface) isisServerApp.getOutgoingMsgQueue(),
				this));

		this.webToRemote = new Thread(new Thread2((ToRemoteInterface) handler, 
				(WebSocketIncomingQueueInterface) isisServerApp.getIncomingMsgQueue(),
				(WebSocketOutgoingQueueInterface) isisServerApp.getOutgoingMsgQueue(),
				this));

		this.timer = new Thread (new Timer()); //TODO: Add parameters

		System.out.println("Time: " + new java.util.Date() + ", Created Controller Threads");

		/*
		 * Run the Controller threads
		 */
		this.remoteToWeb.start();
		this.webToRemote.start();
		this.timer.start();
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
	 * @param destination if this is null it is a broadcast to all connected clients
	 * @return
	 */
	@SuppressWarnings("unchecked")
	static JSONObject convertPacketToJSON(RemoteData pkt, String responseTypeToCreate) { //ISISWebSocket destination,
		// JSONObject to return
		JSONObject obj = new JSONObject();
		// Parameter object
		JSONObject paramObj = new JSONObject();

		// What type of response is required?
		if(responseTypeToCreate.equals(Controller.buttonEventStr)) {
			paramObj.put(Controller.buttonEventControllerAddressStr, pkt.getControllerID());
			paramObj.put(Controller.buttonEventDataArrayStr, pkt.getButtonIOStates());

			// Fill in the fields of the JSONObject to return
			obj.put(Controller.jsonResponseFieldStr, Controller.buttonEventStr);
			obj.put(Controller.jsonResponseArgumentFieldStr, paramObj);
		}
		else if(responseTypeToCreate.equals(Controller.ioResponseStr)) {
			paramObj.put(Controller.ioControllerAddressStr, pkt.getControllerID());
			paramObj.put(Controller.ioDataArrayStr, pkt.getButtonIOStates());

			// Fill in the fields of the JSONObject to return
			obj.put(Controller.jsonResponseFieldStr, Controller.ioResponseStr);
			obj.put(Controller.jsonResponseArgumentFieldStr, paramObj);
		}
		else if(responseTypeToCreate.equals(Controller.diagResponseStr)) {
			paramObj.put(Controller.diagControllerAddressStr, pkt.getControllerID()); // TODO COULD ADD MORE LATER
			paramObj.put(Controller.diagDataArrayStr, pkt.getButtonIOStates());
			paramObj.put(Controller.diagIsOnStr, pkt.isOn());
			paramObj.put(Controller.diagPinVoltageArrayStr, pkt.getButtonPinVoltages());

			// Fill in the fields of the JSONObject to return
			obj.put(Controller.jsonResponseFieldStr, Controller.diagResponseStr);
			obj.put(Controller.jsonResponseArgumentFieldStr, paramObj);
		}
		else if(responseTypeToCreate.equals(Controller.initResponseStr)) {
			paramObj.put(Controller.initFieldStr, "");

			// Fill in the fields of the JSONObject to return
			obj.put(Controller.jsonResponseFieldStr, Controller.initResponseStr);
			obj.put(Controller.jsonResponseArgumentFieldStr, paramObj);
		}
		else if(responseTypeToCreate.equals(Controller.errorResponseStr)) {
			paramObj.put(Controller.errorMessageStr, ""); // TODO RemoteData: ERROR MSG SET AND GET CAPABILITY

			// Fill in the fields of the JSONObject to return
			obj.put(Controller.jsonResponseFieldStr, Controller.errorResponseStr);
			obj.put(Controller.jsonResponseArgumentFieldStr, paramObj);
		}
		else {
			obj = null;
		}

		return obj;
	}

	/**
	 * This method will convert the JSON string into a command class. 
	 * Will be calling the logical to physical to get the physical address from 
	 * the logical one given from the browser client.
	 * 
	 * @param jsonObj
	 * @return
	 */
	RemoteData convertJSONToPacket(JSONObject jsonObj) {
		if(jsonObj.get(Controller.jsonCommandFieldStr) == null) {
			return null;
		}

		// Create new packet
		RemoteData pkt = null;
		int logicalControllerId;
		String controllerIdToSet = "";

		// Figure out what kind of client request (json) it is
		if(jsonObj.get(Controller.jsonCommandFieldStr).equals(Controller.initCodeStr)) {
			try {
				pkt = new XBeePacket();
				logicalControllerId = Integer.parseInt(
						(String) jsonObj.get(Controller.jsonRequestArgumentFieldStr));
				controllerIdToSet = logicalToPhysicalAddress(logicalControllerId);
				pkt.setControllerID(controllerIdToSet);
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
		else if(jsonObj.get(Controller.jsonCommandFieldStr).equals(Controller.diagCodeStr)) {
			try {
				pkt = new XBeePacket();
				logicalControllerId = Integer.parseInt(
						(String) jsonObj.get(Controller.jsonRequestArgumentFieldStr));
				controllerIdToSet = logicalToPhysicalAddress(logicalControllerId);
				pkt.setControllerID(controllerIdToSet);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else if(jsonObj.get(Controller.jsonCommandFieldStr).equals(Controller.initCodeStr)) {
			pkt = new XBeePacket();
			pkt.setControllerID(Controller.initControllerParam);
		}
		else {
			pkt = null;
		}

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
	 * Add an XBee state to the list of XBee states. If the Remote does not exist yet
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
	synchronized void removeStateForController(String id) {
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
		// The physical is already in the system return the logical.
		if(this.physicalIDMap.containsValue(physical) && this.logicalIDMap.containsKey(physical))
		{
			return this.logicalIDMap.get(physical);
		}
		// This is an init case if the physical address is not yet associated to a logical ID.
		else
		{
			int newLogicalId = physicalIDMap.size();
			this.physicalIDMap.put(newLogicalId, physical);
			this.logicalIDMap.put(physical, newLogicalId);
			return newLogicalId;
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
		// Make sure that both maps know about the logical address.
		if(this.physicalIDMap.containsKey(logicalAddr) && 
				this.logicalIDMap.containsValue(logicalAddr))
		{
			return this.physicalIDMap.get(logicalAddr);
		}
		// If one of them doesn't there is a problem.
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

	/**
	 * Regression test for controller
	 * @param args
	 */
	public static void main(String[] args) { // TODO: MAIN FUNCTION
		// Create a new Xbee handler
		//XBeeHandler handler = new XBeeHandler();

		// Create a new controller to test
		//Controller ctrl = new Controller(XBeeHandler handler, ISISServerApplication serverApp);

		// Test the controller conversion function:
		// JSONObj convertPacketToJSON(RemoteData pkt, String responseTypeToCreate)
		
		// Test the controller conversion function:
		// RemoteData convertJSONToPacket(JSONObject jsonObj)
	}
}
