package edu.uw.tcss450.stran373.ui.SignIn;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.uw.tcss450.stran373.databinding.FragmentResetPasswordBinding;

/**
 * Reset password fragment
 */
public class ResetPasswordFragment extends Fragment {

    private FragmentResetPasswordBinding binding;
    private ResetViewModel mResetModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentResetPasswordBinding.inflate(inflater);
        mResetModel = new ViewModelProvider(getActivity()).get(ResetViewModel.class);
        return binding.getRoot();
    }

    // TODO need to verify email is valid
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonReset.setOnClickListener(button -> {
            // TODO need to notify user that reset password has been sent. Atm, just makes a request
            //  hopes it's right and moves on.
            mResetModel.connectPost(binding.editEmail.getText().toString());
            Navigation.findNavController(getView()).navigate(
                    ResetPasswordFragmentDirections.actionResetPasswordFragmentToCodeFragment(binding.editEmail.getText().toString()));
                });
    }
}