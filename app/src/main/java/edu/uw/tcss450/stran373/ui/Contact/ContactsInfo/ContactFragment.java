package edu.uw.tcss450.stran373.ui.Contact.ContactsInfo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import edu.uw.tcss450.stran373.R;
import edu.uw.tcss450.stran373.UserInfoViewModel;
import edu.uw.tcss450.stran373.databinding.FragmentContactBinding;
import edu.uw.tcss450.stran373.databinding.FragmentContactListBinding;
import edu.uw.tcss450.stran373.databinding.FragmentWeatherBinding;
import edu.uw.tcss450.stran373.ui.Weather.WeatherFragmentDirections;
import edu.uw.tcss450.stran373.ui.Weather.WeatherGenerator;
import edu.uw.tcss450.stran373.ui.Weather.WeatherRecyclerViewAdapter;

/**
 * The contact fragment that will contain the recyclerview and
 * show the cards
 * @author Andrew Bennett
 */
public class ContactFragment extends Fragment {
    FragmentContactBinding binding;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

    }
}