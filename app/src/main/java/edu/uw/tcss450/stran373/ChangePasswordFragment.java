package edu.uw.tcss450.stran373;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import edu.uw.tcss450.stran373.databinding.FragmentChangePasswordBinding;
import edu.uw.tcss450.stran373.databinding.FragmentResetPasswordBinding;
import edu.uw.tcss450.stran373.io.RequestQueueSingleton;

/**
 * create an instance of this fragment.
 */
public class ChangePasswordFragment extends Fragment {

    private UserInfoViewModel mUserViewModel;
    private FragmentChangePasswordBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserViewModel = new ViewModelProvider(getActivity()).get(UserInfoViewModel.class);
        setHasOptionsMenu(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_change_password, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentChangePasswordBinding.bind(getView());

        binding.buttonConfirmChangePassword.setOnClickListener(buttonView -> {
            String oldpassword = binding.editPasswordCurrent.getText().toString();
            String newpassword = binding.editChangePassword.getText().toString();

            //
            if (!(oldpassword.isEmpty() && newpassword.isEmpty()) &&
                    binding.editChangePasswordConfirm.getText().toString().equals(newpassword)) {
                binding.buttonConfirmChangePassword.setEnabled(false);
                sendChangePasswordRequest(oldpassword, newpassword);
            } else {
                binding.editPasswordCurrent.setError(getString(R.string.text_password_invalid));
            }
        });
    }

    /**
     * Sends a PUT reset request to the server
     *
     * @param oldpassword old password to verify
     * @param newpassword new password to change to
     */
    private void sendChangePasswordRequest(final String oldpassword, final String newpassword) {
        Snackbar.make(getView(), R.string.snackbar_password_sent, Snackbar.LENGTH_LONG).show();
        Log.d("old", oldpassword);
        Log.d("new", newpassword);
        final String url =  getString(R.string.base_url) + "auth/changePassword";

        final JSONObject body = new JSONObject();
        try {
            body.put("oldpassword", oldpassword);
            body.put("newpassword", newpassword);
        } catch(JSONException theE) {
            theE.printStackTrace();
        }
        Request request = new JsonObjectRequest(
                Request.Method.PUT,
                url,
                body,
                this::navigateToSuccess,
                this::handleError) {

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                // add headers <key,value>
                headers.put("Authorization", mUserViewModel.getJwt());
                return headers;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueueSingleton.getInstance(requireActivity().getApplicationContext())
                .addToRequestQueue(request);
    }

    /**
     * Handles unsuccessful response when attemping to reset
     *
     * @param error the error
     */
    private void handleError(final VolleyError error) {
        binding.buttonConfirmChangePassword.setEnabled(true);
        if (Objects.isNull(error.networkResponse)) {
            Log.e("NETWORK ERROR", error.getMessage());
        }
        else {
            String data = new String(error.networkResponse.data, Charset.defaultCharset());
            Log.e("CLIENT ERROR",
                    error.networkResponse.statusCode +
                            " " +
                            data);
            Snackbar.make(getView(), R.string.snackbar_password_sent_failure, Snackbar.LENGTH_LONG).show();
        }
    }

    private void navigateToSuccess(final JSONObject theJSONObject) {
        binding.buttonConfirmChangePassword.setEnabled(true);
        ((MainActivity) requireActivity()).signOut();
    }
}