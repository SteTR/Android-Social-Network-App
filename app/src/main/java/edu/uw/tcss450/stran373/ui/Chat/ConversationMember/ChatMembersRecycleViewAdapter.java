package edu.uw.tcss450.stran373.ui.Chat.ConversationMember;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.uw.tcss450.stran373.R;
import edu.uw.tcss450.stran373.databinding.FragmentChatMemberBinding;

public class ChatMembersRecycleViewAdapter extends RecyclerView.Adapter<ChatMembersRecycleViewAdapter.SendMessageViewHolder> {

    private final List<ChatMember> mChatMembers;
    private final int mChatId;

    public ChatMembersRecycleViewAdapter(final List<ChatMember> chatMembers, final int chatid) {
        this.mChatMembers = chatMembers;
        this.mChatId = chatid;
    }

    @Override
    public SendMessageViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
        return new SendMessageViewHolder(parent.getContext(), LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_chat_member, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ChatMembersRecycleViewAdapter.SendMessageViewHolder holder, final int position) {
        holder.setChatMembercard(mChatMembers.get(position).getName(), mChatMembers.get(position).getMemberId(), mChatId);
    }

    @Override
    public int getItemCount() {
        return mChatMembers.size();
    }

    class SendMessageViewHolder extends RecyclerView.ViewHolder {
        private final View mView;
        private final Context mContext;
        private FragmentChatMemberBinding binding;

        public SendMessageViewHolder(@NonNull Context context, @NonNull View view) {
            super(view);
            mView = view;
            mContext = context;

            binding = FragmentChatMemberBinding.bind(view);
        }

        void setChatMembercard(final String name, final int memberid, final int chatid) {
            binding.textName.setText(name);
            binding.textId.setText(memberid + "");
            binding.cardConstraint.setOnClickListener((mView) -> new ChatMemberUpdateDialogFragment(name, memberid, chatid)
                    .show(((AppCompatActivity) mContext).getSupportFragmentManager(), ""));
        }
    }
}
