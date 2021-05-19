package edu.uw.tcss450.stran373.ui.Weather;

import java.util.Arrays;
import java.util.List;

/**
 * This class is used to generate fake information
 * for the HourlyRecycler.
 */
public class HourlyGenerator {

    private static final HourlyCard[] HOURLYS;
    public static final int COUNT = 24;

    static {
        HOURLYS = new HourlyCard[COUNT];
        for (int i = 1; i < COUNT+1; i++) {
//            String time = i <= 12 ? i%12 + " am" : i%12 + " pm";
            int time = i;
            HOURLYS[i-1] = new HourlyCard
                    .Builder(time, "67 F°", 800).build();
        }
    }

    /**
     * Returns a list of generated cards.
     * @return A List of generated cards
     */
    public static List<HourlyCard> getHourlyList() {
        return Arrays.asList(HOURLYS);
    }

    /**
     * Private constructor not used.
     */
    private HourlyGenerator() {}
}
