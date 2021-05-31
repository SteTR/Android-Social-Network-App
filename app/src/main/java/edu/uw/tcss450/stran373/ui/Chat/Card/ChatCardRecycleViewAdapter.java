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

/**
 * A RecycleView holder to hold the chat cards
 *
 * @author Steven Tran
 * @author Haoying Li
 */
public class ChatCardRecycleViewAdapter extends RecyclerView.Adapter<ChatCardRecycleViewAdapter.ChatCardViewHolder> {

    // List of chat cards to be displayed
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

        public ChatCardViewHolder(@NonNull final View itemView) {
            super(itemView);
            mView = itemView;
            binding = FragmentChatCardBinding.bind(itemView);
        }

        /**
         * Sets the chat to the holder and sets the button to navigate to the single chat fragment
         *
         * @param chat a chat card
         */
        private void setChat(final ChatCard chat)
        {
            binding.textLastmessage.setText(chat.getLastMessage());
            binding.textName.setText(chat.getName());
//            binding.textTime.setText(chat.getTime());

            // Moves to the chat id in the chatcard
            binding.cardConstraint.setOnClickListener(view ->
                    Navigation.findNavController(mView).navigate(ChatCardListFragmentDirections
                            .actionNavigationChatsToNavigationSingleChat(chat.getChatID(), chat.getName())));
        }
    }
}
