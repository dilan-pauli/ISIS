package test;

import java.util.Timer;
import java.util.TimerTask;

import com.rapplogic.xbee.api.ApiId;
import com.rapplogic.xbee.api.PacketListener;
import com.rapplogic.xbee.api.RemoteAtRequest;
import com.rapplogic.xbee.api.RemoteAtResponse;
import com.rapplogic.xbee.api.XBee;
import com.rapplogic.xbee.api.XBeeAddress64;
import com.rapplogic.xbee.api.XBeeException;
import com.rapplogic.xbee.api.XBeeResponse;
import com.rapplogic.xbee.api.XBeeTimeoutException;
import com.rapplogic.xbee.api.zigbee.ZNetRxIoSampleResponse;

public class XBeeTest {
/**
 * This will just be a simple test. To get the I/O information from the remote Device.
 * @throws XBeeException 
 */
	private XBee xbee;
	private Timer timer;
	private XBeeAddress64 addr;
	
	public XBeeTest()
	{
		//Create the xbee and the timer for toggling the feedback light
		XBee xbee = new XBee();
		timer = new Timer();
		
		//Open the connection
		try {
			xbee.open("COM3", 9600);
		} catch (XBeeException e) {
			System.out.println("Error opening the COM Connection...");
			System.out.println(e.getCause());
			e.printStackTrace();
		}
		
		System.out.println("Serial connection established...");
	}
	
	/**
	 * Create the packet listener that should pick up on all incomming packets.
	 */
	public void createListener()
	{
		System.out.println("Waiting for the IO Packet...");
		xbee.addPacketListener(new PacketListener() {
			
		public void processResponse(XBeeResponse IOpacket) {
			//If the packet is an IO sample then check the status of the ports.
			if (IOpacket.getApiId() == ApiId.ZNET_IO_SAMPLE_RESPONSE) 
			{
		        ZNetRxIoSampleResponse ioSample = (ZNetRxIoSampleResponse) IOpacket;
		        addr = ioSample.getRemoteAddress64();
		        sendAckLight(1);
		        timer.schedule(new AckOff(), 1000);
		        
		        System.out.println("Received a sample from " + ioSample.getRemoteAddress64());
		        System.out.println("Digital D0 (pin 20) is " + (ioSample.isD0On() ? "on" : "off"));
		        System.out.println("Digital D1 (pin 19) is " + (ioSample.isD1On() ? "on" : "off"));
		        System.out.println("Digital D2 (pin 18) is " + (ioSample.isD2On() ? "on" : "off"));
		        System.out.println("Digital D3 (pin 17) is " + (ioSample.isD3On() ? "on" : "off"));
		        System.out.println("Digital D12 (pin 4) is " + (ioSample.isD12On() ? "on" : "off"));
				System.out.println("Waiting for the IO Packet...");
			}
		}		
		});
	}
	
	public void sendAckLight(int level)
	{
		try {
			RemoteAtRequest request = new RemoteAtRequest(addr, "D3", new int[] {level});
			
			RemoteAtResponse response = (RemoteAtResponse) xbee.sendSynchronous(request, 10000);
			
			if (response.isOk())
				System.out.println("Light On");
			else
				throw new RuntimeException("failed to turn on pin D3.  status is " + response.getStatus());
		}
		catch (XBeeTimeoutException e){
			System.out.println("request timed out. make sure you remote XBee is configured and powered on");
		}
		catch (Exception e){
			System.out.println("unexpected error" + e.toString());
		}		
	}
	
	public class AckOff extends TimerTask
	{
		public void run()
		{
			sendAckLight(0);
		}
	}

	public static void main(String args[]) throws XBeeException
	{
		new XBeeTest().createListener();
		
		System.out.println("Type quit to exit...");
		for(;;)
			if(System.console().readLine() == "quit")
				System.exit(0);
		
	}
}
