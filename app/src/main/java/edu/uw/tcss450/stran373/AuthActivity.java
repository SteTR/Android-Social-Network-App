package edu.uw.tcss450.stran373;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import edu.uw.tcss450.stran373.model.PushyTokenViewModel;
import edu.uw.tcss450.stran373.utils.Utils;
import me.pushy.sdk.Pushy;

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

        //If it is not already running, start the Pushy listening service
        Pushy.listen(this);
        initiatePushyTokenRequest();
    }

    private void initiatePushyTokenRequest() {
        new ViewModelProvider(this).get(PushyTokenViewModel.class).retrieveToken();
    }
}
