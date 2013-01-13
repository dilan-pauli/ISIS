
public class Thread3 implements Runnable {

	public Thread3() {
		
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		/*
		 * Listen for error handling or diagnostic request from
		 * browser
		 * The error request signal will be sent from the server process
		 * via USB to the Xbee coordinator that will ping the required
		 * controller
		 */
		for(int i = 0; i < 100; i++) {
			for(int j = 0; j < 10000; j++) {
				;
			}
			System.out.println("Thread3 running");
		}
	}

}
