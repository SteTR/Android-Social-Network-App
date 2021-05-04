package edu.uw.tcss450.stran373;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

/**
 * The inner activity used to house other functionalities (chat, contacts, etc.).
 */
public class MainActivity extends AppCompatActivity {

    /**
     * The set of arguments this activity has (specified in main_graph).
     */
    private MainActivityArgs args;

    /**
     * Initializes the SuccessFragment for the user.
     *
     * @param theSavedInstanceState is a bundle object.
     */
    @Override
    protected void onCreate(Bundle theSavedInstanceState) {
        super.onCreate(theSavedInstanceState);
        setContentView(R.layout.activity_main);
        args = MainActivityArgs.fromBundle(getIntent().getExtras());
        new ViewModelProvider(this,
                new UserInfoViewModel.UserInfoViewModelFactory(args.getJwt(), args.getEmail())
        ).get(UserInfoViewModel.class);

    }

//    @Override
//    public boolean onSupportNavigateUp() {
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
//    }
//
//    public MainActivityArgs getTheArgs() {
//        return args;
//    }
}