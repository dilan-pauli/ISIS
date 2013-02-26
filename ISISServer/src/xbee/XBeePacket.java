package xbee;

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
	private int buttonPinVoltages[];
	
	// TODO: COULD ADD MORE STORED INFO HERE (int powerRemaining, int wirelessStrength, int errorRate)

	
	/**
	 * Class constructor
	 */
	public XBeePacket() {
		
		// Initalize private fields
		this.deviceID = "";
		this.deviceIsOn = false;
		
		this.buttonData = new boolean[this.NUM_IO_PINS];
		this.buttonPinVoltages = new int[this.NUM_IO_PINS];
		for(int i = 0; i < this.NUM_IO_PINS; i++) {
			this.buttonData[i] = false;
			this.buttonPinVoltages[i] = 0;
		}
	}

	
	// ACCESSOR FUNCTIONS
	
	/**
	 * Obtain the device ID
	 */
	public String getControllerID() {
		return this.deviceID;
	}

	/**
	 * Obtain the ON state of the device
	 */
	public boolean isOn() {
		return this.deviceIsOn;
	}

	/**
	 * Obtain the state of the button IO pins (UP,DOWN,LEFT,RIGHT,CENTER) => true means pressed
	 */
	public boolean[] getButtonIOStates() {
		return this.buttonData;
	}

	/**
	 * Obtain the button pin voltages
	 */
	public int[] getButtonPinVoltages() {
		return this.buttonPinVoltages;
	}

	
	// MUTATOR FUNCTIONS
	
	/**
	 * Set the controller ID for the device whose state this is
	 * DIO corresponds with the following:
	 * 0 = UP
	 * 1 = LEFT
 	 * 2 = RIGHT
 	 * 3 = DOWN
 	 * 4 = CENTER
	 */
	public void setControllerID(String id) {
		this.deviceID = id;
	}

	/**
	 * Set the on state of the device (true if the device is on)
	 */
	public void setOnState(boolean newOnState) {
		this.deviceIsOn = newOnState;
	}

	/**
	 * Set the state of the button IO pins (pressed or not pressed?)
	 * @return true if operation succeeded
	 */
	public boolean setButtonIOStates(boolean[] newIOStates) {
		if(newIOStates.length != 5) {
			return false;
		}
		
		for(int i = 0; i < this.NUM_IO_PINS; i++) {
			this.buttonData[i] = newIOStates[i];
		}
		
		return true;
	}

	/**
	 * Set the button pin voltages
	 * @return true if operation succeeded
	 */
	public boolean setButtonPinVoltages(int[] newPinVoltages) {
		if(newPinVoltages.length != 5) {
			return false;
		}
		
		for(int i = 0; i < this.NUM_IO_PINS; i++) {
			this.buttonPinVoltages[i] = newPinVoltages[i];
		}
		
		return true;
	}
}
