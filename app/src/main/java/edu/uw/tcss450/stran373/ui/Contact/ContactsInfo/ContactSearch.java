package edu.uw.tcss450.stran373.ui.Contact.ContactsInfo;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import edu.uw.tcss450.stran373.R;
import edu.uw.tcss450.stran373.UserInfoViewModel;
import edu.uw.tcss450.stran373.databinding.FragmentContactSearchBinding;
import edu.uw.tcss450.stran373.databinding.FragmentContactSearchListBinding;
import edu.uw.tcss450.stran373.ui.Contact.RequestsInfo.RequestListViewModel;

/**
 * @author Andrew Bennett
 * Fragment for the Contact search functions
 */
public class ContactSearch extends Fragment {

    /**
     *
     */
    FragmentContactSearchBinding binding;

    /**
     * View model for contact search
     */
    ContactSearchViewModel mModel;

    /**
     * User Info View model
     */
    UserInfoViewModel mUserInfoViewModel;

    /**
     * Array of contacts that are currently selected
     */
    private List<ContactCard> currentSelectedItems = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        mModel = new ViewModelProvider(getActivity()).get(ContactSearchViewModel.class);
        mUserInfoViewModel = new ViewModelProvider(getActivity()).get(UserInfoViewModel.class);
        View v = inflater.inflate(R.layout.fragment_contact_search, container, false);
        binding = FragmentContactSearchBinding.bind(v);
        Spinner spinner = (Spinner) v.findViewById(R.id.search_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.search_array, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        spinner.setSelection(1);
        return v;
    }
}