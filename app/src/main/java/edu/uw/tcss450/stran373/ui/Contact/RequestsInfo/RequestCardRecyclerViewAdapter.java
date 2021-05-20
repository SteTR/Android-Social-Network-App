package edu.uw.tcss450.stran373.ui.Contact.RequestsInfo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.uw.tcss450.stran373.R;
import edu.uw.tcss450.stran373.databinding.FragmentRequestCardBinding;

public class RequestCardRecyclerViewAdapter extends RecyclerView.Adapter<RequestCardRecyclerViewAdapter.RequestCardViewHolder>{
    /**
     * List of contacts to be displayed
     */
    private final List<RequestCard> mRequests;


    public RequestCardRecyclerViewAdapter(final List<RequestCard> theRequestCards)
    {
        this.mRequests= theRequestCards;
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

        /**
         * The specific contact card
         */
        private RequestCard mRequest;


        public RequestCardViewHolder(@NonNull final View itemView) {
            super(itemView);
            mView = itemView;
            binding = FragmentRequestCardBinding.bind(itemView);
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
