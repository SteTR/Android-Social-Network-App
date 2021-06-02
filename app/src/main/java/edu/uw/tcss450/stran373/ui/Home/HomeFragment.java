package edu.uw.tcss450.stran373.ui.Home;

import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.Calendar;
import java.util.TimeZone;

import edu.uw.tcss450.stran373.MainActivity;
import edu.uw.tcss450.stran373.R;
import edu.uw.tcss450.stran373.databinding.FragmentHomeBinding;
import edu.uw.tcss450.stran373.ui.Weather.WeatherCard;

/**
 * A simple {@link Fragment} subclass.
 * A blank fragment used for UI at the moment.
 */
public class HomeFragment extends Fragment {

    /**
     *
     */
    private FragmentHomeBinding mBinding;

    /**
     * The ViewModel used for displaying functionality.
     */
    private HomeViewModel mModel;

    /**
     * The JWT used for connection to the weather service.
     */
    private String mJWT;

    /**
     * Instantiates the ViewModel needed for functionality.
     *
     * @param savedInstanceState is a Bundle object that keeps track of the saved instance state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentHomeBinding.inflate(inflater);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mModel = new ViewModelProvider(getActivity()).get(HomeViewModel.class);
        MainActivity main = (MainActivity) getActivity();
        LocationViewModel model = new ViewModelProvider(getActivity()).get(LocationViewModel.class);
        mJWT = main.getTheArgs().getJwt();
        model.addLocationObserver(getViewLifecycleOwner(), location -> {
            mModel.connect(mJWT, location);
        });

//        Log.d("Call 2: ", "Here");
//        Log.d("Latitude: ", "" + model.getLocation().getLatitude());
//        mModel.connect(mJWT);
        mModel.addResponseObserver(getViewLifecycleOwner(), weather -> {
            mBinding.textCurrentLocation.setText(weather.getLocation());
            mBinding.textCurrentTemp.setText(weather.getTemp());
            setWeatherIcon(weather.getCondition());
        });
        mBinding.textHomeName.setText(main.getTheArgs().getEmail() + '!');
    }

    /**
     * Helper method used to update the icon
     * for the current weather condition.
     * @param theCond is the current weather condition
     */
    private void setWeatherIcon(final int theCond) {
        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        boolean night = (hour > 16 && hour < 24) || (hour > -1 && hour < 4);
        if (theCond <= 232) {
            mBinding.imageCurrentCond.setImageResource(R.drawable.ic_thunder);
        } else if (theCond <= 531 && theCond >= 300) {
            mBinding.imageCurrentCond.setImageResource(R.drawable.ic_rainy_24dp);
        } else if (theCond<= 622 && theCond >= 600) {
            mBinding.imageCurrentCond.setImageResource(R.drawable.ic_snowy_24dp);
        } else if ((theCond <= 781 && theCond >= 701) || theCond> 800) {
            if (night) {
                mBinding.imageCurrentCond.setImageResource(R.drawable.ic_cloudy_night_24dp);
            } else {
                mBinding.imageCurrentCond.setImageResource(R.drawable.ic_cloudy);
            }
        } else if (theCond== 800) {
            if (night) {
                mBinding.imageCurrentCond.setImageResource(R.drawable.ic_night_24dp);
            } else {
                mBinding.imageCurrentCond.setImageResource(R.drawable.ic_day);
            }
        }
    }
}