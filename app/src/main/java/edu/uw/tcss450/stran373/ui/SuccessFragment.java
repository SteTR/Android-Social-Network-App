package edu.uw.tcss450.stran373.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.uw.tcss450.stran373.UserInfoViewModel;
import edu.uw.tcss450.stran373.databinding.FragmentSuccessBinding;

/**
 * Fragment that simply displays a welcome message for a valid user.
 */
public class SuccessFragment extends Fragment {

    /**
     * Binding for this fragment.
     */
    private FragmentSuccessBinding binding;

    /**
     * Used to create a view hierarchy associated with the fragment.
     *
     * @param theInflater is a LayoutInflater object.
     * @param theContainer is a ViewGroup object.
     * @param theSavedInstanceState is a Bundle object.
     * @return A View hierarchy
     */
    @Override
    public View onCreateView(LayoutInflater theInflater, ViewGroup theContainer,
                             Bundle theSavedInstanceState) {
        binding = FragmentSuccessBinding.inflate(theInflater, theContainer, false);
        return binding.getRoot();
    }

    /**
     * Used to create a ViewModel upon successful loading.
     *
     * @param theView is a View object.
     * @param theSavedInstanceState is a Bundle object.
     */
    @Override
    public void onViewCreated(@NonNull View theView, @Nullable Bundle theSavedInstanceState) {
        super.onViewCreated(theView, theSavedInstanceState);
        UserInfoViewModel model = new ViewModelProvider(getActivity()).get(UserInfoViewModel.class);
        binding.textLabel.setText("Hello " + model.getEmail() + "!");
    }

    /**
     * Helper method to update the contents of the application.
     *
     * @param theEmail is the user's email.
     */
    private void updateContent(String theEmail) {
        binding.textLabel.setText("Hello " + theEmail);
    }
}