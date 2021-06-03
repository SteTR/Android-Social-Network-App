package edu.uw.tcss450.stran373.ui.SignIn;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.Objects;

import edu.uw.tcss450.stran373.R;
import edu.uw.tcss450.stran373.io.RequestQueueSingleton;

/**
 * ViewModel to hold response after reset
 *
 * @author Steven Tran
 */
public class ResetViewModel extends AndroidViewModel {
    // TODO at the moment, the post response does not get tracked.
    // TODO need to inform the user that password has been changed
    private MutableLiveData<JSONObject> mResponse;

    public ResetViewModel(@NonNull Application application) {
        super(application);
        mResponse = new MutableLiveData<>();
        mResponse.setValue(new JSONObject());
    }

    /**
     * Add a observer to any changes to response
     *
     * @param owner owner who wants to observe
     * @param observer the function after observing
     */
    public void addResponseObserver(@NonNull LifecycleOwner owner,
                                    @NonNull Observer<? super JSONObject> observer) {
        mResponse.observe(owner, observer);
    }

    /**
     * Sends a post request to reset account password with associated email
     * @param email the email of the account to reset
     */
    public void connectPost(final String email) {
        final JSONObject body = new JSONObject();
        try {
            body.put("email", email);
        } catch (JSONException theE) {
            theE.printStackTrace();
        }
        String url = getApplication().getResources().getString(R.string.base_url) + "auth/resetPassword";
        Request resetPasswordRequest = new JsonObjectRequest(Request.Method.POST, url, body,
                (value) -> Log.d("Value from reset POST", value.toString()), this::handleError);

        resetPasswordRequest.setRetryPolicy(
                new DefaultRetryPolicy(10_000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Instantiate the RequestQueueand add the request to the queue
        RequestQueueSingleton.getInstance(
                getApplication().getApplicationContext()).addToRequestQueue(resetPasswordRequest);
    }

    /**
     * Sends a put request with a code to ensure the email received the code for resetting password
     *
     * @param email the email associated with the password reset request
     * @param code the 5 digit code needed to verify the user from the email
     * @param password the new password
     */
    public void connectPut(final String email, final int code, final String password) {
        final JSONObject body = new JSONObject();
        try {
            body.put("email", email);
            body.put("code", code);
            body.put("password", password);
        } catch (JSONException theE) {
            theE.printStackTrace();
        }

        String url = getApplication().getResources().getString(R.string.base_url) + "auth/resetPassword";
        Request request = new JsonObjectRequest(Request.Method.PUT, url, body,
                mResponse::setValue, this::handleError);
        request.setRetryPolicy(
                new DefaultRetryPolicy(10_000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Instantiate the RequestQueueand add the request to the queue
        RequestQueueSingleton.getInstance(
                getApplication().getApplicationContext()).addToRequestQueue(request);
    }


    private void handleError(final VolleyError error) {

        if (Objects.isNull(error.networkResponse)) {
            try {
                mResponse.setValue(new JSONObject("{" +
                        "error:\"" + error.getMessage() +
                        "\"}"));
            } catch (JSONException e) {
                Log.e("JSON PARSE", "JSON Parse Error in handleError");
            }
        } else {
            String data = new String(error.networkResponse.data, Charset.defaultCharset())
                    .replace('\"', '\'');
            try {
                JSONObject response = new JSONObject();
                response.put("code", error.networkResponse.statusCode);
                response.put("data", new JSONObject(data));
                mResponse.setValue(response);
                Log.d("RESET RESPONSE ERROR MESSAGE", mResponse.getValue().toString());
            } catch (JSONException e) {
                Log.e("JSON PARSE", "JSON Parse Error in handleError");
            }
        }
    }
}
