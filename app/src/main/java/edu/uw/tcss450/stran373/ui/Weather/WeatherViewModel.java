package edu.uw.tcss450.stran373.ui.Weather;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
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

    /**
     * Constructor for the ViewModel.
     *
     * @param application
     */
    public WeatherViewModel(@NonNull Application application) {
        super(application);
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
    private void handleResult(final JSONObject result) {
        IntFunction<String> getString = getApplication().getResources()::getString;
        try {
            JSONObject root = result;
            if (root.has(getString.apply(R.string.keys_json_weather_response))) {
                JSONObject response =
                        root.getJSONObject(getString.apply(
                                R.string.keys_json_weather_response));
                if (response.has(getString.apply(R.string.keys_json_weather_data))) {
                    JSONArray data = response.getJSONArray(
                            getString.apply(R.string.keys_json_weather_data));
                    JSONObject obj = data.getJSONObject(0);
                    WeatherCard wc = new WeatherCard
                            .Builder("Seattle, WA", "67 F°")
                            .addDay1("Tuesday 27th","70/65 F°")
                            .addDay2("Wednesday 28th", "65/49 F°")
                            .addDay3("Thursday 29th", "66/56 F°")
                            .addDay4("Friday 30th", "73/63 F°")
                            .addDay5("Saturday 1st", "75/65 F°").build();
                    if (!mCardList.getValue().contains(wc)) {
                        mCardList.getValue().add(wc);
                    }
                } else {
                    Log.e("ERROR!", "No data array");
                }
            } else {
                Log.e("ERROR!", "No response");
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("ERROR!", e.getMessage());
        }

        mCardList.setValue(mCardList.getValue());
    }

    /**
     * Used to connect to the weather web service.
     *
     * @param jwt
     */
    public void connect(final String jwt) {
        String url = "https://api.openweathermap.org/data/2.5/weather";
//        url += "?q=Seattle,53,1&appid=128e4fc74c1ba9cb7c3c3e7de0e05cd6";
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
                headers.put("appid","128e4fc74c1ba9cb7c3c3e7de0e05cd6");
//                headers.put("mode", "html");
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
