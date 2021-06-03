package edu.uw.tcss450.stran373.ui.Weather;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import edu.uw.tcss450.stran373.MainActivity;
import edu.uw.tcss450.stran373.R;
import edu.uw.tcss450.stran373.databinding.FragmentWeatherBinding;

/**
 * The fragment used for the application's weather feature.
 *
 * @author Jonathan Lee
 */
public class WeatherFragment extends Fragment {

    /**
     * Binding used to access resources for view.
     */
    private FragmentWeatherBinding mBinding;

    /**
     * The ViewModel used for displaying functionality.
     */
    private WeatherViewModel mModel;

    /**
     * The JWT used for connection to the weather service.
     */
    private String mJWT;

    /**
     * Instantiates the ViewModel needed for functionality.
     *
     * @param theSavedInstanceState is a Bundle object that keeps track of the saved instance state.
     */
    @Override
    public void onCreate(Bundle theSavedInstanceState) {
        super.onCreate(theSavedInstanceState);
        mModel = new ViewModelProvider(getActivity()).get(WeatherViewModel.class);
        MainActivity main = (MainActivity) getActivity();
        mJWT = main.getTheArgs().getJwt();
        mModel.setZip(98109);
        mModel.connect(mJWT);
    }

    /**
     * Initializes the contents of the ViewModel.
     *
     * @param theInflater is a LayoutInflater object.
     * @param theContainer is a ViewGroup object.
     * @param theSavedInstanceState is a Bundle object that keeps track of the saved instance state.
     * @return a View used to reference the current View.
     */
    @Override
    public View onCreateView(LayoutInflater theInflater, ViewGroup theContainer,
                             Bundle theSavedInstanceState) {

        // Inflate the layout for this fragment
        mBinding = FragmentWeatherBinding.inflate(theInflater);
        mModel.addWeatherCardListObserver(getViewLifecycleOwner(), cardList -> {
            if (!cardList.isEmpty()) {
                mBinding.weatherRecyler.setAdapter(
                        new WeatherRecyclerViewAdapter(cardList, mModel));
            }
        });

        return mBinding.getRoot();
    }

    /**
     * Enables the searching button for searching up the weather.
     *
     * @param theView is the current View of the application.
     * @param theSavedInstanceState is a Bundle object that keeps track of the saved instance state.
     */
    @Override
    public void onViewCreated(@NonNull View theView, @Nullable Bundle theSavedInstanceState) {
        super.onViewCreated(theView, theSavedInstanceState);
        //Setting up Listeners

        mBinding.expandableFabSearch.setOnClickListener(button ->
                Navigation.findNavController(getView()).navigate(
                WeatherFragmentDirections.actionNavigationWeatherToWeatherSearchFragment()));
        mBinding.expandableFabMap.setOnClickListener(button ->
                Navigation.findNavController(getView()).navigate(
                WeatherFragmentDirections.actionNavigationWeatherToMapFragment()));
    }



}