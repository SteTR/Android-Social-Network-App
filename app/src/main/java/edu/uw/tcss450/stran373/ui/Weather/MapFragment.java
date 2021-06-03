package edu.uw.tcss450.stran373.ui.Weather;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

import edu.uw.tcss450.stran373.MainActivity;
import edu.uw.tcss450.stran373.R;
import edu.uw.tcss450.stran373.databinding.FragmentMapBinding;
import edu.uw.tcss450.stran373.databinding.FragmentWeatherBinding;
import edu.uw.tcss450.stran373.ui.Home.LocationViewModel;

/**
 * Used to manage activities related to Google Maps.
 *
 * @author Jonathan Lee
 */
public class MapFragment extends Fragment implements OnMapReadyCallback,
        GoogleMap.OnMapClickListener, View.OnClickListener, Runnable {

    /**
     * Represents this fragment's binding.
     */
    private FragmentMapBinding mBinding;

    /**
     * Represents the location view model.
     */
    private LocationViewModel mModel;

    /**
     * Represents Google Maps.
     */
    private GoogleMap mMap;

    /**
     * Represents the activity housing this fragment.
     */
    private MainActivity mMain;

    /**
     * Represents the JWT used for connection.
     */
    private String mJWT;

    /**
     * Represents the geocoder used to retrieve the location.
     */
    private Geocoder mCoder;

    /**
     * Represents the list of addresses.
     */
    private List<Address> mAddresses;

    /**
     * Represents the lat/long of the selected location.
     */
    private LatLng mLatLng;

    /**
     * Represents a marker used for the map.
     */
    private Marker mMarker;

    /**
     * Represents the view model used to keep track of adding locations.
     */
    private MapViewModel mMapModel;

    /**
     * Constructor for this fragment.
     *
     * @param savedInstanceState is a Bundle object.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMain = (MainActivity) getActivity();
        mJWT = mMain.getTheArgs().getJwt();
        mCoder = new Geocoder(getActivity().getApplicationContext());
        mMapModel = new ViewModelProvider(getActivity()).get(MapViewModel.class);
    }

    /**
     * Necessary to allow implementing the Runnable interface.
     */
    @Override
    public void run() { }

    /**
     * Retrieves local location upon succession. An extra thread is used to decrease the
     * workload of the main thread.
     *
     * @param view is a view used to run this method.
     */
    @Override
    public void onClick(View view) {
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                DecimalFormat df = new DecimalFormat();
                df.setMaximumFractionDigits(3);
                Log.d("LAT/LNG: ", mLatLng.latitude + "/" + mLatLng.longitude);
                try {
                    mAddresses = mCoder.getFromLocation(Double.parseDouble(df.format(mLatLng.latitude)),
                            Double.parseDouble(df.format(mLatLng.longitude)), 1);
                    Log.d("Here: ", mAddresses.get(0).getLocality());
                    mMapModel.connect(mJWT, mAddresses.get(0).getLatitude(), mAddresses.get(0).getLongitude(),
                            mAddresses.get(0).getLocality());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        t1.start();
    }

    /**
     * Used to create a marker on the map and retrieve the coordinates of the selected location.
     *
     * @param latLng is the lat/long coordinates of the selected location.
     */
    @Override
    public void onMapClick(LatLng latLng) {
        Log.d("LAT/LONG", latLng.toString());
        if (mMarker != null) {
            mMarker.setPosition(latLng);
        } else {
            mMarker = mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title("New Marker"));
        }
        mMap.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                        latLng, mMap.getCameraPosition().zoom));
        mLatLng = latLng;
        Log.d("LAT/LNG: ", mLatLng.latitude + "/" + mLatLng.longitude);
        if (!mBinding.addLocButton.isEnabled()) {
            mBinding.addLocButton.setEnabled(true);
        }
    }

    /**
     * Called upon creation of the view to retrieve the inflated view.
     *
     * @param inflater is a LayoutInflater object.
     * @param container is a ViewGroup object.
     * @param savedInstanceState is a Bundle object.
     * @return the inflated view.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    /**
     * Used to set up the necessary view models and buttons for the fragment.
     *
     * @param view is the current view.
     * @param savedInstanceState is a Bundle object.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mBinding = FragmentMapBinding.bind(getView());

        mModel = new ViewModelProvider(getActivity())
                .get(LocationViewModel.class);
        mModel.addLocationObserver(getViewLifecycleOwner(), location ->
                mBinding.textLatLong.setText(location.getLatitude()
                        + ", " + location.getLongitude()));

        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mBinding.addLocButton.setOnClickListener(this);
        mBinding.addLocButton.setEnabled(false);

    }

    /**
     * Preps up the map for usage.
     *
     * @param googleMap is the map used for this fragment.
     */
    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LocationViewModel model = new ViewModelProvider(getActivity())
                .get(LocationViewModel.class);
        model.addLocationObserver(getViewLifecycleOwner(), location -> {
            if (location != null) {
                googleMap.getUiSettings().setZoomControlsEnabled(true);
                googleMap.setMyLocationEnabled(true);

                final LatLng c = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(c, 15.0f));
            }
        });
        mMap.setOnMapClickListener(this);
    }

}