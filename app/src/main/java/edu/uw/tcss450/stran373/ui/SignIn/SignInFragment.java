package edu.uw.tcss450.stran373.ui.SignIn;

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
    private FragmentSignInBinding myBinding;

    /**
     * ViewModel used to manage UI-related data regarding sign-in.
     */
    private SignInViewModel mSignInModel;

    /**
     * Collection of special characters used for password verification.
     */
    private char[] mySpecials;

    /**
     * Executed upon creation to create a new ViewModelProvider for log-in.
     *
     * @param theSavedInstanceState is a Bundle object.
     */
    @Override
    public void onCreate(@Nullable Bundle theSavedInstanceState) {
        super.onCreate(theSavedInstanceState);
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
        myBinding.editTextTextPersonName2.setText(args.getEmail().equals("default") ? "" : args.getEmail());
        myBinding.editTextPassword.setText(args.getPassword().equals("default") ? "" : args.getPassword());
    }

    /**
     * Helper method for verifying both the email and the password.
     *
     * @param theView is the View object used for interaction.
     */
    private void verify(View theView) {
        EditText email = myBinding.editTextTextPersonName2;
        EditText password = myBinding.editTextPassword;
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
     * Helper method used to authenticate with the web server.
     */
    private void verifyAuthWithServer() {
        mSignInModel.connect(
                myBinding.editTextTextPersonName2.getText().toString(),
                myBinding.editTextPassword.getText().toString());
    }

    /**
     * Helper to abstract the navigation to the Activity past Authentication.
     *
     * @param theEmail users email
     * @param theJwt the JSON Web Token supplied by the server
     */
    private void navigateToSuccess(final String theEmail, final String theJwt) {
        Navigation.findNavController(getView())
                .navigate(SignInFragmentDirections
                        .actionSignInFragmentToMainActivity(theJwt, theEmail));
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
                    myBinding.editTextTextPersonName2.setError(
                            "Error Authenticating: " +
                                    theResponse.getJSONObject("data").getString("message"));
                } catch (JSONException e) {
                    Log.e("JSON Parse Error", e.getMessage());
                }
            } else {
                try {
                    navigateToSuccess(
                            myBinding.editTextPassword.getText().toString(),
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