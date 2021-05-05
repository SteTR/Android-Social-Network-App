package edu.uw.tcss450.stran373.ui.Weather;

import android.util.Log;

import java.util.Arrays;
import java.util.List;

/**
 * Builds fake weather data.
 */
public final class WeatherGenerator {

    private static final WeatherCard[] CARDS;
    public static final int COUNT = 5;

    static {
        CARDS = new WeatherCard[COUNT];
        for (int i = 0; i < COUNT; i++) {
            CARDS[i] = new WeatherCard
                    .Builder("Seattle, WA", "67 F°")
                    .addDay1("Tuesday 27th","70/65 F°")
                    .addDay2("Wednesday 28th", "65/49 F°")
                    .addDay3("Thursday 29th", "66/56 F°")
                    .addDay4("Friday 30th", "73/63 F°")
                    .addDay5("Saturday 1st", "75/65 F°").build();
        }
    }

    /**
     * Returns fake weather data list.
     * @return A list of WeatherCards with fake data
     */
    public static List<WeatherCard> getWeatherList() {
        return Arrays.asList(CARDS);
    }

    private WeatherGenerator() {}
}
