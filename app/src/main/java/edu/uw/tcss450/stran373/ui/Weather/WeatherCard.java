package edu.uw.tcss450.stran373.ui.Weather;

import java.io.Serializable;

public class WeatherCard implements Serializable {

    private final String mLocation;
    private final String mDate1;
    private final String mTemp1;
    private final String mDate2;
    private final String mTemp2;
    private final String mDate3;
    private final String mTemp3;
    private final String mDate4;
    private final String mTemp4;
    private final String mDate5;
    private final String mTemp5;

    public static class Builder {
        private final String mLocation;
        private String mDate1;
        private String mTemp1;
        private String mDate2;
        private String mTemp2;
        private String mDate3;
        private String mTemp3;
        private String mDate4;
        private String mTemp4;
        private String mDate5;
        private String mTemp5;

        public Builder(final String theLocation) {
            this.mLocation = theLocation;
        }

        public Builder addDay1(final String theDate, final String theTemp) {
            mDate1 = theDate;
            mTemp1 = theTemp;
            return this;
        }

        public Builder addDay2(final String theDate, final String theTemp) {
            mDate2 = theDate;
            mTemp2 = theTemp;
            return this;
        }

        public Builder addDay3(final String theDate, final String theTemp) {
            mDate3 = theDate;
            mTemp3 = theTemp;
            return this;
        }

        public Builder addDay4(final String theDate, final String theTemp) {
            mDate4 = theDate;
            mTemp4 = theTemp;
            return this;
        }

        public Builder addDay5(final String theDate, final String theTemp) {
            mDate5 = theDate;
            mTemp5 = theTemp;
            return this;
        }

        public WeatherCard build() {return new WeatherCard(this);}

    }

    private WeatherCard(final Builder builder) {
        this.mLocation = builder.mLocation;
        this.mDate1 = builder.mDate1;
        this.mTemp1 = builder.mTemp1;
        this.mDate2 = builder.mDate2;
        this.mTemp2 = builder.mTemp2;
        this.mDate3 = builder.mDate3;
        this.mTemp3 = builder.mTemp3;
        this.mDate4 = builder.mDate4;
        this.mTemp4 = builder.mTemp4;
        this.mDate5 = builder.mDate5;
        this.mTemp5 = builder.mTemp5;
    }

    public String getLocation() { return mLocation; }
    public String getDate1() { return mDate1; }
    public String getDate2() { return mDate2; }
    public String getDate3() { return mDate3; }
    public String getDate4() { return mDate4; }
    public String getDate5() { return mDate5; }
    public String getTemp1() { return mTemp1; }
    public String getTemp2() { return mTemp2; }
    public String getTemp3() { return mTemp3; }
    public String getTemp4() { return mTemp4; }
    public String getTemp5() { return mTemp5; }

}
