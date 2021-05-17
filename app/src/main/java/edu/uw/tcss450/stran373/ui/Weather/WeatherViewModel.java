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
     * @param theApplication is the current application object.
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
            Log.d("JSON:", theResult.toString());
            JSONObject root = theResult;
            Log.d("NULL:", String.valueOf(root == null));
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

            int[] jConds = fiveConditions(jArr);
            JSONObject current = (JSONObject) root.get("current");
            JSONArray currentWeather = (JSONArray) current.get("weather");
            JSONObject currentCond = currentWeather.getJSONObject(0);
            int currentCondition = (int) currentCond.get("id");
            double currentTemp = (double) current.get("temp");

            WeatherCard wc = new WeatherCard
                    .Builder("Seattle, WA", currentTemp + " F°", currentCondition)
                    .addDay1(String.format("%d/%d", mFutureDays[0][0], mFutureDays[0][1]),
                            (String.format("%,.1f/%,.1f F°", jDays[0].get("min"), jDays[0].get("max"))), jConds[0])
                    .addDay2(String.format("%d/%d", mFutureDays[1][0], mFutureDays[1][1]),
                            (String.format("%,.1f/%,.1f F°", jDays[1].get("min"), jDays[1].get("max"))), jConds[1])
                    .addDay3(String.format("%d/%d", mFutureDays[2][0], mFutureDays[2][1]),
                            (String.format("%,.1f/%,.1f F°", jDays[2].get("min"), jDays[2].get("max"))), jConds[2])
                    .addDay4(String.format("%d/%d", mFutureDays[3][0], mFutureDays[3][1]),
                            (String.format("%,.1f/%,.1f F°", jDays[3].get("min"), jDays[3].get("max"))), jConds[3])
                    .addDay5(String.format("%d/%d", mFutureDays[4][0], mFutureDays[4][1]),
                            (String.format("%,.1f/%,.1f F°", jDays[4].get("min"), jDays[4].get("max"))), jConds[4])
                    .build();
            if (!mCardList.getValue().contains(wc) && mCardList.getValue().size() < 1) {
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
//                Log.d("temp", "" + hourTemp.get("temp").getClass());
                Double temp = (Double) hourTemp.get("temp");
                int nextHour = (hour + i) % 24;
                HourlyCard hc = new HourlyCard
                        .Builder(String.format("%d:00", nextHour),
                        String.format("%,.1f F°", temp), hourCond).build();
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
    public void connect(String theJWT) {
        final String url = "https://production-tcss450-backend.herokuapp.com/weather?lat=47.608013&lon=-122.335167";
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
     * Getter method for the list of HourlyCards for the 24-hour forecast.
     *
     * @return a list of 24 HourlyCards.
     */
    public List<HourlyCard> getHours() {
        return mHourCards.getValue();
    }

}
