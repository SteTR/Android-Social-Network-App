package edu.uw.tcss450.stran373.login;

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

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;

import org.json.JSONException;
import org.json.JSONObject;

import edu.uw.tcss450.stran373.R;
import edu.uw.tcss450.stran373.databinding.FragmentSignInBinding;

/**
 * Fragment that houses the sign-in feature.
 */
public class SignInFragment extends Fragment implements View.OnClickListener {

    /**
     * Binding for this fragment.
     */
    private FragmentSignInBinding mBinding;

    /**
     * ViewModel used to manage UI-related data regarding sign-in.
     */
    private SignInViewModel mSignInModel;

    /**
     * Collection of special characters used for password verification.
     */
    private char[] mSpecials;

    /**
     * Executed upon creation to create a new ViewModelProvider for log-in.
     *
     * @param theSavedInstanceState is a Bundle object.
     */
    @Override
    public void onCreate(@Nullable Bundle theSavedInstanceState) {
        super.onCreate(theSavedInstanceState);
        mSpecials = new char[] {'!', '?', '&', '$', '#'};
        mSignInModel = new ViewModelProvider(getActivity())
                .get(SignInViewModel.class);
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
    public View onCreateView(LayoutInflater theInflater, ViewGroup theContainer,
                             Bundle theSavedInstanceState) {
        mBinding = FragmentSignInBinding.inflate(theInflater);
        return mBinding.getRoot();
    }

    /**
     * Used to "destroy" the contents of this fragment's view.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }

    /**
     * Called when the user clicks the "Register" button to go to the registration page.
     *
     * @param theView is the View to be passed for navigation.
     */
    @Override
    public void onClick(View theView) {
        SignInFragmentDirections.ActionSignInFragmentToRegistrationFragment directions2 =
                SignInFragmentDirections.actionSignInFragmentToRegistrationFragment(0);
        Navigation.findNavController(getView()).navigate(directions2);
    }

    /**
     * Initializes the sign-in page for the user.
     *
     * @param theView is the View to be passed for navigation.
     * @param theSavedInstanceState is a Bundle object.
     */
    @Override
    public void onViewCreated(@NonNull View theView, @Nullable Bundle theSavedInstanceState) {
        mBinding.registerButton.setOnClickListener(this);
        mBinding.signInButton.setOnClickListener(this::verify);
        mSignInModel.addResponseObserver(
                getViewLifecycleOwner(),
                this::observeResponse);

        SignInFragmentArgs args = SignInFragmentArgs.fromBundle(getArguments());
        mBinding.editText.setText(args.getEmail().equals("default") ? "" : args.getEmail());
        mBinding.editText2.setText(args.getPassword().equals("default") ? "" : args.getPassword());
    }

    /**
     * Helper method for verifying both the email and the password.
     *
     * @param theView is the View object used to
     */
    private void verify(View theView) {
        EditText email = mBinding.editText;
        EditText password = mBinding.editText2;
        String emailString = email.getText().toString();
        String pwString = password.getText().toString();
        boolean verifyE = verifyEmail(emailString);
        boolean verifyPW = verifyPW(pwString);
        if (!verifyE || !verifyPW) {
            if (!verifyE) {
                email.setError("Invalid Email");
            } else {
                password.setError("Invalid Password");
            }
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
            mBinding.editText.setError("Email is less than 8 characters.");
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
            mBinding.editText2.setError("Password is less than 8 characters.");
        } else {
            for (int i = 0; i < thePW.length(); i++) {
                int j = 0;
                while (j < mSpecials.length && !pw) {
                    if (thePW.charAt(i) == mSpecials[j]) {
                        pw = true;
                    } else {
                        j++;
                    }
                }
            }
        }

        if (!pw) {
            mBinding.editText2.setError("Special character missing");
        }

        return pw;
    }

    /**
     * Helper method used to authenticate with the web server.
     */
    private void verifyAuthWithServer() {
        mSignInModel.connect(
                mBinding.editText.getText().toString(),
                mBinding.editText2.getText().toString());
    }

    /**
     * Helper to abstract the navigation to the Activity past Authentication.
     *
     * @param theEmail users email
     * @param theJwt the JSON Web Token supplied by the server
     */
    private void navigateToSuccess(final String theEmail, final String theJwt) {
//        Navigation.findNavController(getView())
//                .navigate(SignInFragmentDirections
//                        .actionSignInFragmentToMainActivity(theJwt, theEmail));
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
                    mBinding.editText.setError(
                            "Error Authenticating: " +
                                    theResponse.getJSONObject("data").getString("message"));
                } catch (JSONException e) {
                    Log.e("JSON Parse Error", e.getMessage());
                }
            } else {
                try {
                    navigateToSuccess(
                            mBinding.editText.getText().toString(),
                            theResponse.getString("token"));
                    Log.d("JSON Response", theResponse.getString("token"));
                } catch (JSONException e) {
                    Log.e("JSON Parse Error", e.getMessage());
                }
            }
        } else {
            Log.d("JSON Response", "No response");
        }
    }
}