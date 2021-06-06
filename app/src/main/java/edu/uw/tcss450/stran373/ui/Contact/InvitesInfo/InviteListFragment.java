package edu.uw.tcss450.stran373.ui.Contact.InvitesInfo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import edu.uw.tcss450.stran373.R;
import edu.uw.tcss450.stran373.UserInfoViewModel;

import edu.uw.tcss450.stran373.databinding.FragmentInviteListBinding;

/**
 * This fragment holds the recylerview, the view model, and creates
 * the card through a list observer
 * @author Andrew Bennett
 */
public class InviteListFragment extends Fragment {

    UserInfoViewModel mUserViewModel;

    /**
     * View model for the contact list
     */
    private InviteListViewModel mModel;

    /**
     * Array of contacts that are currently selected
     */
    private List<InviteCard> currentSelectedItems = new ArrayList<>();

    /**
     * Inflate the layout
     * @param inflater the layout inflater that will expand the list fragment
     * @param container the viewgroup that holds the fragment
     * @param savedInstanceState includes all of the stateful data such as args
     * @return the inflated view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_invite_list, container, false);
    }

    /**
     * When the fragment is created, call the contact endpoint to get current
     * contacts
     * @param savedInstanceState includes all of the stateful data such as args
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserViewModel = new ViewModelProvider(getActivity()).get(UserInfoViewModel.class);
        mModel = new ViewModelProvider(getActivity()).get(InviteListViewModel.class);
        String jwt = mUserViewModel.getJwt();
        mModel.connectGet(jwt);
    }

    /**
     * Set the adapter with the checks for the checkmarks. Also contains the listeners
     * for the connections nav
     * @param view The view that is created
     * @param savedInstanceState includes all of the stateful data such as args
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FragmentInviteListBinding binding = FragmentInviteListBinding.bind(getView());
        //Add an observer and create list. The code that is in there now
        //is for Sprint 3, it will allow us to build a list of users
        //to invite all at once
        mModel.addInviteListObserver(getViewLifecycleOwner(), inviteList -> {
            if (!inviteList.isEmpty()) {
                binding.inviteRecycler.setAdapter(
                        new InviteCardRecyclerViewAdapter(inviteList, mModel,
                                new InviteCardRecyclerViewAdapter.OnItemCheckListener() {
                                    //The listeners to build the list in Sprint 3
                                    @Override
                                    public void onItemCheck(InviteCard card) {
                                        currentSelectedItems.add(card);
                                        Snackbar.make(getView(), R.string.invite_sent, Snackbar.LENGTH_LONG).show();
                                        mModel.connectPost(card.getMemberID(), mUserViewModel.getJwt());
                                    }
                                    @Override
                                    public void onItemUncheck(InviteCard card) {
                                        currentSelectedItems.remove(card);
                                    }
                                }
                                ));
            }
        });

        //Get the JWT from the activity args
        String jwt = mUserViewModel.getJwt();

        binding.contactButton.setOnClickListener(button ->
                Navigation.findNavController(getView()).navigate(
                        InviteListFragmentDirections.actionInviteListFragmentToNavigationContacts()));

        binding.requestButton.setOnClickListener(button ->
                Navigation.findNavController(getView()).navigate(
                        InviteListFragmentDirections.actionInviteListFragmentToRequestListFragment()));

        //For every item that was added to the list, call the invite endpoint
        //with a POST request
        /*
        for(int i = 0; i < currentSelectedItems.size(); i++) {
            mModel.connectPost(currentSelectedItems.get(i).getMemberID(), jwt);
        }*/

    }
}
