package xbee;

import java.security.InvalidParameterException;

import com.rapplogic.xbee.api.XBeeAddress64;

import remoteInterface.RemoteCommand;

/**
 *  This class extends the XBee packet and adds a command code so that 
 *  the sending thread will know what AT command to send to the XBees.
 *  
 * @author noquarter
 *
 */
public class XBeeCommand extends XBeePacket implements RemoteCommand
{
	private int commandCode;
	
	/**
	 * Constructor to create a trivial class with just an address and a code
	 * 
	 * @param address
	 */
	public XBeeCommand(XBeeAddress64 address, int code) 
	{
		super(address);
		commandCode = code;
	}
	
	/**
	 * Constructor to create a data set with a address and a preset 
	 * array of digital IO signals. Also a command code.
	 * 
	 * @param address
	 * @param io
	 */
	public XBeeCommand(XBeeAddress64 address, boolean[] io, int code)
	{
		super(address, io);
		commandCode = code;
	}
	
	/**
	 * Full constructor to set all of the data fields and a command code.
	 * 
	 * @param address
	 * @param io
	 * @param analog
	 */
	public XBeeCommand(XBeeAddress64 address, boolean[] io, int[] analog, int code)
	{
		super(address, io, analog);
		commandCode = code;
	}

	/**
	 * This function is used to set the address of the command packet.
	 * Use a broadcast address to send the packet to all nodes in the network.
	 * It will set the default command to 0 which is the IO ping.
	 * 
	 * @param addr this should be of type XBeeAddress64
	 * 
	 */
	@Override
	public void setAddress(Object addr) {
		this.address = (XBeeAddress64) addr;
		this.commandCode = 0;
	}
	
	/**
	 * Used to set the command code.
	 * 0 = Ping to see information.
	 * ??1 = set the io states in the class to the device.??
	 * ??2 = Start a node discovery?? 
	 * @param code int 0 or 1 to make a function.
	 */
	@Override
	public void setCommandCode(int code) {
		
		if (code < 0 || code > 3 )
			throw new InvalidParameterException("Code is out of bounds");
		
		this.commandCode = code;
		
	}
	
	public int getCommandCode()
	{
		return commandCode;
	}
	
	public XBeeAddress64 getXBeeAddress()
	{
		return this.address;
	}

}
