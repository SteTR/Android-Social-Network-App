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
import java.util.List;
import java.util.Locale;

import edu.uw.tcss450.stran373.MainActivity;
import edu.uw.tcss450.stran373.R;
import edu.uw.tcss450.stran373.databinding.FragmentMapBinding;
import edu.uw.tcss450.stran373.databinding.FragmentWeatherBinding;
import edu.uw.tcss450.stran373.ui.Home.LocationViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback,
        GoogleMap.OnMapClickListener, View.OnClickListener {

    private FragmentMapBinding mBinding;

    private LocationViewModel mModel;

    private WeatherViewModel mWeather;

    private GoogleMap mMap;

    private MainActivity mMain;

    private String mJWT;

    private Geocoder mCoder;

    private List<Address> mAddresses;

    private AlertDialog.Builder mBuilder;

    private LatLng mLatLng;

    private Marker mMarker;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMain = (MainActivity) getActivity();
        mJWT = mMain.getTheArgs().getJwt();
        mCoder = new Geocoder(getContext(), Locale.getDefault());
        mBuilder = new AlertDialog.Builder(getContext());

    }

    @Override
    public void onClick(View view) {
        AlertDialog alert = mBuilder.create();
        alert.show();
    }

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
        if (!mBinding.addLocButton.isEnabled()) {
            mBinding.addLocButton.setEnabled(true);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mBinding = FragmentMapBinding.bind(getView());

        mModel = new ViewModelProvider(getActivity())
                .get(LocationViewModel.class);
        mModel.addLocationObserver(getViewLifecycleOwner(), location ->
                mBinding.textLatLong.setText(location.getLatitude()
                        + ", " + location.getLongitude()));
        mWeather = new ViewModelProvider(getActivity())
                .get(WeatherViewModel.class);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mBinding.addLocButton.setOnClickListener(this);
        mBinding.addLocButton.setEnabled(false);
        mBuilder.setTitle("Confirm");
        mBuilder.setMessage("Do you want to add location?");

        // If the user enters yes
        mBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                try {
                    mAddresses = mCoder.getFromLocation(mLatLng.latitude, mLatLng.longitude, 1);
                    long zipcode = Long.parseLong(mAddresses.get(0).getPostalCode());
                    boolean newLoc = mWeather.setZip(zipcode);
                    if (newLoc) {
                        Log.d("NEW CONNECTION: ", "" + zipcode);
                        mWeather.connect(mJWT);
                    } else {
                        Toast.makeText(getContext(), "Location already exists.", Toast.LENGTH_SHORT);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("ERROR: ", "Unable to process location");
                }
                dialog.dismiss();
            }
        });

        // If the user enters no.
        mBuilder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
    }

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