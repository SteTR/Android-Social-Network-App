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

import edu.uw.tcss450.stran373.UserInfoViewModel;
import edu.uw.tcss450.stran373.databinding.FragmentChatBinding;

/**
 * Create a fragment that represents a chat. Contains the list of messages of one chat
 * @author Steven Tran
 * TODO currently treating chat card and chat message as two different objects that store the same thing, maybe both will store a chat object that contains both.
 */
public class ChatFragment extends Fragment {

    private FragmentChatBinding binding;
    private ChatViewModel mChatViewModel;
    private UserInfoViewModel mUserViewModel;
    private ChatSendViewModel mSendViewModel;

    private int mChatId;

    public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ChatFragmentArgs args = ChatFragmentArgs.fromBundle(getArguments());
        mChatId = args.getChatId();

        ViewModelProvider provider = new ViewModelProvider(getActivity());
        mUserViewModel = provider.get(UserInfoViewModel.class);
        mChatViewModel = provider.get(ChatViewModel.class);
        mChatViewModel.getFirstMessages(mChatId, mUserViewModel.getJwt());
        mSendViewModel = provider.get(ChatSendViewModel.class);
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

        binding = FragmentChatBinding.bind(getView());
        binding.swipeContainer.setRefreshing(true);

        ChatFragmentArgs chatArguments = ChatFragmentArgs.fromBundle(getArguments());

        final RecyclerView rv = binding.recyclerMessages;

        rv.setAdapter(new ChatRecyclerViewAdapter(
                mChatViewModel.getMessageListByChatId(mChatId),
                mUserViewModel.getEmail()));

        binding.swipeContainer.setOnRefreshListener(() -> {
            mChatViewModel.getNextMessages(mChatId, mUserViewModel.getJwt());
        });

        mChatViewModel.addMessageObserver(mChatId, getViewLifecycleOwner(), list -> {
            rv.getAdapter().notifyDataSetChanged();
            rv.scrollToPosition(rv.getAdapter().getItemCount() - 1);
            binding.swipeContainer.setRefreshing(false);
        });

        //Send button was clicked. Send the message via the SendViewModel
        binding.buttonSend2.setOnClickListener(button -> {
            mSendViewModel.sendMessage(mChatId,
                    mUserViewModel.getJwt(),
                    binding.editMessage.getText().toString());
        });

        //when we get the response back from the server, clear the edittext
        mSendViewModel.addResponseObserver(getViewLifecycleOwner(), response -> {
            binding.editMessage.setText("");
        });
    }
}