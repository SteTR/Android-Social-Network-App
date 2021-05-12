package edu.uw.tcss450.stran373.ui.Register;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import edu.uw.tcss450.stran373.R;
import edu.uw.tcss450.stran373.databinding.FragmentRegisterBinding;

/**
 * Fragment used to house the registration feature.
 */
public class RegisterFragment extends Fragment implements View.OnClickListener {

    /**
     * Binding for this fragment.
     */
    private FragmentRegisterBinding myBinding;

    /**
     * ViewModel used to store information regarding registration.
     */
    private RegisterViewModel myRegisterModel;

    /**
     * Set of special characters used for password validation.
     */
    private char[] mySpecials;

    /**
     * Initializes the registration page for the user.
     *
     * @param theSavedInstanceState is a Bundle object.
     */
    @Override
    public void onCreate(@Nullable Bundle theSavedInstanceState) {
        super.onCreate(theSavedInstanceState);
        mySpecials = new char[] {'!', '?', '&', '$', '#'};
        myRegisterModel = new ViewModelProvider(getActivity())
                .get(RegisterViewModel.class);
    }

    /**
     * Creates a view hierarchy for the user.
     *
     * @param theInflater is a LayoutInflater object.
     * @param theContainer is a ViewGroup object.
     * @param theSavedInstanceState is a Bundle object.
     * @return the application's view hierarchy
     */
    @Override
    public View onCreateView(LayoutInflater theInflater, ViewGroup theContainer,
                             Bundle theSavedInstanceState) {
        myBinding = FragmentRegisterBinding.inflate(theInflater);
        return myBinding.getRoot();
    }

    /**
     * Initializes the buttons and functionalities of the registration page.
     *
     * @param theView is a View object.
     * @param theSavedInstanceState is a Bundle object.
     */
    @Override
    public void onViewCreated(@NonNull View theView, @Nullable Bundle theSavedInstanceState) {
        super.onViewCreated(theView, theSavedInstanceState);
        myBinding.button.setOnClickListener(this);
        myRegisterModel.addResponseObserver(getViewLifecycleOwner(),
                this::observeResponse);
    }

    /**
     * Used to clear the contents of the registration page.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        myBinding = null;
    }

    /**
     * Called when the user clicks on the "register" button to complete registration.
     *
     * @param theView is the current View of the application.
     */
    @Override
    public void onClick(View theView) {
        EditText email = myBinding.editText;
        EditText pw1 = myBinding.editText2;
        EditText pw2 = myBinding.editText3;
        EditText first = myBinding.editText4;
        EditText last = myBinding.editText5;

        int match = pw1.getText().toString().compareTo(pw2.getText().toString());
        boolean validPW1 = verifyPW(pw1.getText().toString());
        boolean emptyE = TextUtils.isEmpty(email.getText().toString());
        boolean emptyFirst = TextUtils.isEmpty(first.getText().toString());
        boolean emptyLast = TextUtils.isEmpty(last.getText().toString());
        boolean verifyE = verifyEmail(email.getText().toString());

        if (emptyE || !verifyE) {
            email.setError("Invalid Email");
        } else if (!validPW1) {
            pw1.setError("Invalid Password");
        } else if (match != 0) {
            pw2.setError("Passwords do not match");
        } else if (emptyFirst) {
            first.setError("Invalid Name");
        } else if (emptyLast) {
            last.setError("Invalid Name");
        } else {
            verifyAuthWithServer();
        }
    }

    /**
     * Helper method used to verify the email.
     *
     * @param theEmail is the email the user types in.
     * @return true if it is valid email, false otherwise.
     */
    private boolean verifyEmail(String theEmail) {
        int i = 0;
        boolean found = false;
        while (i < theEmail.length() && !found &&
                !TextUtils.isEmpty(theEmail)) {
            if (theEmail.charAt(i) == '@') {
                found = true;
            } else {
                i++;
            }
        }
        return found;
    }

    /**
     * Helper method to verify the password.
     *
     * @param thePW is the password the user types in.
     * @return true if it is an acceptable password, false otherwise.
     */
    private boolean verifyPW(String thePW) {
        boolean pw = false;
        if (thePW.length() < 8) {
            myBinding.editText2.setError("Password is less than 8 characters.");
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
            myBinding.editText2.setError("Special character missing");
        }

        return pw;
    }

    /**
     * Helper method used to authenticate with the web server.
     */
    private void verifyAuthWithServer() {
        myRegisterModel.connect(
                myBinding.editText4.getText().toString(),
                myBinding.editText5.getText().toString(),
                myBinding.editText.getText().toString(),
                myBinding.editText2.getText().toString());
    }

    /**
     * Helper method used to navigate to the sign-in page upon successful registration.
     */
    private void navigateToSignIn() {
        RegisterFragmentDirections.ActionRegisterFragmentToSignInFragment directions =
                RegisterFragmentDirections.actionRegisterFragmentToSignInFragment();

        directions.setEmail(myBinding.editText.getText().toString());
        directions.setPassword(myBinding.editText2.getText().toString());

        Navigation.findNavController(getView()).navigate((NavDirections) directions);
        Snackbar.make(getView(), R.string.email_sent, Snackbar.LENGTH_LONG).show();
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
                    myBinding.editText.setError(
                            "Error Authenticating: " +
                                    theResponse.getJSONObject("data").getString("message"));

                } catch (JSONException e) {
                    Log.e("JSON Parse Error", e.getMessage());
                }
            } else {
                navigateToSignIn();
            }
        } else {
            Log.d("JSON Response", "No Response");
        }
    }

}