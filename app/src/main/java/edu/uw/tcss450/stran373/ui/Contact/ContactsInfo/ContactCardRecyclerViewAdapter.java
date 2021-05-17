package edu.uw.tcss450.stran373.ui.Contact.ContactsInfo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import edu.uw.tcss450.stran373.R;
import edu.uw.tcss450.stran373.databinding.FragmentContactCardBinding;
import edu.uw.tcss450.stran373.ui.Weather.HourlyGenerator;
import edu.uw.tcss450.stran373.ui.Weather.HourlyRecyclerViewAdapter;

/**
 * RecyclerViewAdapter is used to show the various cards
 * @author Andrew Bennett
 */
public class ContactCardRecyclerViewAdapter extends RecyclerView.Adapter<ContactCardRecyclerViewAdapter.ContactCardViewHolder>{

    /**
     * List of contacts to be displayed
     */
    private final List<ContactCard> mContacts;

    protected static boolean mCheckBox;

    public ContactCardRecyclerViewAdapter(final List<ContactCard> theContactCards)
    {
        this.mContacts = theContactCards;
    }

    /**
     * When the ViewHolder is created, inflate the contact card layout
     * @param parent is the view group that this child belongs too
     * @param viewType type of viewgroup
     * @return the ContactCardViewHolder for this contact
     */
    @NonNull
    @Override
    public ContactCardViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
        return new ContactCardViewHolder(LayoutInflater.
                from(parent.getContext())
                .inflate(R.layout.fragment_contact_card, parent, false));
    }

    /**
     * When the binding is created, the holder will set the contact to the
     * current position in the list
     * @param holder assigns the current contact in this position
     * @param position the current position in the list
     */
    @Override
    public void onBindViewHolder(@NonNull ContactCardViewHolder holder, int position) {
        holder.setContact(mContacts.get(position));

    }

    /**
     * Returns the amount of total contacts in list
     * @return the amount of contacts
     */
    @Override
    public int getItemCount() {
        return mContacts.size();
    }

    /**
     * Internal class used as a view holder for specific cards in the recycler
     * view
     */
    public static class ContactCardViewHolder extends RecyclerView.ViewHolder {

        public final View mView;
        public FragmentContactCardBinding binding;
        private ContactCard mContact;


        public ContactCardViewHolder(@NonNull final View itemView) {
            super(itemView);
            mView = itemView;
            binding = FragmentContactCardBinding.bind(itemView);
            mView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    mCheckBox = true;
                    return true;// returning true instead of false, works for me
                }
            });
        }

        /**
         * Sets the contact to the holder
         *
         * @param theContact a contact card
         */
        private void setContact(final ContactCard theContact)
        {
            mContact = theContact;
            binding.textFirstName.setText(theContact.getFirstName());
            binding.textLastName.setText(theContact.getLastName());
            String fullName = binding.textFirstName.getText().toString()
                    +" " + binding.textLastName.getText().toString();
            binding.textFirstLast.setText(fullName);
            if(mCheckBox) {
                binding.checkBox.setVisibility(View.INVISIBLE);
            } else {
                binding.checkBox.setVisibility(View.VISIBLE);
            }
        }
    }
}