package edu.uw.tcss450.stran373.ui.Weather;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.uw.tcss450.stran373.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class WeatherFragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_weather, container, false);
        if (view instanceof RecyclerView) {
            ((RecyclerView) view).setLayoutManager( new LinearLayoutManager(getActivity()));
            ((RecyclerView) view).setAdapter(
                    new WeatherRecyclerViewAdapter(WeatherGenerator.getWeatherList()));
        }
        return view;
    }
}