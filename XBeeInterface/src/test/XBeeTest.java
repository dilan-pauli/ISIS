package test;

import com.rapplogic.xbee.api.ApiId;
import com.rapplogic.xbee.api.XBee;
import com.rapplogic.xbee.api.XBeeException;
import com.rapplogic.xbee.api.XBeeResponse;
import com.rapplogic.xbee.api.zigbee.ZNetRxIoSampleResponse;

public class XBeeTest {
/**
 * This will just be a simple test. To get the I/O information from the remote Device.
 * @throws XBeeException 
 */

	public static void main(String args[]) throws XBeeException
	{
		XBee xbee = new XBee();
		XBeeResponse IOpacket;
		
		try {
			xbee.open("COM3", 9600);
		} catch (XBeeException e) {
			System.out.println("Error opening the COM Connection...");
			System.out.println(e.getCause());
			e.printStackTrace();
		}
		
		System.out.println("Serial connection established...");
		
		for(;;)
		{
			//Blocks here for the packet.
			System.out.println("Waiting for the IO Packet...");
			IOpacket = xbee.getResponse();
			
			//If the packet is an IO sample then check the status of the ports.
			if (IOpacket.getApiId() == ApiId.ZNET_IO_SAMPLE_RESPONSE) 
			{
		        ZNetRxIoSampleResponse ioSample = (ZNetRxIoSampleResponse) IOpacket;
		        
		        System.out.println("Received a sample from " + ioSample.getRemoteAddress64());

		        System.out.println("Digital D0 (pin 20) is " + (ioSample.isD0On() ? "on" : "off"));
		        System.out.println("Digital D1 (pin 19) is " + (ioSample.isD1On() ? "on" : "off"));
		        System.out.println("Digital D2 (pin 18) is " + (ioSample.isD2On() ? "on" : "off"));
		        System.out.println("Digital D3 (pin 17) is " + (ioSample.isD3On() ? "on" : "off"));
		        System.out.println("Digital D12 (pin 4) is " + (ioSample.isD12On() ? "on" : "off"));
			}
			
		}
		
		
	}
}
