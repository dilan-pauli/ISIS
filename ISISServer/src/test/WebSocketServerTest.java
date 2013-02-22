package test;

import java.io.IOException;
import java.util.logging.Level;

import webSock.WebSocketServer;

//TODO: WILL NOT NEED THIS ANYMORE AFTER RESTRUCTURING CODE FOR NON-EMBEDDED GLASSFISH SERVER
public class WebSocketServerTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		// Create a WebSocketServer
		WebSocketServer server = new WebSocketServer();

		// Log the initialization of the embedded server
		java.util.logging.Logger.getAnonymousLogger().log(
				Level.INFO, "Time: " + new java.util.Date() + ", " + 
				"Created new embedded WebSocket server");

		// Print the status of the embedded server
		System.out.println("Time: " + new java.util.Date() + 
				", Status of the embedded WebSocket server: " + server.getStatusOfEmbeddedServer());
		
		// Print the deployer for the embedded server
		//System.out.println();
		
		// Print the list of applications deployed on the embedded server
		System.out.println("Time: " + new java.util.Date() + 
				", Deployed applications on the embedded WebSocket server: " + server.getDeployedApplications());

		while(true) {
			System.out.println("Time: " + new java.util.Date() + 
					", Press any key and hit enter to exit the WebSocketServer Test...");
			try {
				if(System.in.read() > 0) {
					// Shut down the WebSocketServer
					server.shutDownEmbeddedServer();

					// Log the shutdown of the embedded server
					java.util.logging.Logger.getAnonymousLogger().log(
							Level.INFO, "Time: " + new java.util.Date() + ", " + 
							"Shut down the embedded WebSocket server");

					// Indicate the end of the test
					System.out.println("Time: " + new java.util.Date() + ", END OF TEST");

					// Exit with success status
					System.exit(0);
				}
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			catch (Exception excp) {
				excp.printStackTrace();
			}
		}
	}
}
