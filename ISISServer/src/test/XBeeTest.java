package test;

import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.rapplogic.xbee.api.ApiId;
import com.rapplogic.xbee.api.PacketListener;
import com.rapplogic.xbee.api.RemoteAtRequest;
import com.rapplogic.xbee.api.XBee;
import com.rapplogic.xbee.api.XBeeAddress64;
import com.rapplogic.xbee.api.XBeeException;
import com.rapplogic.xbee.api.XBeeResponse;
import com.rapplogic.xbee.api.zigbee.ZNetRxIoSampleResponse;
import com.rapplogic.xbee.examples.RemoteAtExample;

public class XBeeTest {
/**
 * This will just be a simple test. To get the I/O information from the remote Device.
 * @throws XBeeException 
 */
	private final static Logger log = Logger.getLogger(RemoteAtExample.class);
	
	private XBee xbee;
	private XBeeAddress64 addr;
	private Boolean on;
	
	public XBeeTest()
	{
		//Create the xbee and the timer for toggling the feedback light
		xbee = new XBee();
		addr = new XBeeAddress64(0, 0x13, 0xa2, 0, 0x40, 0x79, 0xb4, 0x57);
		on = false;
		//Open the connection
		try {
			xbee.open("COM3", 9600);

		} catch (XBeeException e) {
			System.out.println("Error opening the COM Connection or error with the listener");
			System.out.println(e.getCause());
			e.printStackTrace();
		}
		
		System.out.println("Serial connection established...");
		
		
		System.out.println("Waiting for the IO Packet...");
		xbee.addPacketListener(new XBeeListener());
	}
	
	public void sendAckLight(int level)
	{
		
		try {
			RemoteAtRequest request = new RemoteAtRequest(addr, "D4", new int[] {level});
			xbee.sendAsynchronous(request);

		} catch (Exception e) {
			log.error("unexpected error", e);	
		}
	}
	
	public class AckOff extends TimerTask
	{
		public void run()
		{
			sendAckLight(0);
		}
	}
	
	public class XBeeListener implements PacketListener
	{
		@Override
		public void processResponse(XBeeResponse response) 
		{
			//If the packet is an IO sample then check the status of the ports.
			if (response.getApiId() == ApiId.ZNET_IO_SAMPLE_RESPONSE) 
			{
		        ZNetRxIoSampleResponse ioSample = (ZNetRxIoSampleResponse) response;
		        addr = ioSample.getRemoteAddress64();
		        if(on == false)
		        {
		        	sendAckLight(5);
		        	on = true;
		        }
		        else
		        {
		        	sendAckLight(0);
		        	on = false;
		        }
		        System.out.println("Received a sample from " + ioSample.getRemoteAddress64());
		        System.out.println("Digital D0 (pin 20) is " + (ioSample.isD0On() ? "on" : "off"));
		        System.out.println("Digital D1 (pin 19) is " + (ioSample.isD1On() ? "on" : "off"));
		        System.out.println("Digital D2 (pin 18) is " + (ioSample.isD2On() ? "on" : "off"));
		        System.out.println("Digital D3 (pin 17) is " + (ioSample.isD3On() ? "on" : "off"));
		        System.out.println("Digital D12 (pin 4) is " + (ioSample.isD12On() ? "on" : "off"));
				System.out.println("Waiting for the IO Packet...");
			}				
		}
	}

	public static void main(String args[]) throws XBeeException
	{
		XBeeTest handler = new XBeeTest();
		for(;;)
		{
			
		}


	}
}
