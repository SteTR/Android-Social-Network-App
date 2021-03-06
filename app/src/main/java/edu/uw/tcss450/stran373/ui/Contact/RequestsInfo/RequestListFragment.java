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

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import edu.uw.tcss450.stran373.R;
import edu.uw.tcss450.stran373.UserInfoViewModel;
import edu.uw.tcss450.stran373.databinding.FragmentRequestListBinding;
import edu.uw.tcss450.stran373.ui.Contact.InvitesInfo.InviteCard;

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
     * View model for the request list
     */
    private RequestListViewModel mModel;

    /**
     * View model for the contact list
     */
    List<RequestCard> currentSelectedItems = new ArrayList<>();

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

        //Listener for the requests
        mModel.addRequestListObserver(getViewLifecycleOwner(), requestList -> {
            if (!requestList.isEmpty()) {
                binding.requestRecycler.setAdapter(
                        new RequestCardRecyclerViewAdapter(requestList,

                                //This is a custom class that can be used in the future to build lists
                                //to accept reject. As of now, they act as button listeners
                                new RequestCardRecyclerViewAdapter.OnItemCheckListener() {
                                    @Override
                                    public void onAcceptCheck(RequestCard card) {
                                        mModel.connectPost(card.getMemberID(), "Accept", mUserViewModel.getJwt());

                                        //Remove the request from the view model and notify the
                                        //adapter that the data set has changed
                                        mModel.removeRequest(card);
                                        binding.requestRecycler.getAdapter().notifyDataSetChanged();
                                    }

                                    @Override
                                    public void onDeclineCheck(RequestCard card) {
                                        mModel.connectPost(card.getMemberID(), "Decline", mUserViewModel.getJwt());

                                        //Remove the request from the view model and notify the
                                        //adapter that the data set has changed
                                        mModel.removeRequest(card);
                                        binding.requestRecycler.getAdapter().notifyDataSetChanged();
                                    }
                                }
                        )
                        );
            }
        });

        //Button to navigate to contacts
        binding.contactButton.setOnClickListener(button ->
                Navigation.findNavController(getView()).navigate(
                        RequestListFragmentDirections.actionRequestListFragmentToNavigationContacts()));

        //Button to navigate to the invites
        binding.inviteButton.setOnClickListener(button ->
                Navigation.findNavController(getView()).navigate(
                        RequestListFragmentDirections.actionRequestListFragmentToInviteListFragment()));
    }
}