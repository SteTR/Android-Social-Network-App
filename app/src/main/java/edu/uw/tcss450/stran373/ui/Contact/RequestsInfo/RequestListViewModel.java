package edu.uw.tcss450.stran373.ui.Contact.RequestsInfo;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.List;

/**
 * View model containing a list with all of the request cards that are pending
 * @author Andrew Bennett
 */
public class RequestListViewModel extends AndroidViewModel {

    /**
     * A list of all Request cards. For the moment it will be randomly generated,
     * next sprint we will need to pull from our endpoint
     */
    private MutableLiveData<List<Request>> mRequestCards;

    /**
     * Contructor that uses dummy values just to get some requests. Will need to be updated.
     * @param application
     */
    public RequestListViewModel(@NonNull Application application) {
        super(application);
        mRequestCards = new MutableLiveData<>();
        List<Request> cards = new ArrayList<>();
        for (int i = 0; i < cards.size(); i++) {
            cards.add(new Request
                    .Builder(
                            "First",
                            "Last", i,
                            "dummy@email.com",
                            "05/02/2021")
                    .build());
        }
        mRequestCards.setValue(cards);
    }


    /**
     * Add an observer to the contact cards to check for change of state
     *
     * @param owner
     * @param observer
     * @author Andrew Bennett
     */
    public void addRequestListObserver(@NonNull LifecycleOwner owner,
                                       @NonNull Observer<? super List<Request>> observer) {
        mRequestCards.observe(owner, observer);
    }

    /**
     * Handle an error when we actually pull from our contacts endpoint
     *
     * @param error
     */
    private void handleError(final VolleyError error) {
        //We will need to improve this error handler
        Log.e("CONNECTION ERROR", error.getLocalizedMessage());
        throw new IllegalStateException(error.getMessage());
    }

    /*Not needed for this sprint, but should basically be plug and play for next sprint
    private void handleResult(final JSONObject result) {
        IntFunction<String> getString = getApplication().getResources()::getString;
        try {
            JSONObject root = result;
            if (root.has(getString.apply(R.string.keys_json_contacts_response))) {
                JSONObject response =
                        root.getJSONObject(getString.apply(R.string.keys_json_contacts_response));
                if (response.has(getString.apply(
                        R.string.keys_json_contacts_data))) {

                    JSONArray data = response.getJSONArray(getString.apply(
                            R.string.keys_json_contacts_data));
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject jsonBlog = data.getJSONObject(i);
                        Contact contact = new Contact.Builder(
                                jsonBlog.getString(
                                        getString.apply(
                                                R.string.keys_json_contacts_firstname)),
                                jsonBlog.getString(
                                        getString.apply(
                                                R.string.keys_json_contacts_lastname)))
                                .addTeaser(jsonBlog.getString(
                                        getString.apply(
                                                R.string.keys_json_contacts_email)))
                                .addUrl(jsonBlog.getString(
                                        getString.apply(
                                                R.string.keys_json_contacts_nickname)))
                                .build();
                        if (!mContactCards.getValue().contains(contact)) {
                            mContactCards.getValue().add(contact);
                        }
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
        mContactCards.setValue(mContactCards.getValue());
    }*/


    /* Not needed for this sprint.
    public void connectGet() {
        //Need to add our contact endpoint
        String url = "";
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
                headers.put();
                return headers;
            }
        };

        request.setRetryPolicy(new
                DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Instantiate the RequestQueue and add the request to the queue
        Volley.newRequestQueue(getApplication().getApplicationContext()).add(request);
    }*/
}
