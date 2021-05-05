package edu.uw.tcss450.stran373.ui.Chat.Card;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.uw.tcss450.stran373.R;
import edu.uw.tcss450.stran373.databinding.FragmentChatListBinding;

/**
 * Create a fragment that lists all the chat cards before entering a chat.
 * Not changed at the moment.
 * @author Steven Tran
 */
public class ChatCardListFragment extends Fragment {

    private ChatListViewModel mChatListViewModel;

    public ChatCardListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mChatListViewModel = new ViewModelProvider(getActivity()).get(ChatListViewModel.class);
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
        binding.listRecyclerViewChat.setAdapter(new ChatCardRecycleViewAdapter(mChatListViewModel.getCardList()));

    }
}