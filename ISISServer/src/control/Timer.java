package control;

/**
 * Timer thread sleeps and periodically wakes up to put XBee packet commands on the XBee 
 * Object’s Outgoing queue to check whether controllers are present and updates the XBee state list 
 * accordingly
 * 
 * Not sure if this class will be needed prehaps we could implement its functions within
 * Thread2....
 * 
 * 
 */
public class Timer implements Runnable {
	
	private Controller controller;
	
	public Timer() { // (Controller controller) {
		//this.controller = controller;
	}

	@Override
	public void run() {
		while(true) {
			// TODO WRITE THE TASK RUN BY THE THREAD (LOOP AS LONG AS THE PROGRAM HAS NOT EXITED)
			System.out.println("Time: " + new java.util.Date() + ", TIMER THREAD");
			try {
				this.wait(18*1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
