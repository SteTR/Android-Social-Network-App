package edu.uw.tcss450.stran373.ui.Weather;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Used to keep track of the location requested by the user via
 * Google Maps
 *
 * @author Jonathan Lee
 */
public class MapViewModel extends AndroidViewModel {

    /**
     * Represents a JSON response.
     */
    private MutableLiveData<JSONObject> mResponse;

    /**
     * Constructor used to create the ViewModel.
     *
     * @param theApplication is the application that uses this ViewModel.
     */
    public MapViewModel(@NonNull Application theApplication) {
        super(theApplication);
        mResponse = new MutableLiveData<>();
        mResponse.setValue(new JSONObject());
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
     * Used for connecting to the database to add a new location entry to the database,
     * which are used for displaying their weather information.
     *
     * NOTE: Sometimes, you will need to disconnect and reconnect the device from Wi-Fi
     * just to make proper use of the geocoding used here.
     *
     * @param theJWT is the JWT used for authentication.
     * @param theLat is the latitude of the location.
     * @param theLon is the longitude of the location.
     * @param theNN is the nickname of the location.
     */
    public void connect(String theJWT, Double theLat, Double theLon, String theNN) {
        Log.d("JWT: ", theJWT);
        final String url = "https://production-tcss450-backend.herokuapp.com/weather";

        // Request body
        JSONObject body = new JSONObject();
        try {
            body.put("lat", "" + theLon);
            body.put("lon", "" + theLat);
            body.put("nickname", theNN);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Headers
        Request request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                body,
                mResponse::setValue,
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
