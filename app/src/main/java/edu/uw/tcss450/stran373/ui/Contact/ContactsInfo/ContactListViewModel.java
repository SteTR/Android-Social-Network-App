package edu.uw.tcss450.stran373.ui.Contact.ContactsInfo;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import android.util.Log;
import android.view.View;

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

import edu.uw.tcss450.stran373.R;
import edu.uw.tcss450.stran373.UserInfoViewModel;


/**
 * View model to hold the contact cards, observer will be used to
 * navigate to specific contacts in next sprint
 * @author Andrew Bennett
 */
public class ContactListViewModel extends AndroidViewModel {

    /**
     * A list of all contact cards. For the moment it will be randomly generated,
     * next sprint we will need to pull from our endpoint
     */
    private MutableLiveData<List<ContactCard>> mContactCards;

    private MutableLiveData<JSONObject> mResponse;

    public ContactListViewModel(@NonNull Application application) {
        super(application);
        mContactCards = new MutableLiveData<>();
        mContactCards.setValue(new ArrayList<>());
        mResponse = new MutableLiveData<>();
        mResponse.setValue(new JSONObject());
    }


    /**
     * Add an observer to the contact cards to check for change of state
     *
     * @param owner the lifecycle owner of this observer
     * @param observer watches for change of state in contact cards
     * @author Andrew Bennett
     */
    public void addContactListObserver(@NonNull LifecycleOwner owner,
                                       @NonNull Observer<? super List<ContactCard>> observer) {
        mContactCards.observe(owner, observer);
    }

    /**
     * Handle an error when we actually pull from our contacts endpoint
     *
     * @param error that may occur during a request from the endpoint
     */
    private void handleError(final VolleyError error) {
        //We will need to improve this error handler
        Log.e("CONNECTION ERROR", mResponse.getValue().toString());
        throw new IllegalStateException(error.getMessage());
    }


    private void handleResult(final JSONObject theResult) {
        IntFunction<String> getString = getApplication().getResources()::getString;
        try {
            JSONObject root = theResult;
            if(root.get("success").toString().equals("true")) {
                JSONArray data = root.getJSONArray(getString.apply(
                        R.string.keys_json_contacts_data));
                for(int i = 0; i < data.length(); i++) {
                    JSONObject jsonContact = data.getJSONObject(i);
                    ContactCard contact = new ContactCard.Builder(
                            jsonContact.getString("memberid"),
                            jsonContact.getString("firstname"),
                            jsonContact.getString("lastname"),
                            jsonContact.getString("email"),
                            jsonContact.getString("username")
                            )
                            .build();
                    if (!mContactCards.getValue().contains(contact)
                            && mContactCards.getValue().size() < data.length()) {
                        mContactCards.getValue().add(contact);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("ERROR!", e.getMessage());
        }
        mContactCards.setValue(mContactCards.getValue());
    }


    public void connectGet() {
        //Need to add our contact endpoint
        String user_auth = UserInfoViewModel.getJwt();
        Log.i("The token for testing: ", user_auth);
        String url = "https://production-tcss450-backend.herokuapp.com/contacts";
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

    /**
     * Handle the creation of chats, calls the chats endpoint to create
     * a new chat
     * @param theCards are the users the owner wants to invite
     */
    protected void handleChatCreation(List<ContactCard> theCards) {

        //Get the auth token and set url
        String user_auth = UserInfoViewModel.getJwt();
        String url = "https://production-tcss450-backend.herokuapp.com/chats";

        //Currently memberids are strings, so get that and convert to int
        //That may be worth changing in Sprint 2
        String first_id = theCards.get(0).getMemberID();
        int id = Integer.parseInt(first_id);

        //Create the array for the body of the POST request and add the first
        //user
        JSONArray array = new JSONArray();
        StringBuilder sb = new StringBuilder();
        array.put(Integer.parseInt(theCards.get(0).getMemberID()));
        sb.append(theCards.get(0).getFirstName());

        //Create the group name and build the array for userids
        if(theCards.size() > 1) {
            //Add the first name of the user
            sb.append(theCards.get(0).getFirstName());
            //Loop through and add the first names of the
            //chat members and add the member id to the array
            for(int i = 1; i < theCards.size(); i++) {
                sb.append(", " + theCards.get(i).getFirstName());
                array.put(Integer.parseInt(theCards.get(i).getMemberID()));
            }
        } else {
            array.put(Integer.parseInt(theCards.get(0).getMemberID()));
        }
        //Create the body for the JSON request
        JSONObject body = new JSONObject();

        //Use POST to send a request to the chat endpoint
        try {
            body.put("groupname", sb.toString());
            body.put("users", array);
            Log.i("Body", body.toString());
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
}
