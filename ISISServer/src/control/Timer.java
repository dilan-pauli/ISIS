package control;

import com.rapplogic.xbee.api.XBeeAddress64;

import remoteInterface.RemoteCommand;
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
	//private Controller controller;
	
	public Timer(ToRemoteInterface xbee) { //, Controller ctrl) {
		this.toXBeeNetworkQ = xbee;
		//this.controller = ctrl;
	}

	@Override
	public void run() {
		// Data to put on XBee outgoing queue
		RemoteCommand discover;
		
		while(true) {
			// TODO WRITE THE TASK RUN BY THE THREAD (LOOP AS LONG AS THE PROGRAM HAS NOT EXITED)
			System.out.println("Time: " + new java.util.Date() + ", TIMER THREAD");
			
			// Create command to send to XBee network
			discover = new XBeeCommand(XBeeAddress64.BROADCAST, 0);
			this.toXBeeNetworkQ.sendDataToRemote(discover);
			
			try {
				Thread.sleep(20*1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
