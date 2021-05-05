package edu.uw.tcss450.stran373.ui.Contact.RequestsInfo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import edu.uw.tcss450.stran373.databinding.FragmentRequestBinding;
import edu.uw.tcss450.stran373.ui.Contact.InvitesInfo.InviteFragmentDirections;

/**
 * A request fragment that will contain the recyclerview and the
 * cards for now. Will need to be switched to a list view
 * @author Andrew Bennett
 */
public class RequestFragment extends Fragment {

    FragmentRequestBinding binding;

    public RequestFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Attach the recyclerview and set the adapter
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return the root view of the requestfragment data binding.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentRequestBinding.inflate(inflater);
        final RecyclerView rv = binding.requestRecycler;

        rv.setAdapter(new RequestCardRecyclerViewAdapter(RequestGenerator.getCARDS()));
        return binding.getRoot();


    }

    /**
     * Set up the button listeners when the view is created
     * @author Andrew Bennett
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Setting up Listeners
        binding.inviteButton.setOnClickListener(button -> Navigation.findNavController(getView()).navigate(
                RequestFragmentDirections.actionRequestFragmentToInviteFragment()));

        binding.contactButton.setOnClickListener(button ->
                Navigation.findNavController(getView()).navigate(
                        RequestFragmentDirections.actionRequestFragmentToNavigationContacts()));
    }
}
