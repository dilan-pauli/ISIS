package control;

import java.util.HashMap;
import java.util.logging.Level;

import com.rapplogic.xbee.api.XBeeAddress64;

import remoteInterface.RemoteCommand;
import remoteInterface.RemoteData;
import remoteInterface.ToRemoteInterface;
import xbee.XBeeCommand;

/**
 * Timer thread sleeps and periodically wakes up to put XBee packet commands on the XBee 
 * Object Outgoing queue to check whether controllers are present and updates the XBee state list 
 * accordingly
 * 
 * Not sure if this class will be needed perhaps we could implement its functions within
 * Thread2....
 * 
 * 
 */
public class Timer implements Runnable {
	
	private ToRemoteInterface toXBeeNetworkQ;
	
	private HashMap<String, RemoteData> remoteList;
	//private Controller controller;
	
	public Timer(ToRemoteInterface xbee, HashMap<String, RemoteData> remoteStateList) { //, Controller ctrl) {
		this.toXBeeNetworkQ = xbee;
		remoteList = remoteStateList;
		//this.controller = ctrl;
	}

	@Override
	public void run() {
		// Data to put on XBee outgoing queue
		RemoteCommand discover;
		
		while(true) {
			java.util.logging.Logger.getAnonymousLogger().log(
					Level.INFO, "Time: " + new java.util.Date() + ", TIMER THREAD");
			
			// Create command to send to XBee network
			discover = new XBeeCommand(XBeeAddress64.BROADCAST, 0);
			this.toXBeeNetworkQ.sendDataToRemote(discover);
			
			//Wait for the devices to check in...
			try {
				Thread.sleep(500);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			
			//Check if any of the devices is off
			for(RemoteData data : remoteList.values())
			{
				//If the device hasn't reported any information in the last 15 sec it is
				//considered to be OFF.
				if(data.getTimeStamp().getTime() < new java.util.Date().getTime() - 15*1000)
				{
					data.setOnState(false);
					double[] newButtonVoltages = {0, 0, 0, 0, 0};
					data.setButtonPinVoltages(newButtonVoltages);
				}
			}
			
			try {
				Thread.sleep(15*1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
