package edu.uw.tcss450.stran373.ui.Weather;

import android.app.Application;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.IntFunction;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.time.YearMonth;

import edu.uw.tcss450.stran373.UserInfoViewModel;
import edu.uw.tcss450.stran373.R;
import edu.uw.tcss450.stran373.databinding.FragmentWeatherBinding;

/**
 * ViewModel used for the WeatherFragment. This will be used to handle the main
 * functionality for displaying the weather.
 *
 * @author Jonathan Lee
 */
public class WeatherViewModel extends AndroidViewModel {

    /**
     * LiveData to hold the list of WeatherCard objects.
     */
    private MutableLiveData<List<WeatherCard>> mCardList;

    /**
     * LiveData to hold the list of HourlyCard objects.
     */
    private MutableLiveData<List<HourlyCard>> mHourCards;

    /**
     * 2D array to hold the dates for the five days.
     */
    private int[][] mFutureDays;

    /**
     * Represents the most recent latitude used to retrieve the most recent location.
     */
    private Object mLat;

    /**
     * Represents the most recent longitude used to retrieve the most recent location.
     */
    private Object mLon;

    /**
     * Represents the most recent city location.
     */
    private String mCity;

    /**
     * Represents the most recent state.
     */
    private String mState;

    /**
     * Represents the most recent zip code used to retrieve the most recent location.
     */
    private long mZip;

    /**
     * Constructor for the ViewModel.
     *
     * @param theApplication is the current application object.
     */
    public WeatherViewModel(@NonNull Application theApplication) {
        super(theApplication);
        mFutureDays = new int[5][3];
        mCardList = new MutableLiveData<>();
        mCardList.setValue(new ArrayList<>());
        mHourCards = new MutableLiveData<>();
        mHourCards.setValue(new ArrayList<>());
        mZip = 0;
    }

    /**
     * Adds an observer to the weather card list.
     *
     * @param theOwner is a LifecycleOwner object.
     * @param theObserver is an observer for the weather fragment.
     */
    public void addWeatherCardListObserver(@NonNull LifecycleOwner theOwner,
                                            @NonNull Observer<? super List<WeatherCard>> theObserver) {
        mCardList.observe(theOwner, theObserver);
    }

    /**
     * Error-handling method for the ViewModel.
     *
     * @param theError is a VolleyError that is the result of a failed connection.
     */
    private void handleError(final VolleyError theError) {
        Log.e("CONNECTION ERROR", theError.getLocalizedMessage());
        throw new IllegalStateException(theError.getMessage());
    }

    /**
     * Result-handling method for the ViewModel. Creates the default weather card.
     *
     * @param theResult is the root JSONObject for retrieving all of the necessary data.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void handleResult(final JSONObject theResult) {
        try {
            JSONObject root = theResult;
            List<HourlyCard> list = new ArrayList<HourlyCard>();

            // Get the current date information.
            Calendar cal = Calendar.getInstance(TimeZone.getDefault());
            int currentDay = cal.get(Calendar.DATE);
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            YearMonth ymo = YearMonth.of(year, month);
            int daysInMonth = ymo.lengthOfMonth();

            // Get the 5-day forecast.
            mFutureDays = fiveDays(year, month, currentDay, daysInMonth);
            JSONArray jArr = (JSONArray) root.get("daily");
            String[] jDays = fiveForeCast(jArr);
            int[] jConds = fiveConditions(jArr);

            // Get the current weather.
            JSONObject current = (JSONObject) root.get("current");
            JSONArray currentWeather = (JSONArray) current.get("weather");
            JSONObject currentCond = currentWeather.getJSONObject(0);
            int currentCondition = (int) currentCond.get("id");
            double currentTemp = (double) current.get("temp");

            // Weather card for the most recent location.
            WeatherCard wc = new WeatherCard
                    .Builder(mCity + ", " + mState, currentTemp + " F°", currentCondition)
                    .addDay1(String.format("%d/%d", mFutureDays[0][0], mFutureDays[0][1]), (jDays[0]), jConds[0])
                    .addDay2(String.format("%d/%d", mFutureDays[1][0], mFutureDays[1][1]), (jDays[1]), jConds[1])
                    .addDay3(String.format("%d/%d", mFutureDays[2][0], mFutureDays[2][1]), (jDays[2]), jConds[2])
                    .addDay4(String.format("%d/%d", mFutureDays[3][0], mFutureDays[3][1]), (jDays[3]), jConds[3])
                    .addDay5(String.format("%d/%d", mFutureDays[4][0], mFutureDays[4][1]), (jDays[4]), jConds[4])
                    .build();
            // !mCardList.getValue().contains(wc) && mCardList.getValue().size() < 1
//            if (!mCardList.getValue().contains(wc)) {
//                mCardList.getValue().add(wc);
//            }
            if (!cardExists(wc) || mCardList.getValue().isEmpty()) {
                mCardList.getValue().add(wc);
            }

            // Get the 24-hour forecast.
            JSONArray hourArray = (JSONArray) root.get("hourly");
            int hour = cal.get(Calendar.HOUR_OF_DAY);

            for (int i = 0; i < 24; i++) {
                JSONObject hourTemp = hourArray.getJSONObject(i);
                JSONArray hourWeather = (JSONArray) hourTemp.get("weather");
                JSONObject hourObj = hourWeather.getJSONObject(0);
                int hourCond = (int) hourObj.get("id");
                int nextHour = (hour + i) % 24;
                Object temp = hourTemp.get("temp");
                HourlyCard hc = new HourlyCard
                        .Builder(nextHour, String.format("%s F°", formatter(temp)), hourCond)
                        .build();
                list.add(hc);
                mHourCards.getValue().add(hc);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("ERROR!", e.getMessage());
        }

        // Set up the list of weather cards and hourly cards.
        mCardList.setValue(mCardList.getValue());
        mHourCards.setValue(mHourCards.getValue());
    }

    /**
     * Helper method to check to see if the same weather card exists in the card list.
     *
     * @param theCard is a weather card.
     * @return true if there is already the same existing card, false otherwise.
     */
    private boolean cardExists(WeatherCard theCard) {
        boolean exists = false;
        for (int i = 0; i < mCardList.getValue().size(); i++) {
            String loc1 = mCardList.getValue().get(i).getLocation();
            String loc2 = theCard.getLocation();
            if (loc1.equals(loc2)) {
                exists = true;
            }
        }
        return exists;
    }

