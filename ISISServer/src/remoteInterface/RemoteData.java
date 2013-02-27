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
	
	public int[] getButtonPinVoltages();
	
	public boolean[] getButtonIOStates();
	
	public boolean isOn();
	
	public String getControllerID();
	
	public void setControllerID(String id);
	
	public void setOnState(boolean newOnState);
	
	public boolean setButtonIOStates(boolean[] newIOStates);
	
	public boolean setButtonPinVoltages(int[] newPinVoltages);
	
	

}
