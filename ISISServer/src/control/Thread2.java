package control;

import java.util.HashMap;

import webSock.WebSocketIncomingQueueInterface;
import webSock.WebSocketOutgoingQueueInterface;

/**
 * Thread2 monitors the Incoming queue of the WebSocket Object. It will translate items present 
 * on this queue from JSON Objects (as specified by the Browser Request API) and query the XBee state 
 * list for the specific information that the client is after.
 */
public class Thread2 implements Runnable {
	
	private WebSocketIncomingQueueInterface wsInQueue;
	
	private WebSocketOutgoingQueueInterface wsOutQueue;
	
	private Controller controller;
	
	public Thread2(WebSocketIncomingQueueInterface wsIncomingQueue, 
			WebSocketOutgoingQueueInterface wsOutgoingQueue,
			Controller controller) {
		
		this.wsInQueue = wsIncomingQueue;
		this.wsOutQueue = wsOutgoingQueue;
		this.controller = controller;
	}

	@Override
	public void run() {
		while(true) {
			// TODO WRITE THE TASK RUN BY THE THREAD (LOOP AS LONG AS THE PROGRAM HAS NOT EXITED)
			/*
			 * If WebSocket incoming queue is not empty, remove the front item, extract the JSON, figure out
			 * what the request is, access the XBee states list to serve this request, package up the response
			 * to be sent via WebSocket, place this JSON response on the WebSocket outgoing queue
			 * 
			 * Useful methods:
			 */
			//this.wsInQueue.removeNextIncomingWebSocketMsg();
			//this.controller.
			//this.wsOutQueue.putItemOnOutgoingQueue(item);
			
			System.out.println("Time: " + new java.util.Date() + ", THREAD 2");
			try {
				this.wait(14*1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
