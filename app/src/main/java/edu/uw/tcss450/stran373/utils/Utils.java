package edu.uw.tcss450.stran373.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;

import edu.uw.tcss450.stran373.R;

/**
 * @author Bryce Fujita
 * A class used to change an applications theme.
 */
public class Utils {

    /** Static value used to store the state of which theme is chosen. */
    private static int sTheme;

    /** Static value used to represent the default theme. */
    public final static int THEME_DEFAULT = 0;

    /** Static value used to represent the Orange theme. */
    public final static int THEME_ORANGE = 1;

    /** Static value used to represent the PACNW theme. */
    public final static int THEME_PACNW = 2;

    /**
     * Set the theme of the Activity, and restart it by creating a new Activity
     * with the theme
     */
    public static void changeToTheme(final Activity theActivity, final int theTheme) {
        sTheme = theTheme;
        Intent intent = new Intent(theActivity, theActivity.getClass());
        theActivity.recreate();
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

    /** Set the theme of the activity, according to the configuration. */
    public static void onActivityCreateSetTheme(final Activity theActivity, final int theTheme) {
        switch(theTheme) {
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

    /** Set the theme of the activity, according to the configuration. */
    public static int getActivitySetTheme() {
        switch(sTheme) {
            default:
            case THEME_DEFAULT:
                return R.style.ThemeC;
            case THEME_ORANGE:
                return R.style.ThemeB;
            case THEME_PACNW:
                return R.style.ThemeE;
        }
    }
}
