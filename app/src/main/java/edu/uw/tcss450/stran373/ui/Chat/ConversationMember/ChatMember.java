package edu.uw.tcss450.stran373.ui.Chat.ConversationMember;

/**
 * Object to hold the member id and name for chat members list
 *
 * (literally only to hold information because it's not js)
 *
 * @author Steven Tran
 */
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
