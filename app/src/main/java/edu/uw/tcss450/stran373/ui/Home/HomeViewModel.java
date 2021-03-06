package edu.uw.tcss450.stran373.ui.Home;

import android.app.Application;
import android.location.Location;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MediatorLiveData;
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

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import edu.uw.tcss450.stran373.R;
import edu.uw.tcss450.stran373.ui.Weather.HourlyCard;
import edu.uw.tcss450.stran373.ui.Weather.WeatherCard;

/**
 * ViewModel used for the WeatherFragment. This will be used to handle the main
 * functionality for displaying the weather.
 */
public class HomeViewModel extends AndroidViewModel {

    /**
     * LiveData to hold the list of WeatherCard objects.
     */
    private MutableLiveData<Weather> mWeather;

    /**
     * Represents the current state.
     */
    private String mState;

    /**
     * Represents the current city;
     */
    private String mCity;

    /**
     * Constructor for the ViewModel.
     *
     * @param theApplication is the current application object.
     */
    public HomeViewModel(@NonNull Application theApplication) {
        super(theApplication);
        mWeather = new MutableLiveData<>();
        mWeather.setValue(new Weather());
    }

    /**
     * Adds an observer to the weather card list.
     *
     * @param theOwner is a LifecycleOwner object.
     * @param theObserver is an observer for the weather fragment.
     */
    public void addResponseObserver(@NonNull LifecycleOwner theOwner,
                                            @NonNull Observer<? super Weather> theObserver) {
        mWeather.observe(theOwner, theObserver);
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

            // Get the current weather.
            JSONObject current = (JSONObject) root.get("current");
            JSONArray currentWeather = (JSONArray) current.get("weather");
            JSONObject currentCond = currentWeather.getJSONObject(0);
            int currentCondition = (int) currentCond.get("id");
            Object currentTemp = (Object) current.get("temp");
            Double temp = 0.0;
            if (currentTemp instanceof Integer) {
                temp = Double.valueOf((Integer) current.get("temp"));
            } else {
                temp = (Double) current.get("temp");
            }
            // Weather card for Seattle, Washington
            Weather wc = new Weather("Current Location", currentCondition,temp + " F??");
            mWeather.setValue(wc);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("ERROR!", e.getMessage());
        }

        // Set up the list of weather cards and hourly cards.
        mWeather.setValue(mWeather.getValue());
    }

    /**
     * Used to connect to the weather web service.
     */
    public void connect(String theJWT, Location theLoc) {
        Double lat = theLoc.getLatitude();
        Double lng = theLoc.getLongitude();
        final String url = getApplication().getResources().getString(R.string.base_url) + "weather?lat=" + lat + "&lon=" + lng;
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

}
