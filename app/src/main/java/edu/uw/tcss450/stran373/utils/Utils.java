package edu.uw.tcss450.stran373.utils;

import android.app.Activity;
import android.content.Intent;

import edu.uw.tcss450.stran373.R;

public class Utils {

    private static int sTheme;
    public final static int THEME_DEFAULT = 0;
    public final static int THEME_ORANGE = 1;
    public final static int THEME_PACNW = 2;

    /**
     * Set the theme of the Activity, and restart it by creating a new Activity
     * with the theme
     */
    public static void changeToTheme(final Activity theActivity, final int theTheme) {
        sTheme = theTheme;
        theActivity.finish();
        theActivity.startActivity(new Intent(theActivity, theActivity.getClass()));
    }

    /** Set the theme of the activity, according to the configuration. */
    public static void onActivityCreateSetTheme(final Activity theActivity) {
        switch(sTheme) {
            default:
            case THEME_DEFAULT:
                theActivity.setTheme(R.style.ThemeC);
                break;
            case THEME_ORANGE:
                theActivity.setTheme(R.style.ThemeB);
                break;
            case THEME_PACNW:
                theActivity.setTheme(R.style.ThemeE);
                break;
        }
    }
}
