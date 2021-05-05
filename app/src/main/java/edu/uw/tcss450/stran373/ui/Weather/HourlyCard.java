package edu.uw.tcss450.stran373.ui.Weather;

import java.io.Serializable;

public class HourlyCard implements Serializable {

    private final String mTime;
    private final String mTemp;

    public static class Builder {
        private final String mTime;
        private final String mTemp;

        public Builder(final String theTime, final String theTemp) {
            this.mTime = theTime;
            this.mTemp = theTemp;
        }

        public HourlyCard build() {return new HourlyCard(this);}

    }

    private HourlyCard(final Builder builder) {
        this.mTime = builder.mTime;
        this.mTemp = builder.mTemp;
    }

    public String getTime() { return mTime; }
    public String getTemp() { return mTemp; }

}
