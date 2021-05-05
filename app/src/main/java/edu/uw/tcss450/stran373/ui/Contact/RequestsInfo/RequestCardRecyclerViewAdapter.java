package edu.uw.tcss450.stran373.ui.Contact.RequestsInfo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.uw.tcss450.stran373.R;
import edu.uw.tcss450.stran373.databinding.FragmentRequestCardBinding;

/**
 * RecyclerView adapter for the dummy data.
 */
public class RequestCardRecyclerViewAdapter extends RecyclerView.Adapter<RequestCardRecyclerViewAdapter.RequestCardViewHolder>{
    /**
     * List of request cards to be displayed
     * @author Andrew Bennett
     */
    private final List<Request> mRequests;

    public RequestCardRecyclerViewAdapter(final List<Request> theRequestCards)
    {
        mRequests = theRequestCards;
    }

    /**
     * Return the view holder for the request cards
     * @author Andrew Bennett
     * @param parent
     * @param viewType
     * @return RequestCardViewHolder
     */
    @NonNull
    @Override
    public RequestCardViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
        return new RequestCardViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_request_card, parent, false));
    }

    /**
     * When binding, set request to current Request card
     * @author Andrew Bennett
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull RequestCardViewHolder holder, int position) {
        holder.setRequest(mRequests.get(position));
    }

    /**
     * Current total of requests
     * @author Andrew Bennett
     * @return the total amount of requests pending
     */
    @Override
    public int getItemCount() {
        return mRequests.size();
    }

    /**Internal view holder for specific request cards
     * @author Andrew bennett
     */
    public static class RequestCardViewHolder extends RecyclerView.ViewHolder {

        public final View mView;
        public FragmentRequestCardBinding binding;
        private Request mRequest;

        public RequestCardViewHolder(@NonNull final View itemView) {
            super(itemView);
            mView = itemView;
            binding = FragmentRequestCardBinding.bind(itemView);
        }

        /**
         * Sets the request to the holder and sets the text to be shown in the preview
         * @author Andrew Bennett
         * @param theRequest a request
         */
        private void setRequest(final Request theRequest)
        {
            mRequest = theRequest;

            //THIS WILL NEED TO BE UPDATED IN SPRINT 2
            //Navigate to specific fragment
            /*binding.cardRoot.setOnClickListener(view -> {
                Navigation.findNavController(mView).navigate(ContactListFragmentDirections
                        .actionNavigationContactsToContactFragment(mRequest));
            });*/

            //THIS WILL ALSO NEED TO BE UPDATED IN SPRINT 2.
            //Currently the other attributes are set to invisible, preview is visible
            binding.textFirstName.setText(theRequest.getFirstName());
            binding.textLastName.setText(theRequest.getLastName());
            final String preview = binding.textFirstName.getText().toString()
                    + " " + binding.textLastName.getText().toString();
            binding.textPreview.setText(preview);
        }
    }
}