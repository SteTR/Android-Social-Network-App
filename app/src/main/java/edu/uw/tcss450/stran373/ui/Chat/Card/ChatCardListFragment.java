package edu.uw.tcss450.stran373.ui.Chat.Card;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.uw.tcss450.stran373.R;
import edu.uw.tcss450.stran373.UserInfoViewModel;
import edu.uw.tcss450.stran373.databinding.FragmentChatBinding;
import edu.uw.tcss450.stran373.databinding.FragmentChatListBinding;
import edu.uw.tcss450.stran373.ui.Chat.Conversation.ChatViewModel;
import edu.uw.tcss450.stran373.ui.Chat.Conversation.ChatFragment;

/**
 * Create a fragment that lists all the chat cards before entering a chat.
 * Not changed at the moment.
 * @author Steven Tran
 */
public class ChatCardListFragment extends Fragment {

    private ChatListViewModel mChatListViewModel;
    private UserInfoViewModel mUserViewModel;

    public ChatCardListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserViewModel = new ViewModelProvider(getActivity()).get(UserInfoViewModel.class);
        mChatListViewModel = new ViewModelProvider(getActivity()).get(ChatListViewModel.class);
        mChatListViewModel.getChats(mUserViewModel.getJwt());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FragmentChatListBinding binding = FragmentChatListBinding.bind(getView());

        mChatListViewModel.addChatListObserver(getViewLifecycleOwner(), chatlist -> {
            if (!chatlist.isEmpty()) {
                binding.listRecyclerViewChat.setAdapter(new ChatCardRecycleViewAdapter(mChatListViewModel.getCardList()));
            }
        });

//        final RecyclerView rv = binding.listRecyclerViewChat;
//        rv.setAdapter(new ChatCardRecycleViewAdapter(mChatListViewModel.getCardList()));

        // Navigates to the sample chat message for now
        binding.listChatAddButton.setOnClickListener(button ->
                Navigation.findNavController(getView()).navigate(ChatCardListFragmentDirections
                        .actionNavigationChatsToNavigationSingleChat(ChatViewModel.TEST_CHAT_ID)));
    }
}