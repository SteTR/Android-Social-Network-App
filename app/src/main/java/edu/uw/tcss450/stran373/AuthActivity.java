package edu.uw.tcss450.stran373;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

/**
 * The "main" activity used for the starting page of the application.
 */
public class AuthActivity extends AppCompatActivity {

    /**
     * Simple method that sets the ContentView upon creation.
     *
     * @param theSavedInstanceState is a Bundle object.
     */
    @Override
    protected void onCreate(Bundle theSavedInstanceState) {
        super.onCreate(theSavedInstanceState);
        setContentView(R.layout.activity_auth);
    }
}