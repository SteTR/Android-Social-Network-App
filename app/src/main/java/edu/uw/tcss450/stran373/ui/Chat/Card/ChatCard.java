package edu.uw.tcss450.stran373.ui.Chat.Card;

import java.io.Serializable;

/**
 * Represents a chat box in the lists of chats to display
 *
 * @author Steven Tran
 * @author Haoying Li
 */
public class ChatCard implements Serializable {
    private final String mName;
    private final String mLastMessage;
    private final String mTime;
    private final int mChatId;
//    private final _ mPicture;

    /**
     * Necessary builder function to build the chat card
     *
     * @author Steven Tran
     */
    public static class Builder {
        private String mName;
        private String mLastMessage;
        private String mTime;
        private int mChatId;
//        private final _ mPicture;

        public Builder() // , final _ picture
        {
            mName = "";
            mLastMessage = "";
            mTime = "";
            mChatId = 0;
        }

        public Builder(final String name, final String lastmessage, final String time, final int chatId) // , final _ picture
        {
            this.mName = name;
            this.mLastMessage = lastmessage;
            this.mChatId = chatId;
            this.mTime = time;
        }

        /**
         * Sets the name for the builder of chat card
         *
         * @param theMName name of the chat card
         */
        public void setName(final String theMName) {
            mName = theMName;
        }

        /**
         * Sets the last message for the builder of chat card
         *
         * @param theMLastMessage last message of the chat card
         */
        public void setLastMessage(final String theMLastMessage) {
            mLastMessage = theMLastMessage;
        }

        /**
         * Sets the time for the builder of chat card
         *
         * @param theMTime time of last message sent by anyone in the chat of the chat card
         */
        public void setTime(final String theMTime) {
            mTime = theMTime;
        }

        /**
         * Set the chat id of the chat card
         *
         * @param theMChatId integer chat id
         */
        public void setChatId(final int theMChatId) {
            mChatId = theMChatId;
        }

        /**
         * Builds the chat card (equal to new Chatcard(params))
         *
         * @return ChatCard
         */
        public ChatCard build()
        {
            return new ChatCard(this);
        }
    }

    /**
     * Builds the Chat Card
     *
     * @param builder Builder in chat card that carries same param as chat card
     */
    private ChatCard(final Builder builder)
    {
        this.mName = builder.mName;
        this.mLastMessage = builder.mLastMessage;
//        this.mPicture = builder.mPicture;
        this.mTime = builder.mTime;
        this.mChatId = builder.mChatId;
    }

    /**
     * Returns the id corresponding to the chat card
     *
     * @return Integer chat id
     */
    public int getChatID()
    {
        return mChatId;
    }

    /**
     * Returns the chat name of the chat card
     *
     * @return the chat name
     */
    public String getName() {
        if (mName.length() <= 30)
            return mName;
        else
            return mName.substring(0, 29) + "...";
    }

    /**
     * Returns the last message in the chat
     *
     * @return the last message
     */
    public String getLastMessage() {
        if (mLastMessage.length() <= 40)
            return mLastMessage;
        else
            return mLastMessage.substring(0, 39) + "...";
    }

    /**
     * Returns the timestamp of the last message in the chat
     *
     * @return the timestamp
     */
    public String getTime() {
        if (mTime.equals("null"))
            return "";
        else
            return mTime.substring(11,19);
    }
}
