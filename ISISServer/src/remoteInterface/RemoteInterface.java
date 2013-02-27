package remoteInterface;


public interface RemoteInterface {
	
	public RemoteData getRemoteMessage();
	
	public void sendDataToRemote(RemoteData msg);

}
