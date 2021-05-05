package edu.uw.tcss450.stran373.ui.Contact.ContactsInfo;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.uw.tcss450.stran373.databinding.FragmentContactBinding;

/**
 * The contact fragment that will contain the recyclerview and
 * show the cards
 * @author Andrew Bennett
 */
public class ContactFragment extends Fragment {

    /**
     * The databinding for this class
     * @author Andrew Bennett
     */
    FragmentContactBinding binding;

    public ContactFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * When the view is created, attach the recycler view and set the adapter
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return the root view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentContactBinding.inflate(inflater);
        final RecyclerView rv = binding.contactRecycler;

        rv.setAdapter(new ContactCardRecyclerViewAdapter(ContactGenerator.getCARDS()));
        return binding.getRoot();
    }

    /**
     * Setup the button listeners to switch between connection options
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Setting up Listeners
        binding.requestButton.setOnClickListener(button ->
                Navigation.findNavController(getView()).navigate(
                        ContactFragmentDirections.actionNavigationContactsToRequestFragment()));

        binding.inviteButton.setOnClickListener(button ->
                Navigation.findNavController(getView()).navigate(
                        ContactFragmentDirections.actionNavigationContactsToInviteFragment()));
    }

}