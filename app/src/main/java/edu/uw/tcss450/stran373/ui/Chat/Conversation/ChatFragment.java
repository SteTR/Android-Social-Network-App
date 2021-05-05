package edu.uw.tcss450.stran373.ui.Chat.Conversation;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.uw.tcss450.stran373.databinding.FragmentChatBinding;

import static edu.uw.tcss450.stran373.ui.Chat.Conversation.ChatViewModel.TEST_CHAT_ID;
import static edu.uw.tcss450.stran373.ui.Chat.Conversation.ChatViewModel.USER_NAME;

/**
 * Create a fragment that represents a chat. Contains the list of messages of one chat
 * @author Steven Tran
 * TODO currently treating chat card and chat message as two different objects that store the same thing, maybe both will store a chat object that contains both.
 */
public class ChatFragment extends Fragment {

    private FragmentChatBinding binding;
    private ChatViewModel mChatViewModel;

    public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mChatViewModel = new ViewModelProvider(getActivity()).get(ChatViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentChatBinding.inflate(inflater, container, false);
        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // TODO atm, getting only chat id 1, need to update to get other chats with different Ids
        ChatFragmentArgs chatArguments = ChatFragmentArgs.fromBundle(getArguments());


        final RecyclerView rv = binding.recyclerMessages;
        rv.setAdapter(new ChatRecyclerViewAdapter(mChatViewModel.getMessageListByChatId(TEST_CHAT_ID), USER_NAME));
        mChatViewModel.addMessageObserver(TEST_CHAT_ID, getViewLifecycleOwner(), list ->
        {
            // TODO temporary solution
            rv.getAdapter();
            rv.scrollToPosition(rv.getAdapter().getItemCount() - 1);
            binding.swipeContainer.setRefreshing(false);
        });
    }
}