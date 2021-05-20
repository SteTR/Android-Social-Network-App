package edu.uw.tcss450.stran373.ui.Home;

public class Weather {
    private String mLocation;

    private int mCondition;

    private String mTemp;

    public Weather(final String theLocation, final int theCondition,
                   final String theTemp) {
        mLocation = theLocation;
        mCondition = theCondition;
        mTemp = theTemp;
    }

    public Weather() {}

    public String getLocation() {
        return mLocation;
    }

    public int getCondition() {
        return mCondition;
    }

    public String getTemp() {
        return mTemp;
    }

}
