package control;

import java.io.IOException;

import webSock.WebSocketServer;
import xbee.XBeeHandler;

public class Main {
	//TODO: NO LONGER NEED THIS. THIS IS NOW ESSENTIALLY DONE BY ISISServerApplication UPON DEPLOYMENT
	public static void main(String args[]) throws IOException
	{
		System.out.println("Time: " + new java.util.Date() + ", Starting ISIS Program");

		XBeeHandler handler = new XBeeHandler();
		System.out.println("Time: " + new java.util.Date() + ", Created Xbee Handler");

		WebSocketServer server = new WebSocketServer();
		System.out.println("Time: " + new java.util.Date() + ", Created WebSocket Handler");

		//@SuppressWarnings("unused")
		//Controller newController = new Controller(handler, server);
		//System.out.println("Time: " + new java.util.Date() + ", Created Controller");

		while(true)
		{
			System.out.println("Press anykey to exit ISIS...");
			if(System.in.read() > 0){
				// Dispose of the WebSocket server and all its resources
				server.shutDownEmbeddedServer();
				// End the program
				System.exit(0);
			}
		}
	}
}
