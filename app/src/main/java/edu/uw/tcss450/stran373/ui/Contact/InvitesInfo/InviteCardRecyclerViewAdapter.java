package edu.uw.tcss450.stran373.ui.Contact.InvitesInfo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.uw.tcss450.stran373.R;
import edu.uw.tcss450.stran373.databinding.FragmentInviteCardBinding;

/**
 * RecyclerViewAdapter is used to show the various cards
 * @author Andrew Bennett
 */
public class InviteCardRecyclerViewAdapter extends RecyclerView.Adapter<InviteCardRecyclerViewAdapter.InviteCardViewHolder> {

    /**
     * List of contacts to be displayed
     */
    private final List<InviteCard> mInvites;

    interface OnItemCheckListener {
        void onItemCheck(InviteCard card);
        void onItemUncheck(InviteCard card);
    }

    @NonNull
    private InviteCardRecyclerViewAdapter.OnItemCheckListener onItemClick;

    public InviteCardRecyclerViewAdapter(final List<InviteCard> theInviteCards, @NonNull InviteCardRecyclerViewAdapter.OnItemCheckListener onItemCheckListener)
    {
        this.mInvites = theInviteCards;
        this.onItemClick = onItemCheckListener;
    }

    /**
     * When the ViewHolder is created, inflate the invite card layout
     * @param parent is the view group that this child belongs too
     * @param viewType type of viewgroup
     * @return the ContactCardViewHolder for this contact
     */
    @NonNull
    @Override
    public InviteCardViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
        return new InviteCardViewHolder(LayoutInflater.
                from(parent.getContext())
                .inflate(R.layout.fragment_invite_card, parent, false));
    }

    /**
     * When the binding is created, the holder will set the contact to the
     * current position in the list
     * @param holder assigns the current contact in this position
     * @param position the current position in the list
     */
    @Override
    public void onBindViewHolder(@NonNull InviteCardViewHolder holder, int position) {
        final InviteCard invite = mInvites.get(position);
        holder.setInvite(invite);
        holder.buttonInvite.setOnClickListener(view -> {
            onItemClick.onItemCheck(invite);
            invite.setSelected(true);
        });
    }


    /**
     * Returns the amount of total invites in list
     * @return the amount of invites
     */
    @Override
    public int getItemCount() {
        return mInvites.size();
    }

    /**
     * Internal class used as a view holder for specific cards in the recycler
     * view
     */
    public static class InviteCardViewHolder extends RecyclerView.ViewHolder {

        /**
         * View for the card item
         */
        public final View mView;

        /**
         * Access to the ContactCard databinding
         */
        public FragmentInviteCardBinding binding;

        /**
         * Whether the checkbox is checked or not
         */
        protected Button buttonInvite;

        /**
         * The specific contact card
         */
        private InviteCard mContact;


        public InviteCardViewHolder(@NonNull final View itemView) {
            super(itemView);
            mView = itemView;
            binding = FragmentInviteCardBinding.bind(itemView);
            buttonInvite = (Button) itemView.findViewById(R.id.inviteButton);
        }


        /**
         * Sets the invite to the holder
         *
         * @param theInvite a contact card
         */
        private void setInvite(final InviteCard theInvite)
        {
            mContact = theInvite;
            binding.textFirstName.setText(theInvite.getFirstName());
            binding.textLastName.setText(theInvite.getLastName());
            String fullName = binding.textFirstName.getText().toString()
                    +" " + binding.textLastName.getText().toString();
            binding.textFirstLast.setText(fullName);
            binding.textMemberid.setText(theInvite.getMemberID());
        }

        /**
         * on click listener that was used for inviting users.
         * @param onClickListener checks for when the invite button is pressed.
         */
        public void setOnClickListener(Button.OnClickListener onClickListener) {
            buttonInvite.setOnClickListener(onClickListener);
        }
    }
}