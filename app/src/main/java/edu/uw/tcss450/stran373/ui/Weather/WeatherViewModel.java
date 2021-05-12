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
 * ViewModel used for the WeatherFragment.
 */
public class WeatherViewModel extends AndroidViewModel {

    /**
     * LiveData to hold the list of WeatherCard objects.
     */
    private MutableLiveData<List<WeatherCard>> mCardList;

    private int[][] mFutureDays;

    /**
     * Constructor for the ViewModel.
     *
     * @param application
     */
    public WeatherViewModel(@NonNull Application application) {
        super(application);
        mFutureDays = new int[5][3];
        mCardList = new MutableLiveData<>();
        mCardList.setValue(new ArrayList<>());
    }

    /**
     * Adds an observer to the weather card list.
     *
     * @param owner
     * @param observer
     */
    public void addWeatherCardListObserver(@NonNull LifecycleOwner owner,
                                            @NonNull Observer<? super List<WeatherCard>> observer) {
        mCardList.observe(owner, observer);
    }

    /**
     * Error-handling method for the ViewModel.
     *
     * @param error
     */
    private void handleError(final VolleyError error) {
        Log.e("CONNECTION ERROR", error.getLocalizedMessage());
        throw new IllegalStateException(error.getMessage());
    }

    /**
     * Result-handling method for the ViewModel. Creates the default weather card.
     *
     * @param result
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void handleResult(final JSONObject result) {
        try {
            JSONObject root = result;
            JSONObject main = (JSONObject) root.get("main");
            double temperature = (double) main.get("temp");
            String cityName = (String) root.get("name");
            Calendar cal = Calendar.getInstance(TimeZone.getDefault());
            int currentDay = cal.get(Calendar.DATE);
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            YearMonth ymo = YearMonth.of(year, month);
            int daysInMonth = ymo.lengthOfMonth();
            mFutureDays = fiveDays(year, month, currentDay, daysInMonth);
            WeatherCard wc = new WeatherCard
                    .Builder(cityName + ", WA", temperature + " F°")
                    .addDay1(String.format("%d/%d/%d", mFutureDays[0][0], mFutureDays[0][1], mFutureDays[0][2]),
                            String.format("%,.1f/%,.1f F°", (double) main.get("temp_min"), (double) main.get("temp_max")))
                    .addDay2(String.format("%d/%d/%d", mFutureDays[1][0], mFutureDays[1][1], mFutureDays[1][2]),
                            String.format("%,.1f/%,.1f F°", (double) main.get("temp_min"), (double) main.get("temp_max")))
                    .addDay3(String.format("%d/%d/%d", mFutureDays[2][0], mFutureDays[2][1], mFutureDays[2][2]),
                            String.format("%,.1f/%,.1f F°", (double) main.get("temp_min"), main.get("temp_max")))
                    .addDay4(String.format("%d/%d/%d", mFutureDays[3][0], mFutureDays[3][1], mFutureDays[3][2]),
                            String.format("%,.1f/%,.1f F°", (double) main.get("temp_min"), (double) main.get("temp_max")))
                    .addDay5(String.format("%d/%d/%d", mFutureDays[4][0], mFutureDays[4][1], mFutureDays[4][2]),
                            String.format("%,.1f/%,.1f F°", (double) main.get("temp_min"), (double) main.get("temp_max")))
                    .build();
            if (!mCardList.getValue().contains(wc)) {
                mCardList.getValue().add(wc);
            }


        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("ERROR!", e.getMessage());
        }

        mCardList.setValue(mCardList.getValue());
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
        for (int i = 1; i <= days.length; i++) {
            int newDay = (theDay + i) % (theMaxDays + 1);
            if (newDay == 0) {
                newMonth++;
                newDay++;
            }
            days[i-1][0] = newMonth;
            days[i-1][1] = newDay;
            days[i-1][2] = year;
        }
        return days;
    }

    /**
     * Used to connect to the weather web service.
     */
    public void connect() {
        String url = "https://api.openweathermap.org/data/2.5/weather";
        url += "?q=Seattle,53,1&APPID=128e4fc74c1ba9cb7c3c3e7de0e05cd6&cnt=5&units=imperial";
        Request request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                this::handleResult,
                this::handleError) {

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("q","Seattle,53,1");
                headers.put("APPID","128e4fc74c1ba9cb7c3c3e7de0e05cd6");
                headers.put("cnt", "5");
                headers.put("units", "imperial");
                return headers;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(getApplication().getApplicationContext()).add(request);
    }


}
