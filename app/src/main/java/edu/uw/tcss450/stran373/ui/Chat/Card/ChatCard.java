package edu.uw.tcss450.stran373.ui.Chat.Card;

import java.io.Serializable;
import java.util.Date;

/**
 * Represents a chat box in the lists of chats to display
 * @author Steven Tran
 */
public class ChatCard implements Serializable {
    private final String mName;
    private final String mLastMessage;
    private final Date mTime;
    private final int mChatId; // TODO maybe not have the chat card store the chat id but move it to the chat list.
//    private final _ mPicture;

    /**
     * Necessary builder function to build the chat card
     * @author Steven Tran
     */
    public static class Builder {
        private String mName;
        private String mLastMessage;
        private Date mTime;
        private int mChatId;
//        private final _ mPicture;

        public Builder() // , final _ picture
        {
            mName = "";
            mLastMessage = "";
            mTime = null;
            mChatId = 0;
        }

        public Builder(final String name, final String lastmessage, final Date time, final int chatId) // , final _ picture
        {
            this.mName = name;
            this.mLastMessage = lastmessage;
            this.mTime = time;
            this.mChatId = chatId;
        }

        /**
         * Sets the name for the builder of chat card
         * @param theMName name of the chat card
         */
        public void setName(final String theMName) {
            mName = theMName;
        }

        /**
         * Sets the last message for the builder of chat card
         * @param theMLastMessage last message of the chat card
         */
        public void setLastMessage(final String theMLastMessage) {
            mLastMessage = theMLastMessage;
        }

        /**
         * Sets the time for the builder of chat card
         * @param theMTime time of last message sent by anyone in the chat of the chat card
         */
        public void setTime(final Date theMTime) {
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
         * @return ChatCard
         */
        public ChatCard build()
        {
            return new ChatCard(this);
        }
    }

    /**
     * Builds the Chat Card
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
     * @return Integer chat id
     */
    public int getChatID()
    {
        return mChatId;
    }
}
