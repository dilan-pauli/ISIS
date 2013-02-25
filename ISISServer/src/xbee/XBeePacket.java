package xbee;

import java.security.InvalidParameterException;

import org.json.simple.JSONObject;

import remoteInterface.RemoteData;

import com.rapplogic.xbee.api.XBeeAddress64;
/**
 * This class will be a data structure that will be created when the system receives a 
 * data packet from the Xbee network. It uses the Remote Data interface to allow 
 * for the standard access of data if more different types of remote are used.
 * 
 * DIO corresponds with the following:
 * 0 = UP
 * 1 = LEFT
 * 2 = RIGHT
 * 3 = DOWN
 * 4 = CENTER
 * 
 * @author Dilan
 *
 */
public class XBeePacket implements RemoteData
{
	
	protected XBeeAddress64 address;
	private boolean DIO[];
	private int analog[];
	
	/**
	 * Constructor to create a trivial class with just an address.
	 * 
	 * @param address
	 */
	public XBeePacket(XBeeAddress64 address)
	{
		this.address = address;
		this.DIO = new boolean[11];
		this.analog = new int[5];
	}
	
	/**
	 * Constructor to create a data set with a address and a preset 
	 * array of digital IO signals.
	 * 
	 * @param address
	 * @param io
	 */
	public XBeePacket(XBeeAddress64 address, boolean[] io)
	{
		this.address = address;
		this.DIO = io;
		this.analog = null;
	}
	
	/**
	 * Full constructor to set all of the data fields.
	 * 
	 * @param address
	 * @param io
	 * @param analog
	 */
	public XBeePacket(XBeeAddress64 address, boolean[] io, int[] analog)
	{
		this.address = address;
		this.DIO = io;
		this.analog = analog;
	}

	/**
	 * Allow the managing thread access to the address.
	 * @return 8 spot array that contains the 64 bit address for the device.
	 */
	@Override
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
	@Override
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
	@Override
	public int getAnalog(int index) {
		if (index > analog.length)
			throw new InvalidParameterException("Provided index is larger then the array size...");
		if(analog == null)
			throw new InvalidParameterException("Analog not avil");
		return analog[index];
	}
	
	/**
	 * This function will take the private data and package it up into a JSON
	 * object so that it can be sent over the Internet.
	 * @param level 0 is all of the data, 1 is the address, 2 is the DIO data and address, 3 is the address and the analog data.
	 * @return JSON Object
	 */
	@Override
	public JSONObject toJSONString(int level) {
		if (level > 3 || level < 0)
			throw new InvalidParameterException("Provided level is not between 0 and 3");
		
		//TODO - Add JSON creation.
		switch(level)
		{
			case 0:
			case 1:
			case 2:
			case 3:
		}
		return null;
	}
	
}
