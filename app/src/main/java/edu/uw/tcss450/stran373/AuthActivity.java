package edu.uw.tcss450.stran373;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import edu.uw.tcss450.stran373.utils.Utils;

/**
 * A class used to hold all of the Authentification Fragments.
 */
public class AuthActivity extends AppCompatActivity {
    /**
     * Method called when Activity is created.
     * @param theSavedInstanceState stored information.
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
        setContentView(R.layout.activity_auth);
    }

    /**
     * Method called which inflates a menu and its items.
     * @param theMenu the menu item being inflated
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
        }

        return super.onOptionsItemSelected(theItem);
    }
}
