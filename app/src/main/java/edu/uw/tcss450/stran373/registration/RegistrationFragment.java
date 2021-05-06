package edu.uw.tcss450.stran373.registration;

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

import edu.uw.tcss450.stran373.databinding.FragmentRegistrationBinding;

/**
 * Fragment used to house the registration feature.
 */
public class RegistrationFragment extends Fragment implements View.OnClickListener {

    /**
     * Binding for this fragment.
     */
    private FragmentRegistrationBinding mBinding;

    /**
     * ViewModel used to store information regarding registration.
     */
    private RegistrationViewModel mRegisterModel;

    /**
     * Set of special characters used for password validation.
     */
    private char[] mSpecials;

    /**
     * Initializes the registration page for the user.
     *
     * @param theSavedInstanceState is a Bundle object.
     */
    @Override
    public void onCreate(@Nullable Bundle theSavedInstanceState) {
        super.onCreate(theSavedInstanceState);
        mSpecials = new char[] {'!', '?', '&', '$', '#'};
        mRegisterModel = new ViewModelProvider(getActivity())
                .get(RegistrationViewModel.class);
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
        mBinding = FragmentRegistrationBinding.inflate(theInflater);
        return mBinding.getRoot();
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
        mBinding.button.setOnClickListener(this);
        mRegisterModel.addResponseObserver(getViewLifecycleOwner(),
                this::observeResponse);
    }

    /**
     * Used to clear the contents of the registration page.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }

    /**
     * Called when the user clicks on the "register" button to complete registration.
     *
     * @param theView is the current View of the application.
     */
    @Override
    public void onClick(View theView) {
        EditText email = mBinding.editText;
        EditText pw1 = mBinding.editText2;
        EditText pw2 = mBinding.editText3;
        EditText first = mBinding.editText4;
        EditText last = mBinding.editText5;

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
        mRegisterModel.connect(
                mBinding.editText4.getText().toString(),
                mBinding.editText5.getText().toString(),
                mBinding.editText.getText().toString(),
                mBinding.editText2.getText().toString());
    }

    /**
     * Helper method used to navigate to the sign-in page upon successful registration.
     */
    private void navigateToSignIn() {
        RegistrationFragmentDirections.ActionRegistrationFragmentToSignInFragment directions =
                RegistrationFragmentDirections.actionRegistrationFragmentToSignInFragment();

        directions.setEmail(mBinding.editText.getText().toString());
        directions.setPassword(mBinding.editText2.getText().toString());

        Navigation.findNavController(getView()).navigate(directions);

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
                navigateToSignIn();
            }
        } else {
            Log.d("JSON Response", "No Response");
        }
    }

}