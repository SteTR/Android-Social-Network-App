package edu.uw.tcss450.stran373.ui.Weather;

import java.io.Serializable;

/**
 * This class is used for each Hour displayed in the Weather page.
 *
 * @author Bryce Fujita
 */
public class HourlyCard implements Serializable {

    /**
     * Represents the time in hours as an integer.
     */
    private final int mTime;

    /**
     * Represents the time formatted as a string.
     */
    private final String mTemp;

    /**
     * Represents the weather condition.
     */
    private final int mCond;

    /**
     * Inner class used to construct card.
     */
    public static class Builder {
        private final int mTime;
        private final String mTemp;
        private final int mCond;

        /**
         * Constructor.
         * @param theTime the hour displayed
         * @param theTemp the temperature for that hour
         */
        public Builder(final int theTime, final String theTemp,
                       final int theCond) {
            this.mTime = theTime;
            this.mTemp = theTemp;
            this.mCond = theCond;
        }

        /**
         * Public method to contruct Hourly.
         * @return HourlyCard
         */
        public HourlyCard build() {return new HourlyCard(this);}

    }

    /**
     * Private inner constructor.
     * @param theBuilder used to construct HourlyCard
     */
    private HourlyCard(final Builder theBuilder) {
        this.mTime = theBuilder.mTime;
        this.mTemp = theBuilder.mTemp;
        this.mCond = theBuilder.mCond;
    }

    /**
     * Get hour.
     * @return String of the hour
     */
    public String getTime() { return String.format("%d:00", mTime); }

    /**
     * Get the hour number (0-23).
     * @return the hour as a numerical value
     */
    public int getTimeNum() { return mTime; }

    /**
     * Get temperature.
     * @return String of the predicted temperature
     */
    public String getTemp() { return mTemp; }

    /**
     * Get the weather condition.
     *
     * @return the weather condition.
     */
    public int getCond() { return mCond; }

}
