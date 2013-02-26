package remoteInterface;

/**
 * This interface will be used to send commands to the 
 * IO network. The command object will be passed to the 
 * ToNetworkQueue. 
 * 
 * @author noquarter
 *
 */
public interface ToRemoteInterface {
	
	/**
	 * Send a RemoteCommand message to the external IO controller.
	 * @param msg
	 */
	public void sendDataToRemote(RemoteCommand msg);

}
