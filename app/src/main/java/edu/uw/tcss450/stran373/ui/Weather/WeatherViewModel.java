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

/**
 * ViewModel used for the WeatherFragment. This will be used to handle the main
 * functionality for displaying the weather.
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
     * Constructor for the ViewModel.
     *
     * @param theApplication
     */
    public WeatherViewModel(@NonNull Application theApplication) {
        super(theApplication);
        mFutureDays = new int[5][3];
        mCardList = new MutableLiveData<>();
        mCardList.setValue(new ArrayList<>());
        mHourCards = new MutableLiveData<>();
        mHourCards.setValue(new ArrayList<>());
    }

    /**
     * Adds an observer to the weather card list.
     *
     * @param theOwner
     * @param theObserver
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
            Calendar cal = Calendar.getInstance(TimeZone.getDefault());
            int currentDay = cal.get(Calendar.DATE);
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            YearMonth ymo = YearMonth.of(year, month);
            int daysInMonth = ymo.lengthOfMonth();

            // Get the 5-day forecast.
            mFutureDays = fiveDays(year, month, currentDay, daysInMonth);
            JSONArray jArr = (JSONArray) root.get("daily");
            JSONObject[] jDays = fiveForeCast(jArr);
            JSONObject current = (JSONObject) root.get("current");
            double currentTemp = (double) current.get("temp");
            WeatherCard wc = new WeatherCard
                    .Builder("Seattle, WA", currentTemp + " F°")
                    .addDay1(String.format("%d/%d", mFutureDays[0][0], mFutureDays[0][1]),
                            (String.format("%,.1f/%,.1f F°", jDays[0].get("min"), jDays[0].get("max"))))
                    .addDay2(String.format("%d/%d", mFutureDays[1][0], mFutureDays[1][1]),
                            (String.format("%,.1f/%,.1f F°", jDays[1].get("min"), jDays[1].get("max"))))
                    .addDay3(String.format("%d/%d", mFutureDays[2][0], mFutureDays[2][1]),
                            (String.format("%,.1f/%,.1f F°", jDays[2].get("min"), jDays[2].get("max"))))
                    .addDay4(String.format("%d/%d", mFutureDays[3][0], mFutureDays[3][1]),
                            (String.format("%,.1f/%,.1f F°", jDays[3].get("min"), jDays[3].get("max"))))
                    .addDay5(String.format("%d/%d", mFutureDays[4][0], mFutureDays[4][1]),
                            (String.format("%,.1f/%,.1f F°", jDays[4].get("min"), jDays[4].get("max"))))
                    .build();
            if (!mCardList.getValue().contains(wc) && mCardList.getValue().size() < 1) {
                mCardList.getValue().add(wc);
            }

            // Get the 24-hour forecast.
            JSONArray hourArray = (JSONArray) root.get("hourly");
            int hour = cal.get(Calendar.HOUR_OF_DAY);
            for (int i = 0; i < 24; i++) {
                JSONObject hourTemp = (JSONObject) hourArray.getJSONObject(i);
                Double temp = (Double) hourTemp.get("temp");
                int nextHour = (hour + i) % 24;
                HourlyCard hc = new HourlyCard
                        .Builder(String.format("%d:00", nextHour),
                        String.format("%,.1f F°", temp)).build();
                list.add(hc);
                mHourCards.getValue().add(hc);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("ERROR!", e.getMessage());
        }

        mCardList.setValue(mCardList.getValue());
        mHourCards.setValue(mHourCards.getValue());
    }

    /**
     * Helper method for retrieving data for the next five days,
     * including the current day
     *
     * @param theArray a JSONArray for retrieving the necessary data.
     * @return an array of JSONObjects for the five days
     */
    private JSONObject[] fiveForeCast(JSONArray theArray) {
        try {
            JSONObject[] array = new JSONObject[5];
            for (int i = 0; i < array.length; i++) {
                JSONObject obj = theArray.getJSONObject(i);
                JSONObject obj2 = (JSONObject) obj.get("temp");
                array[i] = obj2;
            }
            return array;
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("ERROR!", e.getMessage());
        }
        return null;
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
            days[i][0] = newMonth;
            days[i][1] = newDay;
            days[i][2] = year;
        }
        return days;
    }

    /**
     * Used to connect to the weather web service.
     */
    public void connect() {
        final String url = "https://api.openweathermap.org/data/2.5/onecall?lat=47.608013&lon=-122.335167&exclude=minutely, " +
                "alerts&units=imperial&appid=128e4fc74c1ba9cb7c3c3e7de0e05cd6";
        Request request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                this::handleResult,
                this::handleError) {

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("lat", "47.608013");
                headers.put("lon", "-122.335167");
                headers.put("exclude", "minutely, alerts");
                headers.put("units", "imperial");
                headers.put("appid", "128e4fc74c1ba9cb7c3c3e7de0e05cd6");
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
     * Getter method for the list of HourlyCards for the 24-hour forecast.
     *
     * @return a list of 24 HourlyCards.
     */
    public List<HourlyCard> getHours() {
        return mHourCards.getValue();
    }

}
