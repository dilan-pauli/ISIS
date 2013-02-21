package control;

import java.util.Queue;

import datastucts.XBeePacket;

/** 
 * Thread1 monitors the incoming queue of the XbeeHandler, translate items present on this queue 
 * from XBee packets to JSON Objects which are understood by browser clients (as specified by the Browser 
 * Response API), and then places these translated items on the Outgoing queue of the WebSocket Object 
 */
public class Thread1 implements Runnable {
	
	private Queue<XBeePacket> toXBeeNetworkQ;
	
	private Queue<XBeePacket> fromXBeeNetworkQ;
	
	private Controller controller;
	
	public Thread1() { //(Queue<XBeePacket> toXBeeNetworkQueue, Queue<XBeePacket> fromXBeeNetworkQueue, Controller controller) {
		
		//this.toXBeeNetworkQ = toXBeeNetworkQueue;
		//this.fromXBeeNetworkQ = fromXBeeNetworkQueue;
		//this.controller = controller;
	}

	@Override
	public void run() {
		while(true) {
			// TODO WRITE THE TASK RUN BY THE THREAD (LOOP AS LONG AS THE PROGRAM HAS NOT EXITED)
			System.out.println("Time: " + new java.util.Date() + ", THREAD 1");
			try {
				this.wait(10*1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
