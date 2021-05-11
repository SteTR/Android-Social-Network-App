package edu.uw.tcss450.stran373.ui.Weather;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.uw.tcss450.stran373.R;

/**
 * A simple {@link Fragment} subclass.
 * A Blank fragment with nothing in it.
 */
public class WeatherSearchFragment extends Fragment {

    /**
     *
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_weather_search, container, false);
    }
}