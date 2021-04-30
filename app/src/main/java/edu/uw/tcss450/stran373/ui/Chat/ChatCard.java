package edu.uw.tcss450.stran373.ui.Chat;

import java.io.Serializable;
import java.util.Date;

public class ChatCard implements Serializable {
    private final String mName;
    private final String mLastMessage;
    private final Date mTime;
//    private final _ mPicture;

    public static class Builder {
        private final String mName;
        private final String mLastMessage;
        private final Date mTime;
//        private final _ mPicture;

        public Builder(final String name, final String lastmessage, final Date time) // , final _ picture
        {
            this.mName = name;
            this.mLastMessage = lastmessage;
            this.mTime = time;
        }

        public ChatCard build()
        {
            return new ChatCard(this);
        }
    }

    private ChatCard(final Builder builder)
    {
        this.mName = builder.mName;
        this.mLastMessage = builder.mLastMessage;
//        this.mPicture = builder.mPicture;
        this.mTime = builder.mTime;
    }
}
