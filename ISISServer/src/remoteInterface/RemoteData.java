package remoteInterface;
/**
 *  This is the interface that will be used to handle data on the controller and thread side.
 *  It is set up so that even though the XBee network maybe swapped out or a new network added 
 *  the data types should still be consistent. It implements the essential devices for the remote
 *  network data types. Address, DIO, and possibly analog IO.
 *  
 * @author Dilan
 *
 */
public interface RemoteData {
	
	/**
	 * Obtain the button pin voltages
	 */
	public double[] getButtonPinVoltages();
	
	/**
	 * Obtain the state of the button IO pins (UP,DOWN,LEFT,RIGHT,CENTER) => true means pressed
	 */
	public boolean[] getButtonIOStates();
	
	/**
	 * Obtain the ON state of the device
	 */
	public boolean isOn();
	
	/**
	 * Obtain the device ID (physical address)
	 */
	public String getControllerID();
	
	/**
	 * Set the controller ID (physical address) for the device
	 */
	public void setControllerID(String id);
	
	/**
	 * Set the on state of the device (true if the device is on)
	 */
	public void setOnState(boolean newOnState);
	
	/**
	 * Set the state of the button IO pins (pressed or not pressed?)
	 * IO pins: (UP,DOWN,LEFT,RIGHT,CENTER) => true means pressed
	 * @return true if operation succeeded
	 */
	public boolean setButtonIOStates(boolean[] newIOStates);
	
	/**
	 * Set the button pin voltages
	 * @return true if operation succeeded
	 */
	public boolean setButtonPinVoltages(double[] newPinVoltages);
	
	

}
