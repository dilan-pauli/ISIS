package webSock;

/**
 * Thread to monitor WebSocket outgoing queue and send items on that queue to clients via WebSocket
 * @author Dwight
 */
public class Sender implements Runnable {

	/**
	 * Outgoing queue for WebSocket (items on here are monitored and sent to clients)
	 */
	private WebSocketOutgoingQueueInterface wsOutQueue;

	/**
	 * WebSocket application
	 */
	private ISISServerApplication isisApp;

	/**
	 * Class constructor
	 * @param wsOutQ
	 */
	public Sender(WebSocketOutgoingQueueInterface wsOutQ, ISISServerApplication isisApp) {
		this.wsOutQueue = wsOutQ;
		this.isisApp = isisApp;
	}

	@Override
	public void run() {
		while(true) {
			// TODO WRITE THE TASK RUN BY THE THREAD (LOOP AS LONG AS THE PROGRAM HAS NOT EXITED)
			System.out.println("Time: " + new java.util.Date() + ", WS SENDER THREAD");
			try {
				this.wait(10*1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// TODO THIS TASK IS THE USEFUL THING THIS THREAD DOES
			/*if(!this.wsOutQueue.isEmpty()) {
				this.isisApp.broadcast(this.wsOutQueue.removeNextItemFromOutgoingQueue().toJSONString());
			}*/
		}
	}
}
