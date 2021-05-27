package edu.uw.tcss450.stran373;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.DecimalFormat;

import edu.uw.tcss450.stran373.ui.Home.HomeViewModel;
import edu.uw.tcss450.stran373.utils.Utils;

/**
 * A class used to contain all of the Main Fragments used.
 */
public class MainActivity extends AppCompatActivity {

    private Double mLat;

    private Double mLng;

    private LocationManager mManager;

    private static final int REQUEST_LOCATION = 8414;

    private static final long UPDATE_INTERVAL = 10000;

    public static final long FASTEST_UPDATE = UPDATE_INTERVAL / 2;

    /**
     * Configurations for the menu bar
     */
    private AppBarConfiguration mAppBarConfiguration;

    /**
     * List of arguments that are passed to main
     */
    private MainActivityArgs mArgs;

    private FusedLocationProviderClient mFusedLocationClient;

    private HomeViewModel mHomeModel;

    private LocationRequest mRequest;

    private LocationCallback mCallback;

    private Location mLoc;

    /**
     * Method called when the activity is created.
     * @param theSavedInstanceState Bundle from previous state
     */
    @Override
    protected void onCreate(final Bundle theSavedInstanceState) {
        super.onCreate(theSavedInstanceState);
        SharedPreferences prefs =
                this.getSharedPreferences(
                        getString(R.string.keys_shared_prefs),
                        Context.MODE_PRIVATE);
        if (prefs.contains(getString(R.string.keys_prefs_theme))) {
            int theme = prefs.getInt(getString(R.string.keys_prefs_theme), 1);
            Utils.onActivityCreateSetTheme(this, theme);
        } else {
            Utils.onActivityCreateSetTheme(this);
        }
        setContentView(R.layout.activity_main);

        // Get our args.
        mArgs = MainActivityArgs.fromBundle(getIntent().getExtras());

        // Our new ViewModelProvider
        new ViewModelProvider(this,
                new UserInfoViewModel.UserInfoViewModelFactory(mArgs.getJwt(), mArgs.getEmail())
        ).get(UserInfoViewModel.class);

        // Prompt the user for permission to access the device's location.
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(
                MainActivity.this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            // Get the current location.
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                mHomeModel = new ViewModelProvider(MainActivity.this)
                                        .get(HomeViewModel.class);
                                Log.d("Current Lat/Long:", mLat + "/" + mLng);
                            }
                            Log.d("Current location: ", location.toString());
                            mLoc = location;
                        }
                    });
        }

//        mCallback = new LocationCallback() {
//            @Override
//            public void onLocationResult(LocationResult locationResult) {
//                if (locationResult == null) {
//                    return;
//                }
//
//                for (Location location: locationResult.getLocations()) {
//                    if (mHomeModel == null) {
//                        mHomeModel = new ViewModelProvider(MainActivity.this)
//                                .get(HomeViewModel.class);
//                    }
//                    mHomeModel.setLocation(location);
//                }
//            }
//        };

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_weather,
                R.id.navigation_contacts, R.id.navigation_chats).build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

    public Location getLoc() {
        return mLoc;
    }

    /**
     * The method which inflates the menu
     * @param theMenu which is being created
     * @return true
     */
    @Override
    public boolean onCreateOptionsMenu(final Menu theMenu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings_menu, theMenu);
        return true;
    }

    /**
     * An event handler for when an item is selected.
     * @param theItem The item chosen
     * @return theItem
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull final MenuItem theItem) {
        int id = theItem.getItemId();

        if (id == R.id.theme_selection){
            ThemeDialogFragment dialog = new ThemeDialogFragment();
            dialog.show(getSupportFragmentManager(), "");
            return true;
        } else if (id == R.id.action_sign_out) {
            signOut();
        }

        return super.onOptionsItemSelected(theItem);
    }

    /**
     * Method used to sign a user out/remove their jwt
     */
    private void signOut() {
        SharedPreferences prefs =
                getSharedPreferences(
                        getString(R.string.keys_shared_prefs),
                        Context.MODE_PRIVATE);
        prefs.edit().remove(getString(R.string.keys_prefs_jwt)).apply();
        //End the app completely
        finishAndRemoveTask();
    }

    /**
     * Method required for use of navigation bar.
     * @return A boolean as to if the navigation can move up
     */
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    /**
     * Getter method to retrieve the Main Activity's arguments.
     *
     * @return all of this activity's arguments
     */
    public MainActivityArgs getTheArgs() {
        return mArgs;
    }

//    /**
//     * Getter method for the latitude.
//     *
//     * @return
//     */
//    public Double getLat() {
//        return mLat;
//    }
//
//    /**
//     * Getter method for the longitude.
//     *
//     * @return
//     */
//    public Double getLng() {
//        return mLng;
//    }
}
