package edu.uw.tcss450.stran373;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.BroadcastReceiver;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
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

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.DecimalFormat;

import edu.uw.tcss450.stran373.ui.Home.HomeViewModel;
import edu.uw.tcss450.stran373.ui.Home.LocationViewModel;
import edu.uw.tcss450.stran373.databinding.ActivityMainBinding;
import edu.uw.tcss450.stran373.model.NewMessageCountViewModel;
import edu.uw.tcss450.stran373.model.PushyTokenViewModel;
import edu.uw.tcss450.stran373.services.PushReceiver;
import edu.uw.tcss450.stran373.ui.Chat.Conversation.ChatViewModel;
import edu.uw.tcss450.stran373.ui.Chat.Message.ChatMessage;
import edu.uw.tcss450.stran373.ui.Contact.RequestsInfo.RequestCardRecyclerViewAdapter;
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

    private ActivityMainBinding binding;

    private MainPushMessageReceiver mPushMessageReceiver;
    private NewMessageCountViewModel mNewMessageModel;

    private FusedLocationProviderClient mFusedLocationClient;

    private HomeViewModel mHomeModel;

    private LocationRequest mRequest;

    private LocationCallback mCallback;

    private Location mLoc;

    private LocationViewModel mLocModel;

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
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Get our args.
        mArgs = MainActivityArgs.fromBundle(getIntent().getExtras());

        // Our new ViewModelProvider
        new ViewModelProvider(this,
                new UserInfoViewModel.UserInfoViewModelFactory(mArgs.getJwt(), mArgs.getEmail())
        ).get(UserInfoViewModel.class);

        BottomNavigationView navView = findViewById(R.id.nav_view);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_weather,
                R.id.navigation_contacts, R.id.navigation_chats).build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        mNewMessageModel = new ViewModelProvider(this).get(NewMessageCountViewModel.class);

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() == R.id.navigation_single_chat) {
                //When the user navigates to the chats page, reset the new message count.
                //This will need some extra logic for your project as it should have
                //multiple chat rooms.
                mNewMessageModel.reset();
            }
        });
        mNewMessageModel.addMessageCountObserver(this, count -> {
            BadgeDrawable badge = binding.navView.getOrCreateBadge(R.id.navigation_chats);
            badge.setMaxCharacterCount(2);
            if (count > 0) {
                //new messages! update and show the notification badge.
                badge.setNumber(count);
                badge.setVisible(true);
            } else {
                //user did some action to clear the new messages, remove the badge
                badge.clearNumber();
                badge.setVisible(false);
            }
        });
        // Prompt the user for permission to access the device's location.
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(
                MainActivity.this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            // Get the current location.
            requestLocation();
        }

        mCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }

                for (Location location: locationResult.getLocations()) {
                    Log.d("Location: ", location.toString());
                    if (mLocModel == null) {
                        mLocModel = new ViewModelProvider(MainActivity.this)
                                .get(LocationViewModel.class);
                    }
                    mLocModel.setLocation(location);
                }
            }
        };
        createLocationRequest();
    }

    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    requestLocation();
                } else {
                    finishAndRemoveTask();
                }
            }
        }
    }

    private void createLocationRequest() {
        mRequest = LocationRequest.create();
        mRequest.setInterval(UPDATE_INTERVAL);
        mRequest.setFastestInterval(FASTEST_UPDATE);
        mRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void requestLocation() {
        if (ActivityCompat.checkSelfPermission(
                MainActivity.this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        } else {
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                mLocModel = new ViewModelProvider(MainActivity.this)
                                        .get(LocationViewModel.class);
                                Log.d("Current Lat/Long:", location.getLatitude() + "/" + location.getLongitude());
                                Log.d("Call 1: ", "Here");
                                Log.d("Current location: ", location.toString());
                                mLoc = location;
                                mLocModel.setLocation(location);
                            }

                        }
                    });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        startLocationUpdates();
        if (mPushMessageReceiver == null) {
            mPushMessageReceiver = new MainPushMessageReceiver();
        }
        IntentFilter iFilter = new IntentFilter(PushReceiver.RECEIVED_NEW_MESSAGE);
        registerReceiver(mPushMessageReceiver, iFilter);
    }
    @Override
    public void onPause() {
        super.onPause();
        mFusedLocationClient.removeLocationUpdates(mCallback);
        if (mPushMessageReceiver != null){
            unregisterReceiver(mPushMessageReceiver);
        }
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

        if (id == R.id.theme_selection) {
            ThemeDialogFragment dialog = new ThemeDialogFragment();
            dialog.show(getSupportFragmentManager(), "");
            return true;
        } else if (id == R.id.changePasswordFragment) {
            // Navigate to change password
            Navigation.findNavController(this, R.id.nav_host_fragment)
                    .navigate(NavGraphDirections.actionGlobalChangePasswordFragment());
            return true;
        } else if (id == R.id.action_sign_out) {
            signOut();
        }

        return super.onOptionsItemSelected(theItem);
    }

    /**
     * Method used to sign a user out/remove their jwt
     */
    public void signOut() {
        SharedPreferences prefs =
                getSharedPreferences(
                        getString(R.string.keys_shared_prefs),
                        Context.MODE_PRIVATE);
        prefs.edit().remove(getString(R.string.keys_prefs_jwt)).apply();
        //End the app completely
        PushyTokenViewModel model = new ViewModelProvider(this)
                .get(PushyTokenViewModel.class);
        //when we hear back from the web service quit
        model.addResponseObserver(this, result -> finishAndRemoveTask());
        model.deleteTokenFromWebservice(
                new ViewModelProvider(this)
                        .get(UserInfoViewModel.class)
                        .getJwt()
        );
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

    /**
     * A BroadcastReceiver that listens for messages sent from PushReceiver
     */
    private class MainPushMessageReceiver extends BroadcastReceiver {
        private ChatViewModel mModel =
                new ViewModelProvider(MainActivity.this)
                        .get(ChatViewModel.class);
        @Override
        public void onReceive(Context context, Intent intent) {
            NavController nc =
                    Navigation.findNavController(
                            MainActivity.this, R.id.nav_host_fragment);
            NavDestination nd = nc.getCurrentDestination();
            if (intent.hasExtra("chatMessage")) {
                ChatMessage cm = (ChatMessage) intent.getSerializableExtra("chatMessage");
                //If the user is not on the chat screen, update the
                // NewMessageCountView Model
                if (nd.getId() != R.id.navigation_single_chat) {
                    mNewMessageModel.increment();
                }
                //Inform the view model holding chatroom messages of the new
                //message.
                mModel.addMessage(intent.getIntExtra("chatid", -1), cm);
            }
        }
    }

}
