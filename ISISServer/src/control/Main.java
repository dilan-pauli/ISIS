package control;

import java.io.IOException;

import webSock.WebSocketServer;
import xbee.XBeeHandler;

public class Main {

	public static void main(String args[]) throws IOException
	{

		XBeeHandler handler = new XBeeHandler();
		WebSocketServer server = new WebSocketServer();
		Controller newController = new Controller(handler, server);
		
		while(true)
		{
			System.out.println("Press anykey to exit ISIS...");
			if(System.in.read() > 0)
				System.exit(0);
		}
	}
}
