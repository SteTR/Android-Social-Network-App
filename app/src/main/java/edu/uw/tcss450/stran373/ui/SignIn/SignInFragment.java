package edu.uw.tcss450.stran373.ui.SignIn;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.auth0.android.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import edu.uw.tcss450.stran373.R;
import edu.uw.tcss450.stran373.UserInfoViewModel;
import edu.uw.tcss450.stran373.databinding.FragmentSignInBinding;
import edu.uw.tcss450.stran373.model.PushyTokenViewModel;

/**
 * Fragment that houses the sign-in feature.
 */
public class SignInFragment extends Fragment implements View.OnClickListener {

    /**
     * Binding for this fragment.
     */
    private FragmentSignInBinding myBinding;

    /**
     * ViewModel used to manage UI-related data regarding sign-in.
     */
    private SignInViewModel mSignInModel;

    /**
     * Collection of special characters used for password verification.
     */
    private char[] mySpecials;

    private PushyTokenViewModel mPushyTokenViewModel;
    private UserInfoViewModel mUserViewModel;

    /**
     * Executed upon creation to create a new ViewModelProvider for log-in.
     *
     * @param theSavedInstanceState is a Bundle object.
     */
    @Override
    public void onCreate(@Nullable Bundle theSavedInstanceState) {
        super.onCreate(theSavedInstanceState);
        mySpecials = new char[] {'!', '?', '&', '$', '#'};
        mSignInModel = new ViewModelProvider(getActivity())
                .get(SignInViewModel.class);

        mPushyTokenViewModel = new ViewModelProvider(getActivity())
                .get(PushyTokenViewModel.class);
    }

    /**
     * Creates a view hierarchy upon loading.
     *
     * @param theInflater is a LayoutInflater object.
     * @param theContainer is a ViewGroup object.
     * @param theSavedInstanceState is a Bundle object.
     * @return a View hierarchy
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater theInflater, ViewGroup theContainer,
                             Bundle theSavedInstanceState) {
        myBinding = FragmentSignInBinding.inflate(theInflater);
        return myBinding.getRoot();
    }

    /**
     * Used to "destroy" the contents of this fragment's view.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        myBinding = null;
    }

    /**
     * Called when the user clicks the "Register" button to go to the registration page.
     *
     * @param theView is the View to be passed for navigation.
     */
    @Override
    public void onClick(View theView) {
        Navigation.findNavController(getView())
                .navigate(SignInFragmentDirections
                        .actionSignInFragmentToRegisterFragment());
    }

    /**
     * Initializes the sign-in page for the user.
     *
     * @param theView is the View to be passed for navigation.
     * @param theSavedInstanceState is a Bundle object.
     */
    @Override
    public void onViewCreated(@NonNull View theView, @Nullable Bundle theSavedInstanceState) {
        myBinding.buttonRegister.setOnClickListener(this);
        myBinding.buttonSignin.setOnClickListener(this::verify);
        mSignInModel.addResponseObserver(
                getViewLifecycleOwner(),
                this::observeResponse);

        SignInFragmentArgs args = SignInFragmentArgs.fromBundle(getArguments());
        myBinding.editEmail.setText(args.getEmail().equals("default") ? "" : args.getEmail());
        myBinding.editPassword.setText(args.getPassword().equals("default") ? "" : args.getPassword());

        myBinding.buttonReset.setOnClickListener(button ->
                Navigation.findNavController(getView()).navigate(
                        SignInFragmentDirections.actionSignInFragmentToResetPasswordFragment()
                ));

        //don't allow sign in until pushy token retrieved
        mPushyTokenViewModel.addTokenObserver(getViewLifecycleOwner(), token ->
                myBinding.buttonSignin.setEnabled(!token.isEmpty()));

        mPushyTokenViewModel.addResponseObserver(
                getViewLifecycleOwner(),
                this::observePushyPutResponse);
    }

