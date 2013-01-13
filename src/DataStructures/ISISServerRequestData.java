package DataStructures;

public class ISISServerRequestData {
	
	/**
	 * Possible types of requests
	 */
	public static enum ISISRequest {
		HANDLE_USB_DATA, HANDLE_SIGNALS_BUFFER, HANDLE_DIAGNOSTICS
	}
	
	public static ISISRequest convertToISISRequest(String reqText) {
		reqText.trim();
		return ISISRequest.valueOf(reqText);
	}
}
