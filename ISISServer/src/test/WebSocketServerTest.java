package test;

import java.io.IOException;
import java.util.logging.Level;

import webSock.WebSocketServer;

public class WebSocketServerTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		// Create a WebSocketServer
		WebSocketServer server = new WebSocketServer();

		// Log the initialization of the embedded server
		java.util.logging.Logger.getAnonymousLogger().log(
				Level.INFO, "Time: " + new java.util.Date() + ", " + 
				"Created new embedded WebSocket server");

		// Print the status of the embedded server
		System.out.println("Status of the embedded WebSocket server: " + 
							server.getStatusOfEmbeddedServer());
		
		// Print the deployer for the embedded server
		//System.out.println();
		
		// Print the list of applications deployed on the embedded server
		System.out.println("Deployed applications on the embedded WebSocket server: " + 
							server.getDeployedApplications());

		while(true)
		{
			System.out.println("Press any key and hit enter to exit the WebSocketServer Test...");
			try {
				if(System.in.read() > 0) {
					// Shut down the WebSocketServer
					server.shutDownEmbeddedServer();

					// Log the shutdown of the embedded server
					java.util.logging.Logger.getAnonymousLogger().log(
							Level.INFO, "Time: " + new java.util.Date() + ", " + 
							"Shut down the embedded WebSocket server");

					// Indicate the end of the test
					System.out.println("END OF TEST");

					// Exit with success status
					System.exit(0);
				}
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
