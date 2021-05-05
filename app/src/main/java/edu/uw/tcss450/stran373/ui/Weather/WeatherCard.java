package edu.uw.tcss450.stran373.ui.Weather;

import java.io.Serializable;

/**
 * Class used to reference information to Recycler View cards.
 */
public class WeatherCard implements Serializable {

    /**Location chosen by user.*/
    private final String mLocation;

    /**Current temperature for that location.*/
    private final String mCurrentTemp;

    /**First forecast date.*/
    private final String mDate1;

    /**Predicted temperature for first date.*/
    private final String mTemp1;

    /**Second forecast date.*/
    private final String mDate2;

    /**Predicted temperature for second date.*/
    private final String mTemp2;

    /**Third forecast date.*/
    private final String mDate3;

    /**Predicted temperature for third date.*/
    private final String mTemp3;

    /**Fourth forecast date.*/
    private final String mDate4;

    /**Predicted temperature for fourth date.*/
    private final String mTemp4;

    /**Fifth forecast date.*/
    private final String mDate5;

    /**Predicted temperature for fifth date.*/
    private final String mTemp5;

    public static class Builder {
        /**Location chosen by user.*/
        private final String mLocation;

        /**Current temperature for that location.*/
        private final String mCurrentTemp;

        /**First forecast date.*/
        private String mDate1;

        /**Predicted temperature for first date.*/
        private String mTemp1;

        /**Second forecast date.*/
        private String mDate2;

        /**Predicted temperature for second date.*/
        private String mTemp2;

        /**Third forecast date.*/
        private String mDate3;

        /**Predicted temperature for third date.*/
        private String mTemp3;

        /**Fourth forecast date.*/
        private String mDate4;

        /**Predicted temperature for fourth date.*/
        private String mTemp4;

        /**Fifth forecast date.*/
        private String mDate5;

        /**Predicted temperature for fifth date.*/
        private String mTemp5;

        /**
         * Inner Constructor to build WeatherCard
         * @param theLocation The location for weather
         * @param theCurrent The current temperature for the weather
         */
        public Builder(final String theLocation, final String theCurrent) {
            this.mLocation = theLocation;
            mCurrentTemp = theCurrent;
        }

        /**
         * Add day 1
         * @param theDate the date
         * @param theTemp the forecast
         * @return Builder class
         */
        public Builder addDay1(final String theDate, final String theTemp) {
            mDate1 = theDate;
            mTemp1 = theTemp;
            return this;
        }

        /**
         * Add day 2
         * @param theDate the date
         * @param theTemp the forecast
         * @return Builder class
         */
        public Builder addDay2(final String theDate, final String theTemp) {
            mDate2 = theDate;
            mTemp2 = theTemp;
            return this;
        }

        /**
         * Add day 3
         * @param theDate the date
         * @param theTemp the forecast
         * @return Builder class
         */
        public Builder addDay3(final String theDate, final String theTemp) {
            mDate3 = theDate;
            mTemp3 = theTemp;
            return this;
        }

        /**
         * Add day 4
         * @param theDate the date
         * @param theTemp the forecast
         * @return Builder class
         */
        public Builder addDay4(final String theDate, final String theTemp) {
            mDate4 = theDate;
            mTemp4 = theTemp;
            return this;
        }

        /**
         * Add day 5
         * @param theDate the date
         * @param theTemp the forecast
         * @return Builder class
         */
        public Builder addDay5(final String theDate, final String theTemp) {
            mDate5 = theDate;
            mTemp5 = theTemp;
            return this;
        }

        /**
         * Builds Weather Card to be used.
         * @return Weather Card to be used for reference.
         */
        public WeatherCard build() {return new WeatherCard(this);}

    }

    /** Private inner constructor. */
    private WeatherCard(final Builder theBuilder) {
        this.mLocation = theBuilder.mLocation;
        this.mCurrentTemp = theBuilder.mCurrentTemp;
        this.mDate1 = theBuilder.mDate1;
        this.mTemp1 = theBuilder.mTemp1;
        this.mDate2 = theBuilder.mDate2;
        this.mTemp2 = theBuilder.mTemp2;
        this.mDate3 = theBuilder.mDate3;
        this.mTemp3 = theBuilder.mTemp3;
        this.mDate4 = theBuilder.mDate4;
        this.mTemp4 = theBuilder.mTemp4;
        this.mDate5 = theBuilder.mDate5;
        this.mTemp5 = theBuilder.mTemp5;
    }

    /**
     * Returns location.
     * @return the location
     */
    public String getLocation() { return mLocation; }

    /**
     * Returns Location's current temperature.
     * @return Location's current temperature.
     */
    public String getCurrentTemp() { return mCurrentTemp; }

    /**
     * Returns Date1.
     * @return Returns Date1
     */
    public String getDate1() { return mDate1; }
    /**
     * Returns Date2.
     * @return Returns Date2
     */
    public String getDate2() { return mDate2; }
    /**
     * Returns Date3.
     * @return Returns Date3
     */
    public String getDate3() { return mDate3; }
    /**
     * Returns Date4.
     * @return Returns Date4
     */
    public String getDate4() { return mDate4; }
    /**
     * Returns Date5.
     * @return Returns Date5
     */
    public String getDate5() { return mDate5; }

    /**
     * Returns temperature for Date 1.
     * @return Temperature
     */
    public String getTemp1() { return mTemp1; }
    /**
     * Returns temperature for Date 2.
     * @return Temperature
     */
    public String getTemp2() { return mTemp2; }
    /**
     * Returns temperature for Date 3.
     * @return Temperature
     */
    public String getTemp3() { return mTemp3; }
    /**
     * Returns temperature for Date 4.
     * @return Temperature
     */
    public String getTemp4() { return mTemp4; }
    /**
     * Returns temperature for Date 5.
     * @return Temperature
     */
    public String getTemp5() { return mTemp5; }

}
