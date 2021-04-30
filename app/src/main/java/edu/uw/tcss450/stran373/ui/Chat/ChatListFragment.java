package edu.uw.tcss450.stran373.ui.Chat;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import edu.uw.tcss450.stran373.R;
import edu.uw.tcss450.stran373.databinding.FragmentChatListBinding;

/**
 * create an instance of this fragment.
 */
public class ChatListFragment extends Fragment {

    private ChatListViewModel mChatListViewModel;

    public ChatListFragment() {
        // Required empty public constructor
    }

    /**
     * Temporary method to generate chat cards in random amounts ranging from 1 to 10.
     * @return a list of chat cards
     */
    private static List<ChatCard> ChatCardGenerator()
    {
        final List<ChatCard> chatCards = new ArrayList<>();
        for (int i = 0; i < Math.random() * 10 + 1; i++)
        {
            ChatCard tempCard = new ChatCard.Builder("", "", null).build();
            chatCards.add(tempCard);
        }
        return chatCards;
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
        binding.listRecyclerViewChat.setAdapter(new ChatCardRecycleViewAdapter(ChatCardGenerator()));

    }
}