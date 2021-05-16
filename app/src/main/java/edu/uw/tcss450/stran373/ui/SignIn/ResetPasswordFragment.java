package edu.uw.tcss450.stran373.ui.SignIn;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.uw.tcss450.stran373.R;
import edu.uw.tcss450.stran373.databinding.FragmentResetPasswordBinding;

/**
 * Reset password fragment
 */
public class ResetPasswordFragment extends Fragment {

    private FragmentResetPasswordBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentResetPasswordBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonReset.setOnClickListener(button ->
                Navigation.findNavController(getView()).navigate(
                        ResetPasswordFragmentDirections.actionResetPasswordFragmentToCodeFragment()
                ));
    }
}