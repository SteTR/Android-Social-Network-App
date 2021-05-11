package edu.uw.tcss450.stran373;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import edu.uw.tcss450.stran373.utils.Utils;

public class AuthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.ThemeC);
        setContentView(R.layout.activity_auth);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu theMenu) {
        getMenuInflater().inflate(R.menu.toolbar, theMenu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull final MenuItem theItem) {
        int id = theItem.getItemId();

        if (id == R.id.theme_cherry){
            Utils.changeToTheme(this, Utils.THEME_DEFAULT);
            return true;
        } else if (id == R.id.theme_orange) {
            Utils.changeToTheme(this, Utils.THEME_ORANGE);
            return true;
        } else if (id == R.id.theme_pacnw) {
            Utils.changeToTheme(this, Utils.THEME_PACNW);
        }

        return super.onOptionsItemSelected(theItem);
    }
}
