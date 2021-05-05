package edu.uw.tcss450.stran373.ui.Contact.ContactsInfo;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.uw.tcss450.stran373.R;
import edu.uw.tcss450.stran373.databinding.FragmentContactListBinding;

/**
 * This class will be used in the future when we retrieve data from endpoints
 * It will be used to hold the view model
 * @author Andrew Bennett
 */
public class ContactListFragment extends Fragment {

    private ContactListViewModel mModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contact_list, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       mModel = new ViewModelProvider(getActivity()).get(ContactListViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FragmentContactListBinding binding = FragmentContactListBinding.bind(getView());

        mModel.addContactListObserver(getViewLifecycleOwner(), contactList -> {
            if (!contactList.isEmpty()) {
                binding.recyclerView.setAdapter(
                        new ContactCardRecyclerViewAdapter(contactList));
            }
        });
    }
}