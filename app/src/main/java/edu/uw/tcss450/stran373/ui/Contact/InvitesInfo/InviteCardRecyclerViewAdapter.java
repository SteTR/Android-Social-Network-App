package edu.uw.tcss450.stran373.ui.Contact.InvitesInfo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.uw.tcss450.stran373.R;
import edu.uw.tcss450.stran373.databinding.FragmentContactCardBinding;
import edu.uw.tcss450.stran373.databinding.FragmentInviteCardBinding;


public class InviteCardRecyclerViewAdapter extends RecyclerView.Adapter<InviteCardRecyclerViewAdapter.InviteCardViewHolder>{

    // List of chat cards to be displayed
    private final List<Invite> mInvites;

    public InviteCardRecyclerViewAdapter(final List<Invite> theInviteCards)
    {
        mInvites = theInviteCards;
    }

    @NonNull
    @Override
    public InviteCardViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
        return new InviteCardViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_invite_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull InviteCardViewHolder holder, int position) {
        holder.setInvite(mInvites.get(position));
    }

    @Override
    public int getItemCount() {
        return mInvites.size();
    }

    public static class InviteCardViewHolder extends RecyclerView.ViewHolder {

        public final View mView;
        public FragmentInviteCardBinding binding;
        private Invite mInvite;

        public InviteCardViewHolder(@NonNull final View itemView) {
            super(itemView);
            mView = itemView;
            binding = FragmentInviteCardBinding.bind(itemView);
        }

        /**
         * Sets the request to the holder
         *
         * @param theInvite a request
         */
        private void setInvite(final Invite theInvite)
        {
            mInvite = theInvite;

            //THIS WILL NEED TO BE UPDATED IN SPRINT 2.
            //Currently the other attributes are set to invisible, preview is visible
            binding.textFirstName.setText(theInvite.getFirstName());
            binding.textLastName.setText(theInvite.getLastName());
            final String preview = binding.textFirstName.getText().toString()
                    + " " + binding.textLastName.getText().toString();
            binding.textPreview.setText(preview);
        }
    }
}