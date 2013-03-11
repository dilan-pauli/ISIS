/**
* WebSocket client request strings and codes
*/

// Keys that should appear in JSON requests (in order)
var jsonCommandFieldStr = "CommandCode";
var jsonRequestArgumentFieldStr = "Argument";
//-------------------------------------------------------------------------
// Possible values for request types / codes
var ioCodeStr = "IO_CODE";
var diagCodeStr = "DIAG_CODE";
var initCodeStr = "INIT_CODE";


/**
 * WebSocket client response strings and codes
 */
// Keys that should appear in JSON responses (in order)
var jsonResponseFieldStr = "ResponseCode";
var jsonResponseArgumentFieldStr = "Object";
//-------------------------------------------------------------------------
// Possible values for response types / codes
var buttonEventStr = "BUTTON_EVENT";
var ioResponseStr = "IO_RESPONSE";
var diagResponseStr = "DIAG_RESPONSE";
var initResponseStr = "INIT_RESPONSE";
var errorResponseStr = "ERROR_RESPONSE";
//-------------------------------------------------------------------------
// Field names for Initialize Objects
var initFieldStr = "Initialization";
var initControllerParam = "All";

// Field names for IO Objects
var ioControllerAddressStr = "ControllerAddress";
var ioDataArrayStr = "DataArray";

// Field names for Diagnostic Objects
var diagControllerAddressStr = "ControllerAddress";
var diagDataArrayStr = "DataArray";
var diagIsOnStr = "ControllerOn";
var diagPinVoltageArrayStr = "PinVoltages";
//var diagPowerRemainingStr = "PowerRemaining";
//var diagWirelessStrength = "WirelessStrength";
//var diagErrorRate = "ErrorRate";

// Field names for Button Event Objects
var buttonEventControllerAddressStr = "ControllerAddress";
var buttonEventDataArrayStr = "DataArray";

// Field names for Error Objects
var errorMessageStr = "ErrorMessage";

//these three states are the identifiers
/*var IO_CODE = 1;
var DIAG_CODE = 2;
var INIT_CODE = 3;*/



/*
 * Obtain button input information for the specified controller
 * Take the parameter coming, figure out for that function call.
 * What keys and values need to be in the JSON object request?
 * For example, if someone calls getio, send a josn object field command code
 * and command argument.
 */
function getIO(ControllerAddress, WSObj){
    
    var JSONobject = {};
    JSONobject.CommandCode = ioCodeString;
    JSONobject.Argument = ControllerAdress;
    
    //send obj through websocket
    WSObj.send(JSONobject);
}

/*
 * Obtain diagnostic information for the specified controller
 */
function getDiagnostics(ControllerAddress, WSObj){
    
    var JSONobject = {};
    JSONobject.CommandCode = diagCodeString;
    JSONoject.Argument = ControllerAddress;
    
    //send through websocket
    WSObj.send(JSONobject);
}

/*
 * Initialize the state report of all controllers in the network
 */
function initialize(WSObj)
{
    //open websocket connection and leave it open
    var JSONoject = {};
    JSONobject.CommandCode = initCodeString;
    JSONoject.Argument = "";
    
    //send the initialize code
    WSObj.send(JSONobject);
    
}

/*
 * this function will close the connection with the websocket a
 */
function terminate(WSObj){
    //close all connections and end the function
    WSObj.close();
}