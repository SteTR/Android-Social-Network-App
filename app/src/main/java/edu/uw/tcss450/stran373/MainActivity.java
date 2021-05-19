package edu.uw.tcss450.stran373;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.DecimalFormat;

import edu.uw.tcss450.stran373.utils.Utils;

/**
 * A class used to contain all of the Main Fragments used.
 */
public class MainActivity extends AppCompatActivity {

    /**
     * Configurations for the menu bar
     */
    private AppBarConfiguration mAppBarConfiguration;

    /**
     * List of agruments that are passed to main
     */
    private MainActivityArgs mArgs;

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

        mArgs = MainActivityArgs.fromBundle(getIntent().getExtras());

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

    public MainActivityArgs getTheArgs() {
        return mArgs;
    }
}
