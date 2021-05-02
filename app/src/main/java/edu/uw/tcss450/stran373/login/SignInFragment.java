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
 * A simple {@link Fragment} subclass.
 */
public class SignInFragment extends Fragment {

    private FragmentSignInBinding myBinding;

    private SignInViewModel mSignInModel;

    private char[] mySpecials;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mySpecials = new char[] {'!', '?', '&', '$', '#'};
        mSignInModel = new ViewModelProvider(getActivity())
                .get(SignInViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

//        binding = FragmentSignInBinding.inflate(inflater, container, false);
        myBinding = FragmentSignInBinding.inflate(inflater);
        return myBinding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        myBinding = null;
    }

//    @Override
//    public void onClick(View view) {
//        SignInFragmentDirections.ActionSignInFragmentToRegisterFragment directions2 =
//                SignInFragmentDirections.actionSignInFragmentToRegisterFragment(0);
//        Navigation.findNavController(getView()).navigate(directions2);
//    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        binding.registerButton.setOnClickListener(this);
        myBinding.signInButton.setOnClickListener(this::verify);
//        mSignInModel.addResponseObserver(
//                getViewLifecycleOwner(),
//                this::observeResponse);

//        SignInFragmentArgs args = SignInFragmentArgs.fromBundle(getArguments());
//        binding.editText.setText(args.getEmail().equals("default") ? "" : args.getEmail());
//        binding.editText2.setText(args.getPassword().equals("default") ? "" : args.getPassword());
    }

    /**
     * Helper method for verifying both the email and the password.
     *
     * @param view is the View object used to
     */
    private void verify(View view) {
        EditText email = myBinding.editText;
        EditText password = myBinding.editText2;
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
            Navigation.findNavController(
                    getView())
                    .navigate(SignInFragmentDirections
                            .actionSignInFragmentToSuccessFragment(generateJwt(emailString), emailString));
//            getActivity().finish();
//            verifyAuthWithServer();
        }

    }

    /**
     * Helper method to verify the email.
     *
     * @param theEmail
     * @return
     */
    private boolean verifyEmail(String theEmail) {
        int i = 0;
        boolean found = false;
        if (theEmail.length() < 8) {
            myBinding.editText.setError("Email is less than 8 characters.");
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
     * @param thePW
     * @return
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

    private String generateJwt(final String email) {
        String token;
        try {
            Algorithm algorithm = Algorithm.HMAC256("secret key don't use a string literal in " +
                    "production code!!!");
            token = JWT.create()
                    .withIssuer("auth0")
                    .withClaim("email", email)
                    .sign(algorithm);
        } catch (JWTCreationException exception){
            throw new RuntimeException("JWT Failed to Create.");
        }
        return token;
    }

    private void verifyAuthWithServer() {
        mSignInModel.connect(
                myBinding.editText.getText().toString(),
                myBinding.editText2.getText().toString());
    }

    /**
     * Helper to abstract the navigation to the Activity past Authentication.
     * @param email users email
     * @param jwt the JSON Web Token supplied by the server
     */
    private void navigateToSuccess(final String email, final String jwt) {
        Navigation.findNavController(getView())
                .navigate(SignInFragmentDirections
                        .actionSignInFragmentToSuccessFragment(jwt, email));
    }

    /**
     * An observer on the HTTP Response from the web server. This observer should be
     * attached to SignInViewModel.
     *
     * @param response the Response from the server
     */
    private void observeResponse(final JSONObject response) {
        if (response.length() > 0) {
            if (response.has("code")) {
                try {
                    myBinding.editText.setError(
                            "Error Authenticating: " +
                                    response.getJSONObject("data").getString("message"));
                } catch (JSONException e) {
                    Log.e("JSON Parse Error", e.getMessage());
                }
            } else {
                try {
                    navigateToSuccess(
                            myBinding.editText.getText().toString(),
                            response.getString("token"));
                    Log.d("JSON Response", response.getString("token"));
                } catch (JSONException e) {
                    Log.e("JSON Parse Error", e.getMessage());
                }
            }
        } else {
            Log.d("JSON Response", "No response");
        }
    }
}