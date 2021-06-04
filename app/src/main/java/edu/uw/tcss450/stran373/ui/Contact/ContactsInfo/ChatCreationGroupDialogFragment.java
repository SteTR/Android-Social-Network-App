package edu.uw.tcss450.stran373.ui.Contact.ContactsInfo;

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

import com.google.android.material.snackbar.Snackbar;

import java.util.List;
import java.util.Objects;

import edu.uw.tcss450.stran373.R;
import edu.uw.tcss450.stran373.UserInfoViewModel;
import edu.uw.tcss450.stran373.ui.Chat.ConversationMember.ChatMembersViewModel;

public class ChatCreationGroupDialogFragment extends DialogFragment {

    /**
     * View model for the contact list
     */
    private ContactListViewModel mModel;

    /**
     * User info view model to get the JWT
     */
    UserInfoViewModel mUserViewModel;

    private final List<ContactCard> mContacts;

    ChatCreationGroupDialogFragment(final List<ContactCard> theContacts) {
        mContacts = theContacts;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        mModel = new ViewModelProvider(requireActivity()).get(ContactListViewModel.class);
        mUserViewModel = new ViewModelProvider(getActivity()).get(UserInfoViewModel.class);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable final Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Group Name");
        builder.setMessage("Please set the name of the group. \nNOTE: name cannot be changed after creation");
        final EditText input = new EditText(builder.getContext());
        input.setHint("Group Name");
        input.setText("");
        input.setInputType(InputType.TYPE_CLASS_TEXT);


        builder.setView(input);

        builder.setPositiveButton("Confirm", (dialog, which) -> {
            mModel.handleChatCreation(input.getText().toString(), mContacts,
                    mUserViewModel.getJwt());
            if (!input.getText().toString().isEmpty()) Snackbar.make(getParentFragment().requireView(),
                    R.string.text_chat_created, Snackbar.LENGTH_LONG).show();
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        return builder.create();
    }
}