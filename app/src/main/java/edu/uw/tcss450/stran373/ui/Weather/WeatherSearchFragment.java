package edu.uw.tcss450.stran373.ui.Weather;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.MapView;

import org.json.JSONException;
import org.json.JSONObject;

import edu.uw.tcss450.stran373.MainActivity;
import edu.uw.tcss450.stran373.R;
import edu.uw.tcss450.stran373.databinding.FragmentSignInBinding;
import edu.uw.tcss450.stran373.databinding.FragmentWeatherSearchBinding;
import edu.uw.tcss450.stran373.ui.SignIn.SignInFragmentArgs;
import edu.uw.tcss450.stran373.ui.SignIn.SignInFragmentDirections;
import edu.uw.tcss450.stran373.ui.SignIn.SignInViewModel;

import static android.view.View.GONE;

/**
 * Represents the weather search feature of the application.
 */
public class WeatherSearchFragment extends Fragment {

    /**
     * Bindings used to access fragment elements.
     */
    private FragmentWeatherSearchBinding mBinding;

    /**
     * The latitude that is returned from the zip model.
     */
    private double mLat;

    /**
     * The longitude that is returned from the zip model.
     */
    private double mLon;

    /**
     * The location that is returned from the Zip model.
     */
    private String mLocation;

    /**
     * Jwt used for remote authorization.
     */
    private String mJWT;

    /**
     * The ZipModel used to get information about a zipcode entered.
     */
    private ZipcodeViewModel mZipModel;

    /**
     * Executed upon creation to create a new ViewModelProvider for log-in.
     *
     * @param theSavedInstanceState is a Bundle object.
     */
    @Override
    public void onCreate(@Nullable Bundle theSavedInstanceState) {
        super.onCreate(theSavedInstanceState);
        mZipModel = new ViewModelProvider(getActivity())
                .get(ZipcodeViewModel.class);

        mJWT = ((MainActivity) getActivity()).getTheArgs().getJwt();
    }

    /**
     * Creates a view hierarchy upon loading.
     *
     * @param theInflater is a LayoutInflater object.
     * @param theContainer is a ViewGroup object.
     * @param theSavedInstanceState is a Bundle object.
     * @return a View hierarchy
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater theInflater, ViewGroup theContainer,
                             Bundle theSavedInstanceState) {
        mBinding = FragmentWeatherSearchBinding.inflate(theInflater);
        mLat = 0;
        mLon = 0;
        return mBinding.getRoot();
    }

    /**
     * Initializes the search page for the user.
     *
     * @param theView is the View to be passed for navigation.
     * @param theSavedInstanceState is a Bundle object.
     */
    @Override
    public void onViewCreated(@NonNull View theView, @Nullable Bundle theSavedInstanceState) {
        mBinding.textCurrrent.setVisibility(GONE);
        mBinding.buttonAddLocation.setVisibility(GONE);
        mBinding.buttonAddLocation.setEnabled(false);
        mZipModel.addResponseObserver(
                getViewLifecycleOwner(),
                this::observeResponse);

        mBinding.buttonZipSearch.setOnClickListener( view -> {
            mBinding.textCurrrent.setVisibility(View.VISIBLE);
            mBinding.buttonAddLocation.setVisibility(View.VISIBLE);
            Log.v("Zip entered", mBinding.textSearch.getText().toString());
            if(mBinding.textCurrrent.getText().toString() != (getString(R.string.text_weather_search))) {
                mZipModel.connect(mJWT, mBinding.textSearch.getText().toString());
            }
        });

        mBinding.buttonAddLocation.setOnClickListener(view -> {
            MapViewModel addModel = new ViewModelProvider(getActivity())
                    .get(MapViewModel.class);
            addModel.connect(mJWT, mLat, mLon, mLocation);
        });

    }



    /**
     * An observer on the HTTP Response from the web server. This observer should be
     * attached to ZipcodeViewModel.
     *
     * @param theResponse the Response from the server
     */
    private void observeResponse(final JSONObject theResponse) {
        if (theResponse.length() > 0) {
            if (theResponse.has("error_code")) {
                try {
                    mBinding.textCurrrent.setText(
                            "Error Authenticating: " +
                                    theResponse.getString("error_msg"));
                    mBinding.buttonAddLocation.setEnabled(false);
                } catch (JSONException e) {
                    Log.e("JSON Parse Errors", e.getMessage());
                }
            } else {
                try {
                    mBinding.buttonAddLocation.setEnabled(true);
                    mLocation = theResponse.getString("city") + ", " +
                            theResponse.getString("state");
                    mBinding.textCurrrent.setText(mLocation);
                    mLat = theResponse.getDouble("lat");
                    mLon = theResponse.getDouble("lng");
                    Log.d("JSON Response", theResponse.getString("city"));
                } catch (JSONException e) {
                    Log.e("JSON Parse Error", e.getMessage());
                }
            }
        } else {
            Log.d("JSON Response", "No response");
        }
    }
}