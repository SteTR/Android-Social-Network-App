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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.text.DecimalFormat;

import edu.uw.tcss450.stran373.MainActivity;
import edu.uw.tcss450.stran373.R;
import edu.uw.tcss450.stran373.databinding.FragmentWeatherBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class WeatherFragment extends Fragment {

    /**Binding used to access resources for view.*/
    FragmentWeatherBinding mBinding;

    /**
     *
     */
    private WeatherViewModel mModel;

    /**
     *
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mModel = new ViewModelProvider(getActivity()).get(WeatherViewModel.class);
        mModel.connect();
    }

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
        mBinding = FragmentWeatherBinding.inflate(inflater);
        mModel.addWeatherCardListObserver(getViewLifecycleOwner(), cardList -> {
            if (!cardList.isEmpty()) {
                mBinding.weatherRecyler.setAdapter(
                        new WeatherRecyclerViewAdapter(cardList, mModel));
            }
        });
        return mBinding.getRoot();
    }

    /**
     *
     *
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Setting up Listeners
        mBinding.buttonSearch.setOnClickListener(button ->
                Navigation.findNavController(getView()).navigate(
                WeatherFragmentDirections.actionNavigationWeatherToWeatherSearchFragment()));
    }
}