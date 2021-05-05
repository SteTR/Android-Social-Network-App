package edu.uw.tcss450.stran373.ui.Contact.InvitesInfo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import edu.uw.tcss450.stran373.databinding.FragmentInviteBinding;


/**
 * The invite fragment that will hold the recycler view
 * and show the cards
 * @author Andrew Bennett
 */
public class InviteFragment extends Fragment {

    /**
     * The data binding for this fragment
     */
    FragmentInviteBinding binding;

    public InviteFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * When view is created, assign binding, create recyclerview, and attach adapter
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return the root view in the data binding
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentInviteBinding.inflate(inflater);
        final RecyclerView rv = binding.inviteRecycler;

        rv.setAdapter(new InviteCardRecyclerViewAdapter(InviteGenerator.getCARDS()));
        return binding.getRoot();
    }

    /**
     * Set the listeners to move between Connections fragments
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Setting up Listeners
        binding.requestButton.setOnClickListener(button ->
                Navigation.findNavController(getView()).navigate(
                        InviteFragmentDirections.actionInviteFragmentToRequestFragment()));

        binding.contactButton.setOnClickListener(button ->
                Navigation.findNavController(getView()).navigate(
                        InviteFragmentDirections.actionInviteFragmentToNavigationContacts()));
    }
}