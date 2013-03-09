package xbee;

import java.util.Date;

import remoteInterface.RemoteData;

public class XBeePacket implements RemoteData{
	/**
	 * Private data fields
	 */
	// Controller device ID
	private String deviceID;
	
	// Controller device ID (physical)
	//private String physDeviceID;
	
	// Number of controller buttons
	private final int NUM_IO_PINS = 5;
	
	// (UP,DOWN,LEFT,RIGHT,CENTER) => true means pressed
	private boolean buttonData[];
	
	// True if the device with the deviceID is on
	private boolean deviceIsOn;
	
	// (UP,DOWN,LEFT,RIGHT,CENTER) => 0 volts (low) to 3.3 volts (high)
	private double buttonPinVoltages[];
	
	// Time Stamp for freshness check.
	private Date timeStamp;
	
	// TODO: COULD ADD MORE STORED INFO HERE (int powerRemaining, int wirelessStrength, int errorRate)

	
	/**
	 * Class constructor
	 */
	public XBeePacket() {
		
		// Initialize private fields
		this.deviceID = "";
		this.deviceIsOn = false;
		
		this.buttonData = new boolean[this.NUM_IO_PINS];
		this.buttonPinVoltages = new double[this.NUM_IO_PINS];
		for(int i = 0; i < this.NUM_IO_PINS; i++) {
			this.buttonData[i] = false;
			this.buttonPinVoltages[i] = 0.0;
		}
	}

	
	// ACCESSOR FUNCTIONS
	
	@Override
	public String getControllerID() {
		return this.deviceID;
	}

	@Override
	public boolean isOn() {
		return this.deviceIsOn;
	}

	@Override
	public boolean[] getButtonIOStates() {
		return this.buttonData;
	}

	@Override
	public double[] getButtonPinVoltages() {
		return this.buttonPinVoltages;
	}

	
	// MUTATOR FUNCTIONS
	
	@Override
	public void setControllerID(String id) {
		this.deviceID = id;
	}

	@Override
	public void setOnState(boolean newOnState) {
		this.deviceIsOn = newOnState;
	}

	@Override
	public boolean setButtonIOStates(boolean[] newIOStates) {
		if(newIOStates.length != 5) {
			return false;
		}
		
		for(int i = 0; i < this.NUM_IO_PINS; i++) {
			this.buttonData[i] = newIOStates[i];
		}
		
		return true;
	}
	
	@Override
	public boolean setButtonPinVoltages(double[] newPinVoltages) {
		
		this.buttonPinVoltages = newPinVoltages;
		
		return true;
	}
	
	/**
	 * This is the function that will be called to set the controllers
	 * freshness.
	 * @param time
	 */
	public void setTimeStamp(Date time)
	{
		this.timeStamp = time;
	}
	
	/**
	 * Get the time when the controller was last updated.
	 * @return
	 */
	public Date getTimeStamp()
	{
		return this.timeStamp;
	}
	
	@Override
	public String toString() {
		String strToReturn = "Device ID: " + this.deviceID + "\n" + "Device is on?: " + this.deviceIsOn + "\n" + 
								"Button data (UP,DN,LT,RT,CNTR): ";
		for(int i = 0; i < this.buttonData.length; i++) {
			strToReturn = strToReturn + this.buttonData[i] + " ";
		}
		strToReturn = strToReturn + "\n" + "Button pin voltages (UP,DN,LT,RT,CNTR): ";
		for(int i = 0; i < this.buttonPinVoltages.length; i++) {
			strToReturn = strToReturn + this.buttonPinVoltages[i] + " ";
		}
		return strToReturn;
	}
}
