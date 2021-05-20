package edu.uw.tcss450.stran373.ui.Chat.Card;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.uw.tcss450.stran373.R;

/**
 * create an instance of this chat card fragment.
 * Currently not used for anything besides representing a card
 *
 * @author Steven Tran
 */
public class ChatCardFragment extends Fragment {

    public ChatCardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View chatCardView = inflater.inflate(R.layout.fragment_chat_card, container, false);
        return inflater.inflate(R.layout.fragment_chat_card, container, false);
    }
}