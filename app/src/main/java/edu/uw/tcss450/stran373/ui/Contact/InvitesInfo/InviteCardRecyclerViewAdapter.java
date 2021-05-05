package edu.uw.tcss450.stran373.ui.Contact.InvitesInfo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.uw.tcss450.stran373.R;
import edu.uw.tcss450.stran373.databinding.FragmentInviteCardBinding;

/**
 * Recyvlerview adapter to be used in the invite fragment
 * @author Andrew Bennett
 */
public class InviteCardRecyclerViewAdapter extends RecyclerView.Adapter<
        InviteCardRecyclerViewAdapter.InviteCardViewHolder>{

    // List of invite cards to be used
    private final List<Invite> mInvites;

    /**
     * Constructs the adapter with the list of invite cards
     * @author Andrew Bennett
     * @param theInviteCards
     */
    public InviteCardRecyclerViewAdapter(final List<Invite> theInviteCards)
    {
        mInvites = theInviteCards;
    }

    /**
     * View holder that containts the card layout to be used in
     * the recyclerview.
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public InviteCardViewHolder onCreateViewHolder(@NonNull final ViewGroup parent,
                                                   final int viewType) {
        return new InviteCardViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_invite_card, parent, false));
    }

    /**
     * Set the invite to the view holder, uses the current position in the list
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull InviteCardViewHolder holder, int position) {
        holder.setInvite(mInvites.get(position));
    }

    /**
     * Returns the amount of invites
     * @return the total invites
     */
    @Override
    public int getItemCount() {
        return mInvites.size();
    }

    /**
     * Internal class to use as view holder for the cards
     * @author Andrew Bennett
     */
    public static class InviteCardViewHolder extends RecyclerView.ViewHolder {

        public final View mView;
        public FragmentInviteCardBinding binding;
        private Invite mInvite;

        /**
         * Constructs the view holder from the current view
         * and the invite card layout
         * @param itemView
         */
        public InviteCardViewHolder(@NonNull final View itemView) {
            super(itemView);
            mView = itemView;
            binding = FragmentInviteCardBinding.bind(itemView);
        }

        /**
         * Sets the invite to the holder
         *
         * @param theInvite a invite object
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
