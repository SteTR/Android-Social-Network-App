package edu.uw.tcss450.stran373.ui.SignIn;

import android.app.Application;
import android.content.res.Resources;
import android.util.Base64;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import edu.uw.tcss450.stran373.R;
import edu.uw.tcss450.stran373.io.RequestQueueSingleton;

/**
 * ViewModel used to manage and store UI-related data regarding sign-in.
 */
public class SignInViewModel extends AndroidViewModel {

    /**
     * Used to keep track of the user's response.
     */
    private MutableLiveData<JSONObject> mResponse;

    /**
     * Constructor for the ViewModel.
     *
     * @param theApplication is the application used to set up the ViewModel.
     */
    public SignInViewModel(@NonNull Application theApplication) {
        super(theApplication);
        mResponse = new MutableLiveData<>();
        mResponse.setValue(new JSONObject());
    }

    /**
     * Used for adding a response observer to the ViewModel.
     *
     * @param theOwner is a LifecycleOwner object.
     * @param theObserver is an Observer object.
     */
    public void addResponseObserver(@NonNull LifecycleOwner theOwner,
                                    @NonNull Observer<? super JSONObject> theObserver) {
        mResponse.observe(theOwner, theObserver);
    }

    /**
     * Helper method to handle errors regarding sign-in.
     *
     * @param theError is a VolleyError object used to check any errors when sending it to a web service.
     */
    private void handleError(final VolleyError theError) {
        if (Objects.isNull(theError.networkResponse)) {
            try {
                mResponse.setValue(new JSONObject("{" +
                        "error:\"" + theError.getMessage() +
                        "\"}"));
            } catch (JSONException e) {
                Log.e("JSON PARSE", "JSON Parse Error in handleError");
            }
        }
        else {
            String data = new String(theError.networkResponse.data, Charset.defaultCharset())
                    .replace('\"', '\'');
            try {
                JSONObject response = new JSONObject();
                response.put("code", theError.networkResponse.statusCode);
                response.put("data", new JSONObject(data));
                mResponse.setValue(response);
            } catch (JSONException e) {
                Log.e("JSON PARSE", "JSON Parse Error in handleError");
            }
        }
    }

    /**
     * Used to connect to a web service for verification.
     *
     * @param theEmail is the user's email.
     * @param thePassword is the user's password.
     */
    public void connect(final String theEmail, final String thePassword) {
        String url = getApplication().getResources().getString(R.string.base_url) + "auth";

        Request request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                mResponse::setValue,
                this::handleError) {

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                String credentials = theEmail + ":" + thePassword;
                String auth = "Basic " + Base64.encodeToString(credentials.getBytes(),
                        Base64.NO_WRAP);
                headers.put("Authorization", auth);
                return headers;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueueSingleton.getInstance(getApplication().getApplicationContext()).addToRequestQueue(request);

    }

    /**
     * Connects to the web service to resend verification email
     *
     * @param email the user's email
     * @param password the user's password
     */
    public void resendEmail(final String email, final String password) {
        String url = getApplication().getResources().getString(R.string.base_url) + "auth/verification";
        Request request = new JsonObjectRequest(Request.Method.GET, url,
                null, mResponse::setValue, this::handleError) {

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                String credentials = email + ":" + password;
                String auth = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                headers.put("Authorization", auth);
                return headers;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueueSingleton.getInstance(getApplication().getApplicationContext()).addToRequestQueue(request);
    }
}