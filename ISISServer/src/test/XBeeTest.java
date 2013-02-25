package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.rapplogic.xbee.api.ApiId;
import com.rapplogic.xbee.api.PacketListener;
import com.rapplogic.xbee.api.RemoteAtRequest;
import com.rapplogic.xbee.api.RemoteAtResponse;
import com.rapplogic.xbee.api.XBee;
import com.rapplogic.xbee.api.XBeeAddress64;
import com.rapplogic.xbee.api.XBeeException;
import com.rapplogic.xbee.api.XBeeResponse;
import com.rapplogic.xbee.api.zigbee.ZBForceSampleRequest;
import com.rapplogic.xbee.api.zigbee.ZNetRxIoSampleResponse;
import com.rapplogic.xbee.examples.RemoteAtExample;

public class XBeeTest {
/**
 * This will just be a simple test. To get the I/O information from the remote Device.
 * @throws XBeeException 
 */
	private final static Logger log = Logger.getLogger(RemoteAtExample.class);
	
	private XBee xbee;
	//private Boolean on;
	private Timer t;
	private XBeeAddress64 addr;
	
	public XBeeTest()
	{
		//Create the xbee and the timer for toggling the feedback light
		xbee = new XBee();
		//on = false;
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
		t = new Timer();
	}
	
	public void sendAckLight(XBeeAddress64 addr)
	{
		//Turn the feedback light on
		try {
			RemoteAtRequest request = new RemoteAtRequest(addr, "D4", new int[] {5});
			xbee.sendAsynchronous(request);

		} catch (Exception e) {
			log.error("unexpected error", e);	
		}
		
		//wait .5 sec and turn it off.
		t.schedule(new AckOff(addr), 500);
		
	}
	
	public class AckOff extends TimerTask
	{
		private XBeeAddress64 addr;
		
		public AckOff(XBeeAddress64 addr){	this.addr = addr;	}
		
		public void run()
		{
			try {
				RemoteAtRequest request = new RemoteAtRequest(this.addr, "D4", new int[] {0});
				xbee.sendAsynchronous(request);

			} catch (Exception e) {
				log.error("unexpected error", e);	
			}
		}
	}
	
	public class XBeeListener implements PacketListener
	{
		private void action(ZNetRxIoSampleResponse ioSample)
		{
	        System.out.println("Received a sample from " + ioSample.getRemoteAddress64());
	        System.out.println("Digital D0 (pin 20) is " + (ioSample.isD0On() ? "on" : "off"));
	        System.out.println("Digital D1 (pin 19) is " + (ioSample.isD1On() ? "on" : "off"));
	        System.out.println("Digital D2 (pin 18) is " + (ioSample.isD2On() ? "on" : "off"));
	        System.out.println("Digital D3 (pin 17) is " + (ioSample.isD3On() ? "on" : "off"));
	        System.out.println("Digital D12 (pin 4) is " + (ioSample.isD12On() ? "on" : "off"));
			System.out.println("Waiting for the IO Packet...");
		}
		@Override
		public void processResponse(XBeeResponse response) 
		{
			//If the packet is an IO sample then check the status of the ports.
			if (response.getApiId() == ApiId.ZNET_IO_SAMPLE_RESPONSE ) 
			{
		        ZNetRxIoSampleResponse ioSample = (ZNetRxIoSampleResponse) response;
		        addr = ioSample.getRemoteAddress64();
		        sendAckLight(ioSample.getRemoteAddress64());

		        action(ioSample);
			}
			else if(response.getApiId() == ApiId.REMOTE_AT_RESPONSE)
			{
				RemoteAtResponse remoteAt = (RemoteAtResponse) response;
				if(remoteAt.getCommand().equals("IS"))
				{
					ZNetRxIoSampleResponse ioSample = null;
				
					if (remoteAt.isOk())  
					{
						// extract the i/o sample
						try {
							ioSample = ZNetRxIoSampleResponse.parseIsSample(remoteAt);
						} catch (IOException e) {
							e.printStackTrace();
						}
					} 
					else
						log.info("Remote AT request failed: " + response);
					ioSample.setRemoteAddress64(remoteAt.getRemoteAddress64());
					action(ioSample);
				}
			}
		}
	}
	
	public void force()
	{
		try {
			xbee.sendAsynchronous(new ZBForceSampleRequest(XBeeAddress64.BROADCAST));
		} catch (XBeeException e) {
			e.printStackTrace();
			log.error("XBeeSendingThread: Error in sending the Force sample");
		}
	}

	public static void main(String args[]) throws XBeeException
	{
		XBeeTest handler = new XBeeTest();
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String com = null;
		for(;;)
		{
			//Ask the user what COM port to read the coordinator.
			
			System.out.println("ehhhh buddyyyy see if you can force a sample eh?");
			try {
				com = br.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(com.equals("yeah"))
			{
				handler.force();
			}
			else
				System.out.println("ehh broh you have to give me a yeah eh?");
			
		}


	}
}
