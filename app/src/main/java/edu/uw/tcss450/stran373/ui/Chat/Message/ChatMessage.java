package edu.uw.tcss450.stran373.ui.Chat.Message;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * A class that encapsulates all the necessary information for a chat message
 *
 * @author Steven Tran
 * @author Haoying Li
 */
public class ChatMessage implements Serializable {
    private final int mMessageId;
    private final String mMessage;
    private final String mSender;
    private final String mTimeStamp;

    public ChatMessage(int messageId, String message, String sender, String timeStamp) {
        mMessageId = messageId;
        mMessage = message;
        mSender = sender;
        mTimeStamp = timeStamp;
    }

    /**
     * Static factory method to turn a properly formatted JSON String into a
     * ChatMessage object.
     *
     * @param cmAsJson the String to be parsed into a ChatMessage Object.
     * @return a ChatMessage Object with the details contained in the JSON String.
     * @throws JSONException when cmAsString cannot be parsed into a ChatMessage.
     */
    public static ChatMessage createFromJsonString(final String cmAsJson) throws JSONException {
        final JSONObject msg = new JSONObject(cmAsJson);
        return new ChatMessage(msg.getInt("messageid"),
                msg.getString("message"),
                msg.getString("email"),
                msg.getString("timestamp"));
    }

    /**
     * Returns the content of the message
     *
     * @return the message
     */
    public String getMessage() {
        return mMessage;
    }

    /**
     * Returns the sender email
     *
     * @return the sender email
     */
    public String getSender() {
        return mSender;
    }

    /**
     * Returns the date of the message
     * @return the date
     */
    public String getDate() {
        return mTimeStamp.split(" ")[0];
    }

    /**
     * Returns the timestamp of the message
     *
     * @return the timestamp
     */
    public String getTime() {
        return mTimeStamp.substring(11,19);
    }

    /**
     * Returns the message ID
     * @return the message ID
     */
    public int getMessageId() {
        return mMessageId;
    }

    /**
     * Provides equality solely based on MessageId.
     *
     * @param other the other object to check for equality
     * @return true if other message ID matches this message ID, false otherwise
     */
    @Override
    public boolean equals(@Nullable Object other) {
        boolean result = false;
        if (other instanceof ChatMessage) {
            result = mMessageId == ((ChatMessage) other).mMessageId;
        }
        return result;
    }

    @NonNull
    @Override
    public String toString() {
        return mMessageId + ": " + mSender + ":" + mMessage + ". " + mTimeStamp;
    }
}
