package control;

/**
 * Timer thread sleeps and periodically wakes up to put XBee packet commands on the XBee 
 * Object’s Outgoing queue to check whether controllers are present and updates the XBee state list 
 * accordingly
 */
public class Timer implements Runnable {
	public Timer() {
		;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
}