    /**
     * A method called when the fragment is started.
     * Primarily this method is used for AUTO-SIGN In
     */
    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences prefs =
                getActivity().getSharedPreferences(
                        getString(R.string.keys_shared_prefs),
                        Context.MODE_PRIVATE);
        if (prefs.contains(getString(R.string.keys_prefs_jwt))) {
            String token = prefs.getString(getString(R.string.keys_prefs_jwt), "");
            JWT jwt = new JWT(token);
            // Check to see if the web token is still valid or not. To make a JWT expire after a
            // longer or shorter time period, change the expiration time when the JWT is
            // created on the web service.
            if(!jwt.isExpired(0)) {
                String email = jwt.getClaim("email").asString();
                navigateToSuccess(email, token);
                return;
            }
        }
    }

    /**
     * Helper method for verifying both the email and the password.
     *
     * @param theView is the View object used for interaction.
     */
    private void verify(View theView) {
        EditText email = myBinding.editEmail;
        EditText password = myBinding.editPassword;
        String emailString = email.getText().toString();
        String pwString = password.getText().toString();
        boolean verifyE = !TextUtils.isEmpty(emailString);
        boolean verifyPW = !TextUtils.isEmpty(pwString);
        if (!verifyE) {
            email.setError("Invalid Email");
        } else if (!verifyPW){
            password.setError("Invalid Password");
        } else {
            verifyAuthWithServer();
        }

    }

    /**
     * Helper method to verify the email.
     *
     * @param theEmail is the email the user types in.
     * @return true if the user types in an existing email associated with the account, false otherwise.
     */
    private boolean verifyEmail(String theEmail) {
        int i = 0;
        boolean found = false;
        if (theEmail.length() < 8) {
            myBinding.editEmail.setError("Email is less than 8 characters.");
        } else if (!TextUtils.isEmpty(theEmail)) {
            while (i < theEmail.length() && !found) {
                if (theEmail.charAt(i) == '@') {
                    Log.d("MYLABAPP: ", "Valid Email");
                    found = true;
                } else {
                    i++;
                }
            }
        }

        return found;
    }

    /**
     * Helper method to verify the password.
     *
     * @param thePW is the password the user types in.
     * @return true if the user types in an existing password associated with the account, false otherwise.
     */
    private boolean verifyPW(String thePW) {
        boolean pw = false;
        if (thePW.length() < 8) {
            myBinding.editPassword.setError("Password is less than 8 characters.");
        } else {
            for (int i = 0; i < thePW.length(); i++) {
                int j = 0;
                while (j < mySpecials.length && !pw) {
                    if (thePW.charAt(i) == mySpecials[j]) {
                        pw = true;
                    } else {
                        j++;
                    }
                }
            }
        }

        if (!pw) {
            myBinding.editPassword.setError("Special character missing");
        }

        return pw;
    }

    /**
     * Helper method used to authenticate with the web server.
     */
    private void verifyAuthWithServer() {
        mSignInModel.connect(
                myBinding.editEmail.getText().toString(),
                myBinding.editPassword.getText().toString());
    }

    /**
     * Helper to abstract the navigation to the Activity past Authentication.
     *
     * @param theEmail users email
     * @param theJwt the JSON Web Token supplied by the server
     */
    private void navigateToSuccess(final String theEmail, final String theJwt) {
        if (myBinding.switchSignin.isChecked()) {
            SharedPreferences prefs =
                    getActivity().getSharedPreferences(
                            getString(R.string.keys_shared_prefs),
                            Context.MODE_PRIVATE);
            //Store the credentials in SharedPrefs
            prefs.edit().putString(getString(R.string.keys_prefs_jwt), theJwt).apply();
        }
        Navigation.findNavController(getView())
                .navigate(SignInFragmentDirections
                        .actionSignInFragmentToMainActivity(theEmail, theJwt));
        //Remove THIS activity from the Task list. Pops off the backstack
        getActivity().finish();
    }

    /**
     * An observer on the HTTP Response from the web server. This observer should be
     * attached to SignInViewModel.
     *
     * @param theResponse the Response from the server
     */
    private void observeResponse(final JSONObject theResponse) {
        if (theResponse.length() > 0) {
            if (theResponse.has("code")) {
                try {
                    myBinding.editEmail.setError(
                            "Error Authenticating: " +
                                    theResponse.getJSONObject("data").getString("message"));

                    // If user is not verified, show pop-up message with the option to resend verification email
                    if (theResponse.getJSONObject("data").getString("message").contains("verified")) {
                        Snackbar mSnackbar = Snackbar.make(getView(),
                                R.string.text_email_not_verified, Snackbar.LENGTH_INDEFINITE);
                        mSnackbar.show();
                        mSnackbar.setAction(R.string.button_resend, onClick -> {
                            mSignInModel.resendEmail(myBinding.editEmail.getText().toString(),
                                    myBinding.editPassword.getText().toString());
                            Snackbar.make(getView(), R.string.text_email_sent, Snackbar.LENGTH_LONG).show();
                        });
                    }
                } catch (JSONException e) {
                    Log.e("JSON Parse Error", e.getMessage());
                }
            } else {
                try {
                    mUserViewModel = new ViewModelProvider(getActivity(),
                            new UserInfoViewModel.UserInfoViewModelFactory(
                                    theResponse.getString("token"),
                                    myBinding.editEmail.getText().toString()
                            )).get(UserInfoViewModel.class);
                    sendPushyToken();

//                    navigateToSuccess(
//                            myBinding.editEmail.getText().toString(),
//                            theResponse.getString("token"));
//                    Log.d("JSON Response", theResponse.getString("token"));
                } catch (JSONException e) {
                    Log.e("JSON Parse Error", e.getMessage());
                }
            }
        } else {
            Log.d("JSON Response", "No response");
        }
    }

    /**
     * Helper to abstract the request to send the pushy token to the web service
     */
    private void sendPushyToken() {
        mPushyTokenViewModel.sendTokenToWebservice(mUserViewModel.getJwt());
    }

    /**
     * An observer on the HTTP Response from the web server. This observer should be
     * attached to PushyTokenViewModel.
     *
     * @param response the Response from the server
     */
    private void observePushyPutResponse(final JSONObject response) {
        if (response.length() > 0) {
            if (response.has("code")) {
                //this error cannot be fixed by the user changing credentials...
                myBinding.editEmail.setError(
                        "Error Authenticating on Push Token. Please contact support");
            } else {
                navigateToSuccess(
                        myBinding.editEmail.getText().toString(),
                        mUserViewModel.getJwt()
                );
            }
        }
    }
}