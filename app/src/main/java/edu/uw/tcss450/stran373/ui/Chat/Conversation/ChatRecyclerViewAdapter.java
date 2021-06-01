package edu.uw.tcss450.stran373.ui.Chat.Conversation;

import android.content.res.Resources;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.ColorUtils;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.uw.tcss450.stran373.R;
import edu.uw.tcss450.stran373.databinding.FragmentChatReceiveMessageBinding;
import edu.uw.tcss450.stran373.databinding.FragmentChatSendMessageBinding;
import edu.uw.tcss450.stran373.ui.Chat.Message.ChatMessage;

/**
 * A recycle view adapter to keep track of a specific chat's messages and the owner
 *
 * @author Steven Tran
 * @author Charles Bryan
 */
public class ChatRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int SEND_MESSAGE_VIEW_TYPE = 1;
    private static final int RECEIVE_MESSAGE_VIEW_TYPE = 2;

    private final List<ChatMessage> mMessages;
    private final String mEmail;

    public ChatRecyclerViewAdapter(List<ChatMessage> messages, String email) {
        this.mMessages = messages;
        mEmail = email;
    }

    @Override
    public int getItemViewType(final int position) {
        // Checks the message if it belongs to user or other and puts a view type on them
        final ChatMessage message = mMessages.get(position);
        if (message.getSender().equals(mEmail)) {
            return SEND_MESSAGE_VIEW_TYPE;
        } else {
            return RECEIVE_MESSAGE_VIEW_TYPE;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        if (viewType == SEND_MESSAGE_VIEW_TYPE) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_chat_send_message, parent, false);
            return new ChatRecyclerViewAdapter.SendMessageViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_chat_receive_message, parent, false);
            return new ChatRecyclerViewAdapter.ReceiveMessageViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == SEND_MESSAGE_VIEW_TYPE) {
            ((SendMessageViewHolder) holder).setMessage(mMessages.get(position));
        } else {
            ((ReceiveMessageViewHolder) holder).setMessage(mMessages.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    /**
     * Holder that holds receive message (messages that do not belong to the signed in user)
     *
     * @author Steven Tran
     */
    private class ReceiveMessageViewHolder extends RecyclerView.ViewHolder {
        private final View mView;
        private FragmentChatReceiveMessageBinding binding;

        public ReceiveMessageViewHolder(@NonNull View view) {
            super(view);
            mView = view;
            binding = FragmentChatReceiveMessageBinding.bind(view);
        }

        void setMessage(final ChatMessage message) {
                binding.textReceiveMessageName.setText(message.getSender());
                binding.textReceiveMessageTimestamp.setText(message.getTime());
                binding.textReceiveMessageContent.setText(message.getMessage());
            TypedValue typedValue = new TypedValue();
            Resources.Theme theme = mView.getContext().getTheme();
            theme.resolveAttribute(R.attr.colorSecondaryVariant, typedValue, true);
            binding.card.setCardBackgroundColor( typedValue.data);
        }
    }

    /**
     * Holder that holds send message (messages that do belong to the signed in user)
     *
     * @author Steven Tran
     */
    private class SendMessageViewHolder extends RecyclerView.ViewHolder {
        private final View mView;

        private FragmentChatSendMessageBinding binding;

        public SendMessageViewHolder(@NonNull View view) {
            super(view);
            mView = view;
            binding = FragmentChatSendMessageBinding.bind(view);
        }

        void setMessage(final ChatMessage message) {
            binding.textSendMessageContent.setText(message.getMessage());
            binding.textSendMessageDate.setText(message.getDate());
            binding.textSendMessageTime.setText(message.getTime());

            TypedValue typedValue = new TypedValue();
            Resources.Theme theme = mView.getContext().getTheme();
            theme.resolveAttribute(R.attr.colorPrimaryVariant, typedValue, true);
            binding.sendCard.setCardBackgroundColor( typedValue.data);
        }
    }
}
