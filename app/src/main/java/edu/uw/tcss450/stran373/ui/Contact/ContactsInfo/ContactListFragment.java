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
import android.widget.CheckBox;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import edu.uw.tcss450.stran373.MainActivityArgs;
import edu.uw.tcss450.stran373.R;
import edu.uw.tcss450.stran373.UserInfoViewModel;
import edu.uw.tcss450.stran373.databinding.FragmentContactCardBinding;
import edu.uw.tcss450.stran373.databinding.FragmentContactListBinding;
import edu.uw.tcss450.stran373.ui.Weather.WeatherFragmentDirections;

/**
 * This class will be used in the future when we retrieve data from endpoints
 * It will be used to hold the view model
 * @author Andrew Bennett
 */
public class ContactListFragment extends Fragment {

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
     * Inflate the layout
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return the inflated view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contact_list, container, false);
    }

    /**
     * When the fragment is created, call the contact endpoint to get current
     * contacts
     * @param savedInstanceState
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       mModel = new ViewModelProvider(getActivity()).get(ContactListViewModel.class);
       mModel.connectGet();
    }

    /**
     * Set the adapter with the checks for the checkmarks
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FragmentContactListBinding binding = FragmentContactListBinding.bind(getView());

        mModel.addContactListObserver(getViewLifecycleOwner(), contactList -> {
            if (!contactList.isEmpty()) {
                binding.listRoot.setAdapter(
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
        /**
         * When floating action button pressed, create chat
         */
        binding.buttonAdd.setOnClickListener(button ->
                mModel.handleChatCreation(currentSelectedItems));
    }
}
