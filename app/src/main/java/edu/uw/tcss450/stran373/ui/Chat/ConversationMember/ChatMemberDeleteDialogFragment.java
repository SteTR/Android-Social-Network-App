package edu.uw.tcss450.stran373.ui.Chat.ConversationMember;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import edu.uw.tcss450.stran373.R;
import edu.uw.tcss450.stran373.UserInfoViewModel;
import edu.uw.tcss450.stran373.utils.Utils;

/**
 * Dialog Fragment to handle deleting a chat member
 *
 * @author Steven Tran
 */
public class ChatMemberDeleteDialogFragment extends DialogFragment {

    private ChatMembersViewModel mChatMembersVM;
    private final int mChatMemberId;
    private final String mName;
    private final int mChatId;
    private UserInfoViewModel mUserViewModel;

    public ChatMemberDeleteDialogFragment(final String name, final int chatMemberId, final int chatId) {
        super();
        mChatMemberId = chatMemberId;
        mName = name;
        mChatId = chatId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        mChatMembersVM = new ViewModelProvider(getActivity()).get(ChatMembersViewModel.class);
        mUserViewModel = new ViewModelProvider(getActivity()).get(UserInfoViewModel.class);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    /**
     * Method used to create a diolog message for the user
     * AKA a "pop-up" window.
     * @param savedInstanceState Bundle from previous state
     * @return The new Dialog window
     */
    @Override
    public Dialog onCreateDialog(@NonNull Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(mName)
                .setItems(new String[]{"DELETE"}, (dialog, which) -> {
                    mChatMembersVM.deleteUser(mChatId, mUserViewModel.getJwt(), mChatMemberId);
                    dismiss();
                });
        return builder.create();
    }
}
