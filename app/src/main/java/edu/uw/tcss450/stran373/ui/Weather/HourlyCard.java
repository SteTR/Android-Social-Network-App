package edu.uw.tcss450.stran373.ui.Weather;

import java.io.Serializable;

/**
 * This class is used for each Hour displayed in the Weather page.
 */
public class HourlyCard implements Serializable {

    private final String mTime;
    private final String mTemp;

    /**
     * Inner class used to construct card.
     */
    public static class Builder {
        private final String mTime;
        private final String mTemp;

        /**
         * Constructor.
         * @param theTime the hour displayed
         * @param theTemp the temperature for that hour
         */
        public Builder(final String theTime, final String theTemp) {
            this.mTime = theTime;
            this.mTemp = theTemp;
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
    }

    /**
     * Get hour.
     * @return String of the hour
     */
    public String getTime() { return mTime; }

    /**
     * Get temperature.
     * @return String of the predicted temperature
     */
    public String getTemp() { return mTemp; }

}
