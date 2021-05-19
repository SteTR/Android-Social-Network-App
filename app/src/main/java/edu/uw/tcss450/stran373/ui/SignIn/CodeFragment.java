package edu.uw.tcss450.stran373.ui.SignIn;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;
import org.json.JSONObject;

import edu.uw.tcss450.stran373.databinding.FragmentCodeBinding;

/**
 * Reset password fragment
 *
 * @author Haoying Li
 * @author Steven Tran
 */
public class CodeFragment extends Fragment {

    private FragmentCodeBinding binding;
    private String email;
    private ResetViewModel mResetModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCodeBinding.inflate(inflater);
        mResetModel = new ViewModelProvider(getActivity()).get(ResetViewModel.class);
        email = CodeFragmentArgs.fromBundle(getArguments()).getEmail();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonReset.setOnClickListener(this::attemptReset);
        mResetModel.addResponseObserver(getViewLifecycleOwner(), this::observeResponse);
    }
    // TODO need to validate email and password frontend

    private void attemptReset(final View button) {
        mResetModel.connectPut(email,
                Integer.parseInt(binding.editCode.getText().toString()),
                binding.editPassword1.getText().toString());
    }

    /**
     * An observer on the HTTP Response from the web server. This observer should be attached to
     * SignInViewModel.
     *
     * @param response the Response from the server
     */
    private void observeResponse(final JSONObject response) {
        if (response.length() > 0) {
            if (response.has("code")) {
                try {
                    binding.buttonReset.setError(
                            "Error Authenticating: " + response.getJSONObject("data").getString(
                                    "message"));
                } catch (JSONException e) {
                    Log.e("JSON Parse Error", e.getMessage());
                }
            } else {
                navigateToSuccess();
            }
        } else {
            Log.d("JSON Response", "No Response");
        }
    }

    /**
     * Helper to abstract the navigation to the Activity past Authentication.
     */
    private void navigateToSuccess() {
        Navigation.findNavController(getView()).navigate(
                CodeFragmentDirections.actionCodeFragmentToSignInFragment());
    }
}