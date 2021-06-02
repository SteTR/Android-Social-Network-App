package edu.uw.tcss450.stran373.ui.Chat.Card;

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

import edu.uw.tcss450.stran373.R;
import edu.uw.tcss450.stran373.io.RequestQueueSingleton;
import edu.uw.tcss450.stran373.ui.Chat.Card.ChatCard;

/**
 * A view model to hold all the chat cards as a list
 *
 * @author Steven Tran
 * @author Haoying Li
 */
public class ChatListViewModel extends AndroidViewModel {

    private final MutableLiveData<List<ChatCard>> mCardList;

    public ChatListViewModel(@NonNull final Application application) {
        super(application);
        mCardList = new MutableLiveData<>(new ArrayList<>());
    }

    /**
     * Makes the owner observe the lists for any updates
     *
     * @param owner owner to watch
     * @param observer the function to perform upon observation
     */
    public void addChatListObserver(@NonNull LifecycleOwner owner, @NonNull Observer<? super List<ChatCard>> observer) {
        mCardList.observe(owner, observer);
    }

    /**
     * Returns the value of the chat card list
     *
     * @return the card list
     */
    public List<ChatCard> getCardList()
    {
        return mCardList.getValue();
    }

    public List<ChatCard> getRecentCardList() {
        List<ChatCard> temp = new ArrayList<>();
        if (mCardList.getValue().size() >= 1)
            temp.add(mCardList.getValue().get(0));
        if (mCardList.getValue().size() >= 2)
            temp.add(mCardList.getValue().get(1));
        return temp;
    }

    /**
     * Connects to the web service to get chats that the user is part of
     *
     * @param jwt the user's token
     */
    public void getChats(final String jwt) {
        String url = getApplication().getResources().getString(R.string.base_url) + "chats";

        Request request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null, //no body for this get request
                this::handleSuccess,
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

    /**
     * Handles successful response and stores the chats into a list
     *
     * @param response the response of web service
     */
    public void handleSuccess(final JSONObject response) {
        List<ChatCard> list = new ArrayList<>();
        try {
            JSONArray chatcards = response.getJSONArray("chats");
            for (int i = 0; i < chatcards.length(); i++) {
                JSONObject chatcard = chatcards.getJSONObject(i);
                ChatCard card = new ChatCard.Builder(
                        chatcard.getString("groupname"),
                        chatcard.getString("message"),
                        chatcard.getString("timestamp"),
                        chatcard.getInt("chatid")
                ).build();
                if (!list.contains(card)) {
                    // don't add a duplicate
                    list.add(0, card);
                } else {
                    // this shouldn't happen but could with the asynchronous
                    // nature of the application
                    Log.wtf("Chat already received",
                            "Or duplicate id:" + card.getChatID());
                }
            }
            mCardList.setValue(list);
        }catch (JSONException e) {
            Log.e("JSON PARSE ERROR", "Found in handle Success ChatViewModel");
            Log.e("JSON PARSE ERROR", "Error: " + e.getMessage());
        }
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