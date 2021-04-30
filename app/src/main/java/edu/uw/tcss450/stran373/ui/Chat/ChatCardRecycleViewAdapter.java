package edu.uw.tcss450.stran373.ui.Chat;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.uw.tcss450.stran373.R;
import edu.uw.tcss450.stran373.databinding.FragmentChatCardBinding;

public class ChatCardRecycleViewAdapter extends RecyclerView.Adapter<ChatCardRecycleViewAdapter.ChatCardViewHolder> {

    private final List<ChatCard> mChatCards;

    public ChatCardRecycleViewAdapter(final List<ChatCard> theChatCards)
    {
        mChatCards = theChatCards;
    }

    @NonNull
    @Override
    public ChatCardViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
        return new ChatCardViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_chat_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ChatCardViewHolder holder, final int position) {

    }

    @Override
    public int getItemCount() {
        return mChatCards.size();
    }

    public static class ChatCardViewHolder extends RecyclerView.ViewHolder {

        public final View mView;
        public FragmentChatCardBinding binding;
        private ChatCard mBlog;

        public ChatCardViewHolder(@NonNull final View itemView) {
            super(itemView);
            mView = itemView;
            binding = FragmentChatCardBinding.bind(itemView);
            binding.cardConstraint.setOnClickListener(this::openChat);
        }

        /**
         * Listener to open the chat corresponding to the chat card
         *
         * @param button a chat card
         */
        private void openChat(final View button)
        {
            Log.d("Button was clicked", button.toString());
        }
    }
}
