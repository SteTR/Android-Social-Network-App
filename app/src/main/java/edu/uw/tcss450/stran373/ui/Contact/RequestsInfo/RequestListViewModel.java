package edu.uw.tcss450.stran373.ui.Contact.RequestsInfo;

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

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.IntFunction;

import edu.uw.tcss450.stran373.R;

public class RequestListViewModel extends AndroidViewModel {
    /**
     * A list of all request cards. For the moment it will be randomly generated,
     * next sprint we will need to pull from our endpoint
     */
    private MutableLiveData<List<RequestCard>> mRequestCards;

    private MutableLiveData<JSONObject> mResponse;

    public RequestListViewModel(@NonNull Application application) {
        super(application);
        mRequestCards = new MutableLiveData<>();
        mRequestCards.setValue(new ArrayList<>());
        mResponse = new MutableLiveData<>();
        mResponse.setValue(new JSONObject());
    }


    /**
     * Remove this request from the view model
     */
    public void removeRequest(RequestCard theRequest) {
        mRequestCards.getValue().remove(theRequest);
    }

    /**
     * Get request cards
     */
    public MutableLiveData<List<RequestCard>> getRequests() {
        return mRequestCards;
    }
    /**
     * Add an observer to the Request cards to check for change of state
     *
     * @param owner the lifecycle owner of this observer
     * @param observer watches for change of state in Request cards
     * @author Andrew Bennett
     */
    public void addRequestListObserver(@NonNull LifecycleOwner owner,
                                       @NonNull Observer<? super List<RequestCard>> observer) {
        mRequestCards.observe(owner, observer);
    }

    /**
     * Handle an error from the Requests endpoint
     *
     * @param error that may occur during a request from the endpoint
     */
    private void handleError(final VolleyError error) {
        if (Objects.isNull(error.networkResponse)) {
            Log.e("NETWORK ERROR", error.getMessage());
        }
        else {
            String data = new String(error.networkResponse.data, Charset.defaultCharset());
            Log.e("CLIENT ERROR",
                    error.networkResponse.statusCode +
                            " " +
                            data);
        }
    }


    /**
     * Handles a successful get from the request endpoint
     * @param theResult
     */
    private void handleResult(final JSONObject theResult) {
        IntFunction<String> getString = getApplication().getResources()::getString;
        //Check the result of the call
        try {
            JSONObject root = theResult;
            if (root.get("success").toString().equals("true")) {
                JSONArray data = root.getJSONArray(getString.apply(
                        R.string.keys_json_contacts_data));
                for (int i = 0; i < data.length(); i++) {
                    JSONObject jsonRequest = data.getJSONObject(i);
                    RequestCard request = new RequestCard.Builder(
                            jsonRequest.getString("memberid"),
                            jsonRequest.getString("firstname"),
                            jsonRequest.getString("lastname"))
                            .build();
                    if (!mRequestCards.getValue().contains(request)
                            && mRequestCards.getValue().size() < data.length()) {
                        mRequestCards.getValue().add(request);
                    }
                }
            }
            mRequestCards.setValue(mRequestCards.getValue());
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("ERROR!", e.getMessage());
        }

    }


    /**
     * Get the contacts from the web service endpoint
     */
    public void connectGet(final String theJWT) {
        //Need to add our contact endpoint
        String user_auth = theJWT;
        String url = getApplication().getResources().getString(R.string.base_url)+"requests";

        //Request the request information and hand in auth token
        try {
            Request request = new JsonObjectRequest(
                    Request.Method.GET,
                    url,
                    null, //no body for this get request
                    this::handleResult,
                    this::handleError) {
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> headers = new HashMap<>();
                    // add our new <key,value>
                    headers.put("Authorization", user_auth);
                    return headers;
                }
            };
            //Retry after certain amount of time.
            request.setRetryPolicy(new
                    DefaultRetryPolicy(
                    10_000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            //Instantiate the RequestQueue and add the request to the queue
            Volley.newRequestQueue(getApplication().getApplicationContext()).add(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void connectPost(final String theID, final String theAnswer, final String theJWT) {

        String user_auth = theJWT;
        Log.i("JWT", user_auth);
        String url = getApplication().getResources().getString(R.string.base_url)+"requests";
        //Create the body for the JSON request
        JSONObject body = new JSONObject();

        //Use POST to send a request to the chat endpoint
        try {
            body.put("requesterid", theID);
            body.put("answer", theAnswer);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Request request = new JsonObjectRequest(Request.Method.POST,
                url,
                body, mResponse::setValue,
                this::handleError) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                // add our new <key,value>
                headers.put("Authorization", user_auth);
                return headers;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        //Instantiate the RequestQueue and add the request to the queue
        Volley.newRequestQueue(getApplication().getApplicationContext()).add(request);
    }

    public int getSize() {
        return mRequestCards.getValue().size();
    }
}
