package edu.uw.tcss450.stran373.registration;

import android.app.Application;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.android.volley.AuthFailureError;
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

/**
 * ViewModel used to store information regarding registration.
 */
public class RegistrationViewModel extends AndroidViewModel {

    /**
     *
     */
    private MutableLiveData<JSONObject> myResponse;

    /**
     * Constructor used for the ViewModel.
     *
     * @param theApplication is an application who will be referenced.
     */
    public RegistrationViewModel(@NonNull Application theApplication) {
        super(theApplication);
        myResponse = new MutableLiveData<>();
        myResponse.setValue(new JSONObject());
    }

    /**
     * Used to add a response observer to the application.
     *
     * @param theOwner is a LifecycleOwner object.
     * @param theObserver is an Observer object.
     */
    public void addResponseObserver(@NonNull LifecycleOwner theOwner,
                                    @NonNull Observer<? super JSONObject> theObserver) {
        myResponse.observe(theOwner, theObserver);
    }

    /**
     * Helper method used to handle any errors when handling registration.
     *
     * @param theError is an error value that is passed to check to see if something went wrong with registration.
     */
    private void handleError(final VolleyError theError) {
        if (Objects.isNull(theError.networkResponse)) {
            try {
                myResponse.setValue(new JSONObject("{" +
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
                myResponse.setValue(response);
            } catch (JSONException e) {
                Log.e("JSON PARSE", "JSON Parse Error in handleError");
            }
        }
    }

    /**
     * Used to connect the application to a web service.
     *
     * @param theFirst is the user's first name.
     * @param theLast is the user's last name.
     * @param theEmail is the user's email.
     * @param thePassword is the user's password.
     */
    public void connect(final String theFirst, final String theLast, final String theEmail, final String thePassword) {
        String url = "https://group4-tcss450-project.herokuapp.com/auth";
        JSONObject body = new JSONObject();

        try {
            body.put("first", theFirst);
            body.put("last", theLast);
            body.put("email", theEmail);
            body.put("password", thePassword);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Request request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                body,
                myResponse::setValue,
                this::handleError);

        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(getApplication().getApplicationContext()).add(request);
    }
}
