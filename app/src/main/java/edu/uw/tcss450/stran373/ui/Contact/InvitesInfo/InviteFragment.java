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
 * A simple {@link Fragment} subclass.
 */
public class InviteFragment extends Fragment {

    FragmentInviteBinding binding;

    public InviteFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentInviteBinding.inflate(inflater);
        final RecyclerView rv = binding.inviteRecycler;

        rv.setAdapter(new InviteCardRecyclerViewAdapter(InviteGenerator.getCARDS()));
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Setting up Listeners
        binding.button2.setOnClickListener(button ->
                Navigation.findNavController(getView()).navigate(
                        InviteFragmentDirections.actionInviteFragmentToRequestFragment()));

        binding.button.setOnClickListener(button ->
                Navigation.findNavController(getView()).navigate(
                        InviteFragmentDirections.actionInviteFragmentToNavigationContacts()));
    }
}