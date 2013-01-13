

import WebSocketClasses.ISISServerApplication;
//import java.io.*;
//import java.util.ArrayList;

public class ServerProcessMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Application starting...");
		
		/*
		 * Set up required variables, objects, etc
		 * 	- WebSocket connection
		 * 	- Buffer to hold controller signals from USB
		 */
		// Declare the buffer of controller signals
		//TODO
		//ArrayList<ControllerSignal> signalBuffer;
		//signalBuffer = new ArrayList();
		
		// Create an ISISServerApplication object
		//TODO
		@SuppressWarnings("unused")
		ISISServerApplication appl = new ISISServerApplication();
		
		/*
		 * Start the application threads
		 */
		/*System.out.println("Starting the threads...");
		
		Thread1 t1 = new Thread1();
		new Thread(t1).start();
		
		Thread2 t2 = new Thread2();
		new Thread(t2).start();
		
		Thread3 t3 = new Thread3();
		new Thread(t3).start();
		
		System.out.println("Finished starting the threads");
		System.out.println("Enter \"Q\" to exit the application at any time");*/
		
		
		/*
		 * Set up reader for keyboard input
		 */
		/*InputStreamReader iStreamReader = new InputStreamReader(System.in);
		BufferedReader bufRead = new BufferedReader(iStreamReader);*/
		/* 
		 * Loop infinitely exiting the application and therefore killing
		 * the child threads when the quit signal "Q" is given
		 */
		/*boolean quit = false;
		String cmd;
		while(quit == false) {
			try {
				// Obtain a command from the command line
				//System.out.println("If you want to exit, enter \"Q\"");//TEST
				cmd = bufRead.readLine();
				//System.out.println("Entered command: " + cmd);//TEST
				
				// Take action on the given command
				if(cmd.equals("Q")) {
					quit = true;
				}
			}
			catch (IOException err) {
				System.out.println("Error reading line");
			}
		}*/
		System.out.println("Terminating the application...");
		System.exit(0);
	}

}
