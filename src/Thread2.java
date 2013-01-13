
public class Thread2 implements Runnable {

	public Thread2() {
		
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		/*
		 * Listen in on the controller signals buffer and obtain the
		 * signals obtained there
		 * 
		 * Translate the signals found there into a form that will be
		 * understood by the browser application (specifically the ISIS
		 * buffer)
		 * 
		 * Send the translated signals to the ISIS browser app area (buffer)
		 * This buffer can be accessed by the currently running ISIS application
		 * to obtain the signals and take appropriate action
		 */
		for(int i = 0; i < 100; i++) {
			for(int j = 0; j < 10000; j++) {
				;
			}
			System.out.println("Thread2 running");
		}
	}

}
