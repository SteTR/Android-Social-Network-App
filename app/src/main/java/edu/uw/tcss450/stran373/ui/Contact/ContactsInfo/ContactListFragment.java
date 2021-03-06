package edu.uw.tcss450.stran373.ui.Contact.ContactsInfo;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


import edu.uw.tcss450.stran373.R;
import edu.uw.tcss450.stran373.UserInfoViewModel;
import edu.uw.tcss450.stran373.databinding.FragmentContactListBinding;



/**
 * This class is used to hold all of the contact cards, create and set
 * the adapter for the recycler view, and add the observer
 * @author Andrew Bennett
 */
public class ContactListFragment extends Fragment {

    /**
     * User info view model to get the JWT
     */
    UserInfoViewModel mUserViewModel;

    /**
     * Used to keep track of the user's response.
     */
    private MutableLiveData<JSONObject> mResponse;

    /**
     * View model for the contact list
     */
    private ContactListViewModel mModel;

    /**
     * Array of contacts that are currently selected
     */
    private List<ContactCard> currentSelectedItems = new ArrayList<>();

    /**
     * When the fragment is created, call the contact endpoint to get current
     * contacts
     *
     * @param savedInstanceState is the stateful data for use
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserViewModel = new ViewModelProvider(getActivity()).get(UserInfoViewModel.class);
        mModel = new ViewModelProvider(getActivity()).get(ContactListViewModel.class);
        String jwt = mUserViewModel.getJwt();
        mModel.connectGet(jwt);
    }

    /**
     * Inflate the layout
     * @param inflater is the layout inflater
     * @param container is the container for the fragment
     * @param savedInstanceState saves the stateful data for use
     * @return the inflated view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_contact_list, container, false);
    }


    /**
     * Set the adapter with the checks for the checkmarks. Then create chats
     * from all checked cards
     * @param view is the view that is created
     * @param savedInstanceState is the data that may be used
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FragmentContactListBinding binding = FragmentContactListBinding.bind(getView());

        mModel.addContactListObserver(getViewLifecycleOwner(), contactList -> {
            if (!contactList.isEmpty()) {
                binding.contactRecycler.setAdapter(
                        new ContactCardRecyclerViewAdapter(contactList,
                                new ContactCardRecyclerViewAdapter.OnItemCheckListener() {
                                        @Override
                                        public void onItemCheck(ContactCard card) {
                                            currentSelectedItems.add(card);
                                        }
                                        @Override
                                        public void onItemUncheck(ContactCard card) {
                                            currentSelectedItems.remove(card);
                                        }
                                    }
                                    ));
                                }
            });


        String jwt = mUserViewModel.getJwt();
        //When floating action button pressed, create chat
        binding.expandableFabChat.setOnClickListener(button -> {
            mModel.handleChatCreation(currentSelectedItems, jwt);
            Snackbar.make(getView(), R.string.text_chat_created, Snackbar.LENGTH_LONG).show();
        });

        binding.expandableFabSearch.setOnClickListener(button -> {
            Navigation.findNavController(getView()).navigate(
                    ContactListFragmentDirections.actionNavigationContactsToContactSearch());
        });

        binding.expandableFabDelete.setOnClickListener(button -> {
            for(int i = 0; i < currentSelectedItems.size(); i++) {
                mModel.connectPost(Integer.parseInt(currentSelectedItems.get(i).getMemberID()), mUserViewModel.getJwt());
                mModel.removeInvite(currentSelectedItems.get(i));
            }
            binding.contactRecycler.getAdapter().notifyDataSetChanged();
        });

        //Navigate to other connection fragments
        binding.inviteButton.setOnClickListener(button ->
                Navigation.findNavController(getView()).navigate(
                        ContactListFragmentDirections.actionNavigationContactsToInviteListFragment()));

        binding.requestButton.setOnClickListener(button ->
                Navigation.findNavController(getView()).navigate(
                        ContactListFragmentDirections.actionNavigationContactsToRequestListFragment()));
    }
}
