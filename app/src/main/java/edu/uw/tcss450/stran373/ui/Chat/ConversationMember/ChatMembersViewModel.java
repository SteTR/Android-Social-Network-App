package edu.uw.tcss450.stran373.ui.Chat.ConversationMember;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import edu.uw.tcss450.stran373.R;
import edu.uw.tcss450.stran373.io.RequestQueueSingleton;

public class ChatMembersViewModel extends AndroidViewModel {

    private final MutableLiveData<List<ChatMember>> mUsers;

    public ChatMembersViewModel(@NonNull final Application application) {
        super(application);
        mUsers = new MutableLiveData<>();
        mUsers.setValue(new ArrayList<>());
    }

    public void addChatMemberObserver(@NonNull LifecycleOwner owner,
                                      @NonNull Observer<? super List<ChatMember>> observer) {
        mUsers.observe(owner, observer);
    }

    public void getUsers(final int chatId, final String jwt) {
        String url = getApplication().getResources().getString(R.string.base_url) + "chats/" + chatId + '/';

        Request request = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                this::handleGetSuccess,
                this::handleError) {

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                // add headers <key,value>
                headers.put("Authorization", jwt);
                return headers;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Instantiate the RequestQueue and add the request to the queue
        RequestQueueSingleton.getInstance(getApplication().getApplicationContext())
                .addToRequestQueue(request);
    }

    public void deleteUser(final int chatId, final String jwt, final int memberid) {
        String url = getApplication().getResources().getString(R.string.base_url) + "chats/" + chatId + "/members?p=" + memberid;

        Response.Listener<JSONObject> handleDeleteSuccess = (theJSONObject) -> getUsers(chatId, jwt);

        Request request = new JsonObjectRequest(
                Request.Method.DELETE,
                url,
                null,
                handleDeleteSuccess,
                this::handleError) {

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                // add headers <key,value>
                headers.put("Authorization", jwt);
                return headers;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Instantiate the RequestQueue and add the request to the queue
        RequestQueueSingleton.getInstance(getApplication().getApplicationContext())
                .addToRequestQueue(request);
    }

    public void addUser(final int chatId, final String jwt, final int memberid) {
        Log.d("ADD", chatId + ": " + jwt + " - " + memberid);
        String url = getApplication().getResources().getString(R.string.base_url) + "chats/" + chatId + "/members?p=" + memberid;

        Response.Listener<JSONObject> handleAddSuccess = (theJSONObject) -> getUsers(chatId, jwt);

        Log.d("ADD URL", url);
        Request request = new JsonObjectRequest(
                Request.Method.PUT,
                url,
                null,
                handleAddSuccess,
                this::handleError) {

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                // add headers <key,value>
                headers.put("Authorization", jwt);
                return headers;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Instantiate the RequestQueue and add the request to the queue
        RequestQueueSingleton.getInstance(getApplication().getApplicationContext())
                .addToRequestQueue(request);
    }

    private void handleGetSuccess(final JSONArray theJSONArray) {
        final List<ChatMember> members = mUsers.getValue();
        members.clear();
        for (int i = 0; i < theJSONArray.length(); i++) {
            try {
                members.add(new ChatMember(theJSONArray.getJSONObject(i).getInt("memberid"),
                        theJSONArray.getJSONObject(i).getString("email")));
            } catch(JSONException e) {

            }
        }
        mUsers.setValue(members);
    }

    public List<ChatMember> getChatMembers() {
        return mUsers.getValue();
    }

    /**
     * Handles unsuccessful response
     *
     * @param error the error
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
}
