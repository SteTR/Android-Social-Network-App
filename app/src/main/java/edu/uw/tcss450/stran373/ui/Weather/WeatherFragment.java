package edu.uw.tcss450.stran373.ui.Weather;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.DecimalFormat;

import edu.uw.tcss450.stran373.R;
import edu.uw.tcss450.stran373.databinding.FragmentWeatherBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class WeatherFragment extends Fragment {
    /**Binding used to access resources for view.*/
    FragmentWeatherBinding binding;

    private static final WeatherCard[] mCards = new WeatherCard[5];

    private final String mWeatherURL = "http://api.openweathermap.org/data/2.5/weather";
    private final String mAppID = "e53301e27efa0b66d05045d91b2742d3";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void getWeatherDetails(View view) {
        String tempURL = "";
        String city = "";
        String state = "";
        tempURL = mWeatherURL + "?q=" + city + "," + "USA" + "%appid=" + mAppID;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentWeatherBinding.inflate(inflater);

        final RecyclerView rv = binding.weatherRecyler;
        rv.setAdapter(new WeatherRecyclerViewAdapter(WeatherGenerator.getWeatherList()));
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Setting up Listeners
        binding.buttonSearch.setOnClickListener(button ->
                Navigation.findNavController(getView()).navigate(
                WeatherFragmentDirections.actionNavigationWeatherToWeatherSearchFragment()));
    }
}