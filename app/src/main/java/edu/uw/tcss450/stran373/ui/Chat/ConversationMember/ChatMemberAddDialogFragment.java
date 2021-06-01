package edu.uw.tcss450.stran373.ui.Chat.ConversationMember;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import edu.uw.tcss450.stran373.UserInfoViewModel;

/**
 * Dialog Fragment to handle adding a chat member
 *
 * @author Steven Tran
 */
public class ChatMemberAddDialogFragment extends DialogFragment {
    private ChatMembersViewModel mChatMembersVM;
    private final int mChatId;
    private UserInfoViewModel mUserViewModel;

    public ChatMemberAddDialogFragment(final int chatId) {
        super();
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
     *
     * @param savedInstanceState Bundle from previous state
     * @return The new Dialog window
     */
    @Override
    public Dialog onCreateDialog(@NonNull Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("ADD");
        final EditText input = new EditText(builder.getContext());

        input.setInputType(InputType.TYPE_CLASS_TEXT);

        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mChatMembersVM.addUser(mChatId, mUserViewModel.getJwt(), input.getText().toString());
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        return builder.create();
    }

}
