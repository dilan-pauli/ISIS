package remoteInterface;

/**
 * This class will be used to provide a standard interface for the controller threads
 * to communicate with the remote network devices. Ideally the thread should only 
 * need to see if there are currently waiting messages and then access and remove 
 * them.
 * 
 * @author noquarter
 *
 */
public interface FromRemoteInterface {
	
	/**
	 * Remove a message from the remoteNetworks Incoming Queue
	 * @return a remote data that has the address and states of the remote device.
	 */
	public RemoteData getRemoteMessage();
	
	/**
	 * Check to see id the queue has messages to take off.
	 * @return Ture or false if none
	 */
	public boolean hasMessages();

}
