package edu.uw.tcss450.stran373.ui.Chat.Conversation;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import edu.uw.tcss450.stran373.R;
import edu.uw.tcss450.stran373.io.RequestQueueSingleton;
import edu.uw.tcss450.stran373.ui.Chat.Message.ChatMessage;

/**
 * A view model that keeps track of all the chats by id and their messages
 *
 * @author Steven Tran
 * @author Haoying Li
 */
public class ChatViewModel extends AndroidViewModel {

//    public static final int TEST_CHAT_ID = 1;

    /**
     * A Map of Lists of Chat Messages.
     * The Key represents the Chat ID
     * The value represents the List of (known) messages for that that room.
     */
    private final Map<Integer, MutableLiveData<List<ChatMessage>>> mMessages;

    public ChatViewModel(@NonNull Application application) {
        super(application);
        mMessages = new HashMap<>();
    }

    /**
     * Register as an observer to listen to a specific chat room's list of messages.
     *
     * @param chatId   the chatid of the chat to observer
     * @param owner    the fragments lifecycle owner
     * @param observer the observer
     */
    public void addMessageObserver(int chatId,
                                   @NonNull LifecycleOwner owner,
                                   @NonNull Observer<? super List<ChatMessage>> observer) {
        getOrCreateMapEntry(chatId).observe(owner, observer);
    }

    /**
     * Return a reference to the List<> associated with the chat room. If the View Model does
     * not have a mapping for this chatID, it will be created.
     * <p>
     * WARNING: While this method returns a reference to a mutable list, it should not be
     * mutated externally in client code. Use public methods available in this class as
     * needed.
     *
     * @param chatId the id of the chat room List to retrieve
     * @return a reference to the list of messages
     */
    public List<ChatMessage> getMessageListByChatId(final int chatId) {
        return getOrCreateMapEntry(chatId).getValue();
    }

    private MutableLiveData<List<ChatMessage>> getOrCreateMapEntry(final int chatId) {
        if (!mMessages.containsKey(chatId)) {
            mMessages.put(chatId, new MutableLiveData<>(new ArrayList<>()));
        }
        return mMessages.get(chatId);
    }

    /**
     * Makes a request to the web service to get the first batch of messages for a given Chat Room.
     * Parses the response and adds the ChatMessage object to the List associated with the
     * ChatRoom. Informs observers of the update.
     * <p>
     * Subsequent requests to the web service for a given chat room should be made from
     * getNextMessages()
     *
     * @param chatId the chatroom id to request messages of
     * @param jwt    the users signed JWT
     */
    public void getFirstMessages(final int chatId, final String jwt) {
        String url = getApplication().getResources().getString(R.string.base_url) +
                "messages/" + chatId;

        Request request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null, //no body for this get request
                this::handelSuccess,
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
     * Makes a request to the web service to get the next batch of messages for a given Chat Room.
     * This request uses the earliest known ChatMessage in the associated list and passes that
     * messageId to the web service.
     * Parses the response and adds the ChatMessage object to the List associated with the
     * ChatRoom. Informs observers of the update.
     * <p>
     * Subsequent calls to this method receive earlier and earlier messages.
     *
     * @param chatId the chatroom id to request messages of
     * @param jwt    the users signed JWT
     */
    public void getNextMessages(final int chatId, final String jwt) {
        String url = getApplication().getResources().getString(R.string.base_url) +
                "messages/" +
                chatId +
                "/" +
                mMessages.get(chatId).getValue().get(0).getMessageId();

        Request request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null, //no body for this get request
                this::handelSuccess,
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
     * When a chat message is received externally to this ViewModel, add it
     * with this method.
     *
     * @param chatId
     * @param message
     */
    public void addMessage(final int chatId, final ChatMessage message) {
        List<ChatMessage> list = getMessageListByChatId(chatId);
        list.add(message);
        getOrCreateMapEntry(chatId).setValue(list);
    }

    /**
     * Handles successful response and stores messages into a list
     *
     * @param response the response
     */
    private void handelSuccess(final JSONObject response) {
        List<ChatMessage> list;
        if (!response.has("chatid")) {
            throw new IllegalStateException("Unexpected response in ChatViewModel: " + response);
        }
        try {
            list = getMessageListByChatId(response.getInt("chatid"));
            JSONArray messages = response.getJSONArray("rows");
            for (int i = 0; i < messages.length(); i++) {
                JSONObject message = messages.getJSONObject(i);
                ChatMessage cMessage = new ChatMessage(
                        message.getInt("messageid"),
                        message.getString("message"),
                        message.getString("email"),
                        message.getString("timestamp")
                );
                if (!list.contains(cMessage)) {
                    // don't add a duplicate
                    list.add(0, cMessage);
                } else {
                    // this shouldn't happen but could with the asynchronous
                    // nature of the application
                    Log.wtf("Chat message already received",
                            "Or duplicate id:" + cMessage.getMessageId());
                }
            }
            //inform observers of the change (setValue)
            getOrCreateMapEntry(response.getInt("chatid")).setValue(list);
        } catch (JSONException e) {
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
        } else {
            String data = new String(error.networkResponse.data, Charset.defaultCharset());
            Log.e("CLIENT ERROR",
                    error.networkResponse.statusCode +
                            " " +
                            data);
        }
    }
}
