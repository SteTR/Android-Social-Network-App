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
 * A simple {@link Fragment} subclass.
 */
public class RegistrationFragment extends Fragment implements View.OnClickListener {

    /**
     *
     */
    private FragmentRegistrationBinding myBinding;

    /**
     *
     */
    private RegistrationViewModel mRegisterModel;

    /**
     *
     */
    private char[] mySpecials;

    /**
     *
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mySpecials = new char[] {'!', '?', '&', '$', '#'};
        mRegisterModel = new ViewModelProvider(getActivity())
                .get(RegistrationViewModel.class);
    }

    /**
     *
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        myBinding = FragmentRegistrationBinding.inflate(inflater);
        return myBinding.getRoot();
    }

    /**
     *
     *
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        myBinding.button.setOnClickListener(this);
        mRegisterModel.addResponseObserver(getViewLifecycleOwner(),
                this::observeResponse);
    }

    /**
     *
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        myBinding = null;
    }

    /**
     *
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
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
     * @param theEmail
     * @return
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

    /**
     *
     */
    private void verifyAuthWithServer() {
        mRegisterModel.connect(
                myBinding.editText4.getText().toString(),
                myBinding.editText5.getText().toString(),
                myBinding.editText.getText().toString(),
                myBinding.editText2.getText().toString());
    }

    /**
     *
     */
    private void navigateToLogin() {
        RegistrationFragmentDirections.ActionRegistrationFragmentToSignInFragment directions =
                RegistrationFragmentDirections.actionRegistrationFragmentToSignInFragment();

        directions.setEmail(myBinding.editText.getText().toString());
        directions.setPassword(myBinding.editText2.getText().toString());

        Navigation.findNavController(getView()).navigate(directions);

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
                navigateToLogin();
            }
        } else {
            Log.d("JSON Response", "No Response");
        }
    }

}