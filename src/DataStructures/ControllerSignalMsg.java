package DataStructures;
/**
 * This class holds the data for controller signal information
 * @author Dwight
 *
 */
public class ControllerSignalMsg implements ControllerSignalIntf {
	/*
	 * The ID of the controller sending a message
	 */
	private int controllerId;
	
	/*
	 * The button pressed on the controller sending the message
	 * Can be 1 (up), 2 (down), 3 (left), 4 (right), 5 (center)
	 */
	private int buttonPressed;
	
	/*
	 * The controller to ping if required
	 * Useful for diagnostics. Will be negative number if not in use
	 */
	private int controllerToPing;
	
	/*
	 * Put any other data that could be useful here
	 * e.g. Time of button press??
	 */
	
	/**
	 * Obtain the controller ID for a contoller
	 */
	@Override
	public int getControllerId() {
		return this.controllerId;
	}
	
	/**
	 * Get the controller button pressed for the given
	 * controller
	 */
	@Override
	public int getControllerButtonPressed() {
		return this.buttonPressed;
	}
	
	/**
	 * Set the ID of the controller whose status needs to be
	 * enquired about
	 */
	@Override
	public void setControllerToPing(int controllerId) {
		this.controllerToPing = controllerId;
		return;
	}
	
	/**
	 * Get the ID of the controller whose status needs to be
	 * enquired about
	 */
	@Override
	public int getControllerToPing() {
		return this.controllerToPing;
	}
}
