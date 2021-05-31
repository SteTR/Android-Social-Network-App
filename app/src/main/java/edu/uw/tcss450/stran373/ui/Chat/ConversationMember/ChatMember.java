package edu.uw.tcss450.stran373.ui.Chat.ConversationMember;

public class ChatMember {
    private final int mMemberId;
    private final String mName;

    public ChatMember(final int memberid, final String name) {
        this.mMemberId = memberid;
        this.mName = name;
    }

    public int getMemberId() {
        return mMemberId;
    }

    public String getName() {
        return mName;
    }
}
