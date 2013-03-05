package xbee;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.InvalidParameterException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import remoteInterface.FromRemoteInterface;
import remoteInterface.RemoteCommand;
import remoteInterface.RemoteData;
import remoteInterface.ToRemoteInterface;

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


/**
 * On creation this class will instantiate the incoming and outgoing queues that are used 
 * to buffer the messages that are received from the xbee cloud and the messages that are
 * about to be sent out. Along with the queues a XBee object is defined this object is the 
 * virtual connection to the coordinator.
 * 
 * @author noquarter
 *
 */
public class

XBeeHandler implements ToRemoteInterface, FromRemoteInterface
{
	private final static Logger log = Logger.getLogger(RemoteAtExample.class);
	private final int feedBackDelay = 500; //One half second.

	private Queue<XBeeCommand> toNetwork;
	private Queue<XBeePacket> fromNetwork;

	private XBee coordinator;
	private Thread sendingThread;
	private Timer feedBackTmr;

	/**
	 *	This is the constructor class for the XBEE communication system.
	 *	It will create the queues that the XBee uses for communication and 
	 *	init the xbee system. It will also set up the packet listener. 
	 * @throws IOException 
	 */
	public XBeeHandler() throws IOException
	{
		toNetwork = new LinkedList<XBeeCommand>();
		fromNetwork = new LinkedList<XBeePacket>();
		coordinator = new XBee();
		feedBackTmr = new Timer();

		//Ask the user what COM port to read the coordinator.
		/*BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Enter the com port that the Coordinator is residing on. E.g 'COM3'");
		String com = br.readLine();*/
		String com = "COM3";	// TODO: CHANGE THIS AS REQUIRED

		//Open the connection to the XBee device.
		try {
			coordinator.open(com, 9600);
		} catch (XBeeException e) { 
			System.out.println("Error: Opening XBee coordinator connection on specified serial port...");
			e.printStackTrace();
		}

		//Add the custom Listener to the XBee
		coordinator.addPacketListener(new XBeeListener());

		//Create and start the sending Thread
		sendingThread = new Thread(new sendingThread());
		sendingThread.start();
	}


	/**
	 * This method will be called when a packet is received it will blink the
	 * feed back light on for about a half second. it does this by setting a 
	 * timer on a new thread to wake up and turn the light off. This method 
	 * uses the the timer and the Lightoff class to turn the light off. In sort
	 * this method will blink the XBee devices feedback light. THe length of time
	 * that the light is on will be determined by the feedbackdelay constant.
	 * 
	 * @param addr The address of the XBee device whose light is being flashed.
	 * 
	 */
	private void sendAckLight(XBeeAddress64 addr)
	{
		//Turn the light on
		try {
			RemoteAtRequest request = new RemoteAtRequest(addr, "D4", new int[] {5});
			coordinator.sendAsynchronous(request);

		} catch (Exception e) {
			log.error("unexpected error", e);	
		}
		//Set timer to call the command to turn off the feedback light after a half second.
		this.feedBackTmr.schedule(new LightOff(addr), this.feedBackDelay);
	}

	/**
	 * This class is the action for the timer in the Light off method.
	 * The action that it will perform is to send a command to an address to 
	 * turn off the feedback light.
	 * @author noquarter
	 */
	private class LightOff extends TimerTask
	{
		private XBeeAddress64 addr;

		public LightOff(XBeeAddress64 addr){	this.addr = addr;	}

		//Action is to turn off the feedback light.
		public void run()
		{
			try {
				RemoteAtRequest request = new RemoteAtRequest(this.addr, "D4", new int[] {0});
				coordinator.sendAsynchronous(request);
			} catch (Exception e) {
				log.error("unexpected error", e);	
			}
		}
	}

	/**
	 * This class is used to catch the packets as they come in form the Xbee network.
	 * if they are IO packets the feedback light will blink on and then the packet 
	 * will be created and then added to the queue.
	 * @author Dilan
	 *
	 */
	private class XBeeListener implements PacketListener
	{
		@Override
		public void processResponse(XBeeResponse response) {

			if (response.getApiId() == ApiId.ZNET_IO_SAMPLE_RESPONSE) 
			{
				ZNetRxIoSampleResponse ioSample = (ZNetRxIoSampleResponse) response;
				//Send the ack Light
				sendAckLight(ioSample.getRemoteAddress64());
				//Convert to XBeePacket & Place on the Queue
				fromNetwork.add(createXBeePacket(ioSample));
			}
			//This is for other packets
			else if(response.getApiId() == ApiId.REMOTE_AT_RESPONSE)
			{
				RemoteAtResponse remoteAt = (RemoteAtResponse) response;
				//type IS is the packet the corresponds with Force IO sample.
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

						ioSample.setRemoteAddress64(remoteAt.getRemoteAddress64());
						fromNetwork.add(createXBeePacket(ioSample));
					} 
					else
						log.info("Remote AT request failed: " + response);
				}
			}
		}
	}

	/**
	 * This is the class that will implement the sending Thread.
	 * @author noquarter
	 */
	private class sendingThread implements Runnable
	{
		@Override
		public void run() {
			//Create the run order for the sending Thread.
			while(true)
			{
				//The network is not empty
				if (!toNetwork.isEmpty())
				{
					//Get the packet to send.
					XBeeCommand toSend = toNetwork.remove();
					switch(toSend.getCommandCode())
					{
					//Just a ping to get the IO. The Return packet will show up on the fromNetworkQueue
					case 0:
						try {
							coordinator.sendAsynchronous(new ZBForceSampleRequest(toSend.getXBeeAddress()));
						} catch (XBeeException e) {
							e.printStackTrace();
							log.error("XBeeSendingThread: Error in sending the Force sample");
						}
						break;

						// FUTURE STUF TO BE ADDED 

						//This means that we want to set states.
					case 1:
						//TODO - Future add a way to manually change IO States.

						//THis means that we want to build a structure of nodes.
					case 2:
						//TODO - Node discovery...
					default:
						throw new InvalidParameterException("Switch Error: code is out of range...");
					}
				}

				//Sleep because this thread does not need to run all the time.
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				//Loop back around.
			}
		}
	}

	/**
	 * Function to change the XBee packet into a very simple form for the controller.
	 * All the controller needs is the IO and analog information and the address.
	 * 
	 * @param packet
	 * @return XBeePacket
	 */
	private XBeePacket createXBeePacket(ZNetRxIoSampleResponse packet)
	{
		boolean[] DIO = new boolean[5];
		//Fill the packet.
		// UP
		DIO[0] = !packet.isD11On();
		//LEFT
		DIO[1] = !packet.isD1On();
		//RIGHT
		DIO[2] = !packet.isD2On();
		//DOWN
		DIO[3] = !packet.isD3On();
		//CENTER
		DIO[4] = !packet.isD12On();

		XBeePacket pack = new XBeePacket();

		//Add the analog voltage info
		if(packet.containsAnalog())
		{
			double[] analog = new double[3];
			analog[0] = (double)packet.getAnalog0();
			pack.setButtonPinVoltages(analog);
		}

		pack.setButtonIOStates(DIO);
		pack.setControllerID(packet.getRemoteAddress64().toString());

		return pack;
	}

	/**
	 * If the fromNetwork Queue has a message in it that message will be returned.
	 * Else the queue is emptied and will return null.
	 */
	@Override
	public RemoteData getRemoteMessage() {
		if (fromNetwork.isEmpty() == false)
		{
			return fromNetwork.remove();
		}
		else
			return null;
	}

	/**
	 * This method can be called to see if there are any messages on the from
	 * Queue that are waiting to go out to the controller.
	 */
	@Override
	public boolean hasMessages() {
		return !fromNetwork.isEmpty();
	}


	/**
	 * This method will place a command on the ToNetwork Queue.
	 * @throws InvalidParameterException
	 */
	@Override
	public void sendDataToRemote(RemoteCommand msg) {
		//Need to do input checking.
		XBeeCommand msgToAdd = (XBeeCommand) msg;
		if(msgToAdd.address == null || msgToAdd.getCommandCode() < 0 || msgToAdd.getCommandCode() > 3)
			throw new InvalidParameterException("RemoteCode parameters are invaild...");

		toNetwork.add((XBeeCommand) msg);
	}

	/**
	 * This is the xbee test method that will be used to test the functionality of 
	 * this class to pick up IO packets and place them on the queue. It will also see if
	 * the classes sending thread will be able to send IO pings based on command packets.
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String args[]) throws IOException
	{
		//Create the initial class
		XBeeHandler handle = new XBeeHandler();
		//Send a reference to the from interface this allows only get remote message.
		FromRemoteInterface from = handle;
		//Send a reference to the toInterface. This allows the send remote data method.
		ToRemoteInterface to = handle;

		//Init a broadcast command to fill the queue with IO requests from all of the remote.
		RemoteCommand discover = new XBeeCommand(XBeeAddress64.BROADCAST, 0);

		//Put the command on the queue.
		to.sendDataToRemote(discover);

		//Wait for the queue to fill. When it does get the packets.
		while (true)
		{
			try {
				Thread.sleep(100);
			}
			catch(InterruptedException e) {
				e.printStackTrace();
			}

			if(from.hasMessages())
			{
				System.out.println("--------------------Received a message-----------------");
				XBeePacket msg = (XBeePacket) from.getRemoteMessage();
				boolean[] states = msg.getButtonIOStates();
				System.out.println("Package from: " + msg.getControllerID());
				System.out.println("Sates UP:" + states[0]);
				System.out.println("Sates LEFT:" + states[1]);
				System.out.println("Sates RIGHT:" + states[2]);
				System.out.println("Sates DOWN:" + states[3]);
				System.out.println("Sates CENTER:" + states[4]);
				double[] analog = msg.getButtonPinVoltages();
				System.out.println("ADC Voltage level: " + analog[0]);
			}
		}
	}
}