    /**
     * Helper method to represent numerical values into readable Strings.
     *
     * @param theObject is an object that holds the numerical value.
     * @return a String representing said numerical value
     */
    private String formatter(Object theObject) {
        String result = "";
        if (theObject instanceof Double) {
            result = String.format("%,.1f", theObject);
        } else if (theObject instanceof Integer) {
            result = String.format("%d", theObject);
        }
        return result;
    }

    /**
     * Helper method to get the five conditions for the 5-day forecast.
     *
     * @param theArray is an array of JSONObjects representing each day.
     * @return an array of weather condition codes.
     */
    private int[] fiveConditions(JSONArray theArray) {
        try {
            int[] array = new int[5];
            for (int i = 0; i < array.length; i++) {
                JSONObject obj = theArray.getJSONObject(i);
                JSONArray arr = (JSONArray) obj.get("weather");
                JSONObject condition = arr.getJSONObject(0);
                int cond = (int) condition.get("id");
                array[i] = cond;
            }
            return array;
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("ERROR!", e.getMessage());
        }
        return null;
    }

    /**
     * Helper method for retrieving data for the next five days,
     * including the current day
     *
     * @param theArray a JSONArray for retrieving the necessary data.
     * @return an array of JSONObjects for the five days
     */
    private String[] fiveForeCast(JSONArray theArray) {
        try {
            String[] array = new String[5];
            for (int i = 0; i < array.length; i++) {
                JSONObject obj = theArray.getJSONObject(i);
                JSONObject obj2 = (JSONObject) obj.get("temp");
                // Retrieve the minimum and maximum temperatures of that day.
                Object min = obj2.get("min");
                Object max = obj2.get("max");
                array[i] = String.format("%s/%s F°", formatter(min), formatter(max));
            }
            return array;
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("ERROR!", e.getMessage());
        }
        return null;
    }

    /**
     * Retrieves the latitude and longitude from the JSON request.
     *
     * @param theResult is a JSONObject from said request.
     */
    private void handleZip(final JSONObject theResult) {
        try {
            if (theResult.get("lat") instanceof Integer) {
                mLat = theResult.get("lat");
            } else {
                mLat = theResult.get("lat");
            }

            if (theResult.get("lng") instanceof Integer) {
                mLon = theResult.get("lng");
            } else {
                mLon = theResult.get("lng");
            }
            Log.d("Lat/Long", mLat + "/" + mLon);
            mCity = (String) theResult.get("city");
            mState = (String) theResult.get("state");
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("ERROR!", e.getMessage());
        }
    }

    /**
     * Uses the zip code to retrieve the latitude and longitude needed
     * to retrieve location data.
     * Connects to another API service for this purpose.
     *
     * @param theZip is a postal code (zip code).
     */
    public void connectZip(long theZip) {
        final String url = "https://www.zipcodeapi.com/rest/"
        + "jHC7gyngynPlUQuJwlxcccscoKqPYCZjupuRC54gRXTPcNP7KDF7x2mXyoc4lV3A/"
                +"info.json/" + theZip + "/degrees";
        Request request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                this::handleZip,
                this::handleError) {

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                return headers;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(getApplication().getApplicationContext()).add(request);
    }

    /**
     * Helper method to get the next five dates.
     *
     * @param theYear is the current year.
     * @param theMonth is the current month.
     * @param theDay is the current day.
     * @param theMaxDays is the maximum number of days in the current month.
     * @return the dates for the next five days
     */
    private int[][] fiveDays(int theYear, int theMonth, int theDay, int theMaxDays) {
        int days[][] = new int[5][3];
        int newMonth = theMonth;
        int year = theYear % 2000;
        for (int i = 0; i < days.length; i++) {
            int newDay = (theDay + i) % (theMaxDays);
            if (newDay == 0) {
                newMonth++;
                newDay++;
            }

            // month/day/year; e.g. 4/13/21
            days[i][0] = newMonth;
            days[i][1] = newDay;
            days[i][2] = year;
        }
        return days;
    }

    /**
     * Used to connect to the weather web service.
     */
    public void connect(String theJWT) {
        connectZip(mZip);
//        final String url = "https://production-tcss450-backend.herokuapp.com/weather?lat=47.608013&lon=-122.335167";
        final String url = "https://production-tcss450-backend.herokuapp.com/weather?lat=" +
                mLat + "&lon=" + mLon;
        Request request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                this::handleResult,
                this::handleError) {

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", theJWT);
                return headers;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(getApplication().getApplicationContext()).add(request);
    }

    /**
     * Used to set the most recent zip code for retrieving the current location. Returns true
     * if the user enters a new location, false otherwise.
     *
     * @param theZip is a zip code (postal code).
     */
    public boolean setZip(long theZip) {
        boolean exists = theZip == mZip;
        if (!exists) {
            mZip = theZip;
        }
        return exists;
    }

    /**
     * Getter method for the list of HourlyCards for the 24-hour forecast.
     *
     * @return a list of 24 HourlyCards.
     */
    public List<HourlyCard> getHours() {
        return mHourCards.getValue();
    }

}
