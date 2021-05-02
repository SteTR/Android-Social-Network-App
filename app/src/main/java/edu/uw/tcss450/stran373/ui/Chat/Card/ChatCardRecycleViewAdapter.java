package edu.uw.tcss450.stran373.ui.Chat.Card;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.uw.tcss450.stran373.R;
import edu.uw.tcss450.stran373.databinding.FragmentChatCardBinding;
import edu.uw.tcss450.stran373.ui.Chat.ChatListFragmentDirections;

/**
 * A RecycleView holder to hold the chat cards
 * @author Steven Tran
 */
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
        holder.setChat(mChatCards.get(position));
    }

    @Override
    public int getItemCount() {
        return mChatCards.size();
    }

    public static class ChatCardViewHolder extends RecyclerView.ViewHolder {

        public final View mView;
        public FragmentChatCardBinding binding;
        private ChatCard mChat;

        public ChatCardViewHolder(@NonNull final View itemView) {
            super(itemView);
            mView = itemView;
            binding = FragmentChatCardBinding.bind(itemView);
        }

        /**
         * Listener to open the chat corresponding to the chat card
         *
         * @param chat a chat card
         */
        private void setChat(final ChatCard chat)
        {
            mChat = chat;

            // Moves to the chat id in the chatcard
            binding.cardConstraint.setOnClickListener(view ->
                    Navigation.findNavController(mView).navigate(ChatListFragmentDirections
                            .actionNavigationChatsToNavigationSingleChat(mChat.getChatID())));
        }
    }
}
