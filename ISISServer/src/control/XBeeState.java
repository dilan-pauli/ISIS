package control;

public class XBeeState {
	/**
	 * Private data fields
	 */
	// Controller device ID
	private String deviceID;
	
	// Number of controller buttons
	private int NUM_IO_PINS = 5;
	
	// (UP,DOWN,LEFT,RIGHT,CENTER) => true means pressed
	private boolean buttonData[] = {false, false, false, false, false};
	
	// True if the device with the deviceID is on
	private boolean deviceIsOn;
	
	// (UP,DOWN,LEFT,RIGHT,CENTER) => 0 volts (low) to 3.3 volts (high)
	private int buttonPinVoltages[] = {0, 0, 0, 0, 0};
	
	// TODO: COULD ADD MORE STORED INFO HERE (int powerRemaining, int wirelessStrength, int errorRate)

	
	/**
	 * Class constructor
	 */
	public XBeeState() {
		;
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
