package edu.uw.tcss450.stran373.ui.Weather;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import edu.uw.tcss450.stran373.R;
import edu.uw.tcss450.stran373.databinding.ActivityMapsBinding;

/**
 * Represents the activity used for Google Maps.
 *
 * @author Jonathan Lee
 */
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    /**
     * Represents the Google Map for the activity.
     */
    private GoogleMap mMap;

    /**
     * Represents this activity's binding.
     */
    private ActivityMapsBinding binding;

    /**
     * Called when the app wants to create this activity.
     *
     * @param theSavedInstanceState is the saved instance state.
     */
    @Override
    protected void onCreate(Bundle theSavedInstanceState) {
        super.onCreate(theSavedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     *
     * @param theGoogleMap is the Google Map.
     */
    @Override
    public void onMapReady(GoogleMap theGoogleMap) {
        mMap = theGoogleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}