package edu.uw.tcss450.stran373.ui.Contact.ContactsInfo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import edu.uw.tcss450.stran373.R;
import edu.uw.tcss450.stran373.databinding.FragmentContactCardBinding;

/**
 * RecyclerViewAdapter is used to show the various cards
 * @author Andrew Bennett
 */
public class ContactCardRecyclerViewAdapter extends RecyclerView.Adapter<ContactCardRecyclerViewAdapter.ContactCardViewHolder>{

    /**
     * List of contacts to be displayed
     * @author Andrew Bennett
     */
    private final List<Contact> mContacts;

    /**
     * Flag to show whether the card is expanded or not
     * @author Andrew Bennett
     */
    private final Map<Contact, Boolean> mExpandedFlags;


    public ContactCardRecyclerViewAdapter(final List<Contact> theContactCards)
    {
        mContacts = theContactCards;
        this.mExpandedFlags = mContacts.stream()
                .collect(Collectors.toMap(Function.identity(), weather -> false));
    }

    /**
     * When the ViewHolder is created, inflate the contact card layout
     * @param parent
     * @param viewType
     * @return the ContactCardViewHolder for this contact
     */
    @NonNull
    @Override
    public ContactCardViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
        return new ContactCardViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_contact_card, parent, false));
    }

    /**
     * When the binding is created, the holder will set the contact to the
     * current position in the list
     * @author Andrew Bennett
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull ContactCardViewHolder holder, int position) {
        holder.setContact(mContacts.get(position));
    }

    /**
     * Returns the amount of total contacts in list
     * @author Andrew Bennett
     * @return the amount of contacts
     */
    @Override
    public int getItemCount() {
        return mContacts.size();
    }

    /**
     * Internal class used as a view holder for specific cards in the recycler
     * view
     * @author Andrew Bennett
     */
    public static class ContactCardViewHolder extends RecyclerView.ViewHolder {

        public final View mView;
        public FragmentContactCardBinding binding;
        private Contact mContact;


        public ContactCardViewHolder(@NonNull final View itemView) {
            super(itemView);
            mView = itemView;
            binding = FragmentContactCardBinding.bind(itemView);
        }

        /**
         * Sets the contact to the holder
         *
         * @param theContact a contact card
         */
        private void setContact(final Contact theContact)
        {
            mContact = theContact;

            /*// This will need to be build out in sprint 2
            binding.cardRoot.setOnClickListener(view -> {
                Navigation.findNavController(mView).navigate(ContactFragmentDirections
                        .actionNavigationContactsToContactFragment(mContact));
            });*/


            //THIS WILL ALSO NEED TO BE UPDATED IN SPRINT 2.
            //Currently the other attributes are set to invisible, preview is visible
            binding.textFirstName.setText(theContact.getFirstName());
            binding.textLastName.setText(theContact.getLastName());
            final String preview = binding.textFirstName.getText().toString()
                    + " " + binding.textLastName.getText().toString();
            binding.textPreview.setText(preview);
        }
    }
}