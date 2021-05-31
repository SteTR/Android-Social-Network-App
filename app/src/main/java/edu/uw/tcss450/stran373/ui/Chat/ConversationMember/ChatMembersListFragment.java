package edu.uw.tcss450.stran373.ui.Chat.ConversationMember;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.uw.tcss450.stran373.R;
import edu.uw.tcss450.stran373.UserInfoViewModel;
import edu.uw.tcss450.stran373.databinding.FragmentChatMembersListBinding;

/**
 * create an instance of this fragment.
 */
public class ChatMembersListFragment extends Fragment {

    private ChatMembersViewModel mChatMembersVM;
    private UserInfoViewModel mUserViewModel;

    public ChatMembersListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mChatMembersVM = new ViewModelProvider(getActivity()).get(ChatMembersViewModel.class);
        mUserViewModel = new ViewModelProvider(getActivity()).get(UserInfoViewModel.class);
        mChatMembersVM.getUsers(ChatMembersListFragmentArgs.fromBundle(getArguments()).getChatid(),
                mUserViewModel.getJwt());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat_members_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final FragmentChatMembersListBinding binding = FragmentChatMembersListBinding.bind(getView());
        final RecyclerView rv = binding.listRecyclerChatMembers;

        //Set the Adapter to hold a reference to the list the chat ID that the ChatViewModel holds.
        rv.setAdapter(new ChatMembersRecycleViewAdapter(mChatMembersVM.getChatMembers(),
                ChatMembersListFragmentArgs.fromBundle(getArguments()).getChatid()));

        mChatMembersVM.addChatMemberObserver(getViewLifecycleOwner(), (response) -> {
            rv.getAdapter().notifyDataSetChanged();
        });
    }
}