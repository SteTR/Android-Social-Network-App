package edu.uw.tcss450.stran373.ui.Weather;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

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

public class MapViewModel extends AndroidViewModel {

    private String mCity;

    private String mState;

    private String mCountry;

    private long mZip;

    public MapViewModel(@NonNull Application theApplication) {
        super(theApplication);
        mZip  = 0;
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

    private void handleResult(final JSONObject theRoot) {
        try {
            mState = (String) theRoot.get("state");
            mCity = (String) theRoot.get("city");
            JSONObject alt = (JSONObject) theRoot.get("alt");
            JSONArray loc = (JSONArray) alt.get("loc");
            String num = (String) loc.getJSONObject(0).get("postal");
            mZip = conversion(num);
            mCountry = (String) theRoot.get("prov");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private long conversion(String theResult) {
        String num = theResult;
        if (theResult.length() > 5) {
            num = theResult.substring(0, 5);
        }
        return Long.parseLong(num);
    }

    public void connectLatLng(LatLng theLL) {
        Log.d("Let's connect!", "");
        final String url = "https://geocode.xyz/" + theLL.latitude + "," + theLL.longitude + "?json=1";
        Request request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                this::handleResult,
                this::handleError) {

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("json", "1");
                return headers;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(getApplication().getApplicationContext()).add(request);
    }

    public String getCity() {
        return mCity;
    }

    public String getCountry() {
        return mCountry;
    }

    public String getState() {
        return mState;
    }

    public long getZip() {
        return mZip;
    }

}
