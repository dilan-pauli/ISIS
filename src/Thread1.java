
public class Thread1 implements Runnable {

	public Thread1() {
		
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		/* 
		 * Listen in on the USB for incoming data and place it in the
		 * buffer holding controller signals
		 */
		for(int i = 0; i < 100; i++) {
			for(int j = 0; j < 10000; j++) {
				;
			}
			System.out.println("Thread1 running");
		}
	}

}
