package edu.uw.tcss450.stran373.ui.Home;

/**
 * Class used to store current weather for home page
 */
public class Weather {
    /** String used to store location. */
    private String mLocation;

    /** Integer value which represents the current weather condition. */
    private int mCondition;

    /** A string value that has already been interpreted to be readable. */
    private String mTemp;

    /**
     * A Basic constructor which fills these fields.
     * @param theLocation The current location
     * @param theCondition The current condition
     * @param theTemp The current temperature
     */
    public Weather(final String theLocation, final int theCondition,
                   final String theTemp) {
        mLocation = theLocation;
        mCondition = theCondition;
        mTemp = theTemp;
    }

    /** Empty Constructor. */
    public Weather() {}

    /**
     * Returns the current location
     * @return The current location
     */
    public String getLocation() {
        return mLocation;
    }

    /**
     * Returns the current condition.
     * @return The current condition
     */
    public int getCondition() {
        return mCondition;
    }

    /**
     * Returns the current temperature.
     * @return Returns the current temperature
     */
    public String getTemp() {
        return mTemp;
    }

}
