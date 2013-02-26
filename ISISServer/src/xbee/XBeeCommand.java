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
public class XBeeCommand implements RemoteCommand
{
	protected XBeeAddress64 address;
	private boolean DIO[];
	private int analog[];
	private int commandCode;
	
	/**
	 * Constructor to create a trivial class with just an address and a code
	 * 
	 * @param address
	 */
	public XBeeCommand(XBeeAddress64 address, int code) 
	{
		this.address = address;
		if (code < 0 || code > 3 )
			throw new InvalidParameterException("Code is out of bounds");
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
		this.address = address;
		this.DIO = io;
		if (code < 0 || code > 3 )
			throw new InvalidParameterException("Code is out of bounds");
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
		this.address = address;
		this.DIO = io;
		this.analog = analog;
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
	
	/**
	 * Allow the managing thread access to the address.
	 * @return 8 spot array that contains the 64 bit address for the device.
	 */
	public int[] getAddress() {
		return this.address.getAddress();
	}
	
	/**
	 * This method is used so that the digital io field will be accessible.
	 * The max length that the index should be is 11 since there is only 11 DIO
	 * spots on the XBee.
	 * @param index number between 0-11 represents D0, D1, D2, etc.
	 * @return The boolean state of that IO port
	 */
	public boolean getDigital(int index) {
		if (index > DIO.length)
			throw new InvalidParameterException("Provided index is larger then the array size...");
		return DIO[index];
	}
	
	/**
	 * Method is used to access the analog fields of the structure.
	 * Max index length should be 5.
	 * @param index 0-5 that represents A0,A1, etc.
	 * @return The ADC value for that port as an integer.
	 */
	public int getAnalog(int index) {
		if (index > analog.length)
			throw new InvalidParameterException("Provided index is larger then the array size...");
		if(analog == null)
			throw new InvalidParameterException("Analog not avil");
		return analog[index];
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
