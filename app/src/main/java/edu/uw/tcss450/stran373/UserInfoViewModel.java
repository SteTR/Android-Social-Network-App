package edu.uw.tcss450.stran373;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.auth0.android.jwt.JWT;

/**
 * ViewModel used for storage and management of UI-related data regarding sign-in/registration.
 */
public class UserInfoViewModel extends androidx.lifecycle.ViewModel {

    /**
     * The JWT of the current user.
     */
    private static String mJwt;

    /**
     * The email of the current user.
     */
    private String mEmail;

    /**
     * Private constructor for the ViewModel.
     *
     * @param theJwt is the user's jwt
     * @param theEmail is the user's email
     */
    private UserInfoViewModel(String theJwt, String theEmail) {
        mJwt = theJwt;
        mEmail = theEmail;
    }

    /**
     * Getter method for the user's current email.
     *
     * @return the user's current email
     */
    public String getEmail() {
        return mEmail;
    }

    /**
     * Getter method for the user's current JWT.
     *
     * @return the user's current JWT
     */
    public static String getJwt() {
        return mJwt;
    }

    /**
     * Inner class used to assist saving UI-related data.
     */
    public static class UserInfoViewModelFactory implements ViewModelProvider.Factory {

        /**
         * The user's current jwt.
         */
        private final String aJwt;

        /**
         * The user's current email.
         */
        private final String aEmail;

        /**
         * Constructor for the inner class itself.
         *
         * @param theJwt is the user's current JWT.
         * @param theEmail is the user's current email.
         */
        public UserInfoViewModelFactory(final String theJwt, final String theEmail) {
            aJwt = theJwt;
            aEmail = theEmail;
        }

        /**
         * Default create method for the ViewModelFactory
         *
         * @param theModelClass is a modelClass that will be checked if it is a UserInfoViewModel.
         * @return A new UserInfoViewModel
         */
        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> theModelClass) {
            if (theModelClass == UserInfoViewModel.class) {
                return (T) new UserInfoViewModel(aJwt, aEmail);
            }

            throw new IllegalArgumentException("Argument must be: " + UserInfoViewModel.class);
        }
    }
}