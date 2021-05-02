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
 * A simple {@link Fragment} subclass.
 * Use the {@link SuccessFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
/**
 * A simple {@link Fragment} subclass.
 */
public class SuccessFragment extends Fragment {

    /**
     *
     */
    private FragmentSuccessBinding binding;

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
        // Inflate the layout for this fragment
        binding = FragmentSuccessBinding.inflate(inflater, container, false);
        return binding.getRoot();
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
        SuccessFragmentArgs args = SuccessFragmentArgs.fromBundle(getArguments());
        binding.textLabel.setText("Hello " + args.getEmail());

//        UserInfoViewModel model = new ViewModelProvider(getActivity()).get(UserInfoViewModel.class);
//        binding.textLabel.setText("Hello " + model.getEmail() + "!");
    }

    /**
     *
     *
     * @param theEmail
     */
    private void updateContent(String theEmail) {
        binding.textLabel.setText("Hello " + theEmail);
    }
}