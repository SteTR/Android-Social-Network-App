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
import edu.uw.tcss450.stran373.databinding.FragmentSignInBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignInFragment extends Fragment {

    private FragmentSignInBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSignInBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Add Listeners

        binding.buttonRegister.setOnClickListener(button -> goToRegister());
        binding.buttonSignin.setOnClickListener(button -> loginProcess());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void loginProcess() {
        Navigation.findNavController(getView()).navigate(
                SignInFragmentDirections.actionSignInFragmentToMainActivity());

    }

    private void goToRegister() {
        Navigation.findNavController(getView()).navigate(
                SignInFragmentDirections.actionSignInFragmentToRegisterFragment());
    }
}