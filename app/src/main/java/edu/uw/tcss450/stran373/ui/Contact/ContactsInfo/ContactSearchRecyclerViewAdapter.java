package edu.uw.tcss450.stran373.ui.Contact.ContactsInfo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.uw.tcss450.stran373.R;
import edu.uw.tcss450.stran373.databinding.FragmentContactCardBinding;

/**
 * RecyclerViewAdapter is used to show the various cards and hold the view model.
 * The item checks allow us to build a list to launch chats.
 * @author Andrew Bennett
 */
public class ContactSearchRecyclerViewAdapter extends RecyclerView.Adapter<ContactSearchRecyclerViewAdapter.ContactCardViewHolder> {

    /**
     * List of contacts to be displayed
     */
    private final List<ContactCard> mContacts;

    public interface OnItemCheckListener {
        void onItemCheck(ContactCard card);
        void onItemUncheck(ContactCard card);
    }

    @NonNull
    public OnItemCheckListener onItemClick;


    public ContactSearchRecyclerViewAdapter(final List<ContactCard> theContactCards, @NonNull OnItemCheckListener onItemCheckListener)
    {
        this.mContacts = theContactCards;
        this.onItemClick = onItemCheckListener;
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
        final ContactCard contact = mContacts.get(position);
        holder.setContact(contact);

        holder.checkBox.setOnClickListener(view -> {
            if(holder.checkBox.isChecked()){
                onItemClick.onItemCheck(contact);
                contact.setSelected(true);
            }else{
                onItemClick.onItemUncheck(contact);
                contact.setSelected(false);
            }
        });
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

        /**
         * View for the card item
         */
        public final View mView;

        /**
         * Access to the ContactCard databinding
         */
        public FragmentContactCardBinding binding;

        /**
         * Whether the checkbox is checked or not
         */
        protected CheckBox checkBox;

        /**
         * The specific contact card
         */
        private ContactCard mContact;

        private Spinner spinner;


        public ContactCardViewHolder(@NonNull final View itemView) {
            super(itemView);
            mView = itemView;
            binding = FragmentContactCardBinding.bind(itemView);
            checkBox = (CheckBox) itemView.findViewById(R.id.checkBox);
            spinner = (Spinner) itemView.findViewById(R.id.search_spinner);
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
            binding.textEmail.setText(theContact.getEmail());
            binding.textMemberid.setText(theContact.getMemberID());
            binding.textUsername.setText(theContact.getUserName());
            binding.checkBox.setVisibility(View.VISIBLE);
        }

        public void setOnClickListener(CheckBox.OnClickListener onClickListener) {
            checkBox.setOnClickListener(onClickListener);
        }
    }
}