package remoteInterface;

import org.json.simple.JSONObject;
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
	
	public int[] getAddress();
	
	public boolean getDigital(int index);
	 
	public int getAnalog(int index);
	
	public JSONObject toJSONString(int level);

}
