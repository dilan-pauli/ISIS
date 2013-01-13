package DataStructures;
/**
 * This interface is required to obtain controller data information
 * received from the USB controller which would receive this information
 * from the Xbee coordinator
 * @author Dwight
 *
 */
public interface ControllerSignalIntf {
	
	/**
	 * Obtain the controller ID for a contoller
	 */
	public int getControllerId();
	
	/**
	 * Get the controller button pressed for the given
	 * controller
	 */
	public int getControllerButtonPressed();
	
	/**
	 * Get the ID of the controller whose status needs to be
	 * enquired about
	 */
	public int getControllerToPing();
	
	/**
	 * Set the ID of the controller whose status needs to be
	 * enquired about
	 */
	public void setControllerToPing(int controllerId);
}
