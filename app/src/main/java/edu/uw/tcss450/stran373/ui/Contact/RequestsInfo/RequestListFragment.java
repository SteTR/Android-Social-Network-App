package edu.uw.tcss450.stran373.ui.Contact.RequestsInfo;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.uw.tcss450.stran373.R;
import edu.uw.tcss450.stran373.UserInfoViewModel;
import edu.uw.tcss450.stran373.databinding.FragmentRequestListBinding;

/**
 * This is the class that contains all of the buttons for navigation,
 * the recyclerview for the cards, and adds the observer/viewmodel provider
 * @author Andrew Bennett
 */
public class RequestListFragment extends Fragment {

    /**
     * View model to get JWT
     */
    UserInfoViewModel mUserViewModel;

    /**
     * View model for the contact list
     */
    private RequestListViewModel mModel;

    /**
     * Inflate the layout
     * @param inflater is the inflater for the layout
     * @param container is the container (MainActivity)
     * @param savedInstanceState is the instance state of the Bundle
     * @return the inflated view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_request_list, container, false);
    }

    /**
     * When the fragment is created, call the contact endpoint to get current
     * contacts
     * @param savedInstanceState
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mModel = new ViewModelProvider(getActivity()).get(RequestListViewModel.class);
        mUserViewModel = new ViewModelProvider(getActivity()).get(UserInfoViewModel.class);
        String jwt = mUserViewModel.getJwt();
        mModel.connectGet(jwt);
    }

    /**
     * Set the adapter to the requestcard recyclerview
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FragmentRequestListBinding binding = FragmentRequestListBinding.bind(getView());


        //Next sprint we will need to add listeners here so that they can accept/reject lists
        mModel.addRequestListObserver(getViewLifecycleOwner(), requestList -> {
            if (!requestList.isEmpty()) {
                binding.requestRecycler.setAdapter(
                        new RequestCardRecyclerViewAdapter(requestList, new RequestCardRecyclerViewAdapter.ClickListener() {

                            @Override
                            public void onAcceptClicked(RequestCard card) {
                                mModel.connectPost(card.getMemberID(), "Accept", mUserViewModel.getJwt());
                            }

                            @Override
                            public void onDeclineClicked(RequestCard card) {
                                mModel.connectPost(card.getMemberID(), "Decline", mUserViewModel.getJwt());
                            }
                        }));
            }
        });

        binding.contactButton.setOnClickListener(button ->
                Navigation.findNavController(getView()).navigate(
                        RequestListFragmentDirections.actionRequestListFragmentToNavigationContacts()));

        binding.inviteButton.setOnClickListener(button ->
                Navigation.findNavController(getView()).navigate(
                        RequestListFragmentDirections.actionRequestListFragmentToInviteListFragment()));
    }
}