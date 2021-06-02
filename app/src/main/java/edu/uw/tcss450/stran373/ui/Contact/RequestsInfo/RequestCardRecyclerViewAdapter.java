package edu.uw.tcss450.stran373.ui.Contact.RequestsInfo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.checkbox.MaterialCheckBox;

import java.lang.ref.WeakReference;
import java.util.List;

import edu.uw.tcss450.stran373.R;
import edu.uw.tcss450.stran373.databinding.FragmentRequestCardBinding;
import edu.uw.tcss450.stran373.ui.Contact.ContactsInfo.ContactCard;
import edu.uw.tcss450.stran373.ui.Contact.ContactsInfo.ContactCardRecyclerViewAdapter;
import edu.uw.tcss450.stran373.ui.Contact.InvitesInfo.InviteCard;
import edu.uw.tcss450.stran373.ui.Contact.InvitesInfo.InviteCardRecyclerViewAdapter;

/**
 * Holds the different cards for the pending requests
 * @author Andrew Bennett
 */
public class RequestCardRecyclerViewAdapter extends RecyclerView.Adapter<RequestCardRecyclerViewAdapter.RequestCardViewHolder>{
    /**
     * List of contacts to be displayed
     */
    private final List<RequestCard> mRequests;

    interface OnItemCheckListener {
        void onItemCheck(RequestCard card);
    }

    @NonNull
    private RequestCardRecyclerViewAdapter.OnItemCheckListener onItemClick;


    public RequestCardRecyclerViewAdapter(final List<RequestCard> theRequestCards, @NonNull RequestCardRecyclerViewAdapter.OnItemCheckListener onItemCheckListener)
    {
        this.mRequests= theRequestCards;
        this.onItemClick = onItemCheckListener;
    }

    /**
     * When the ViewHolder is created, inflate the contact card layout
     * @param parent is the view group that this child belongs too
     * @param viewType type of viewgroup
     * @return the RequestCardViewHolder for this contact
     */
    @NonNull
    @Override
    public RequestCardRecyclerViewAdapter.RequestCardViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
        return new RequestCardRecyclerViewAdapter.RequestCardViewHolder(LayoutInflater.
                from(parent.getContext())
                .inflate(R.layout.fragment_request_card, parent, false));

    }

    /**
     * When the binding is created, the holder will set the request to the
     * current position in the list
     * @param holder assigns the current contact in this position
     * @param position the current position in the list
     */
    @Override
    public void onBindViewHolder(@NonNull RequestCardRecyclerViewAdapter.RequestCardViewHolder holder, int position) {
        final RequestCard request = mRequests.get(position);
        holder.setRequest(request);

        holder.buttonAccept.setOnClickListener(view -> {
            onItemClick.onItemCheck(request);
            request.setDeclined(false);
            request.setAccepted(true);
        });

        holder.buttonDecline.setOnClickListener(view -> {
            onItemClick.onItemCheck(request);
            request.setAccepted(false);
            request.setDeclined(true);
        });
    }

    /**
     * Returns the amount of total contacts in list
     * @return the amount of contacts
     */
    @Override
    public int getItemCount() {
        return mRequests.size();
    }

    /**
     * Internal class used as a view holder for specific cards in the recycler
     * view
     */
    public static class RequestCardViewHolder extends RecyclerView.ViewHolder {

        /**
         * View for the card item
         */
        public final View mView;

        /**
         * Access to the RequestCard databinding
         */
        public FragmentRequestCardBinding binding;

        protected Button buttonAccept;

        protected Button buttonDecline;

        /**
         * The specific contact card
         */
        private RequestCard mRequest;


        public RequestCardViewHolder(@NonNull final View itemView) {
            super(itemView);
            mView = itemView;
            binding = FragmentRequestCardBinding.bind(itemView);
            buttonAccept = itemView.findViewById(R.id.acceptBox);
            buttonDecline = itemView.findViewById(R.id.declineBox);
        }


        /**
         * Sets the request to the holder
         *
         * @param theRequest is the request to be added
         */
        private void setRequest(final RequestCard theRequest)
        {
            mRequest = theRequest;
            binding.textFirstName.setText(theRequest.getFirstName());
            binding.textLastName.setText(theRequest.getLastName());
            String fullName = binding.textFirstName.getText().toString()
                    +" " + binding.textLastName.getText().toString();
            binding.textFirstLast.setText(fullName);
        }

    }
}
