package edu.uw.tcss450.stran373;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.auth0.android.jwt.JWT;

/**
 *
 */
public class UserInfoViewModel extends androidx.lifecycle.ViewModel {


//    private final JWT mJwt;

    /**
     *
     */
    private static String mJwt;

    /**
     *
     */
    private String mEmail;

//    private UserInfoViewModel(JWT jwt) {
//        mJwt = jwt;
//    }

    /**
     *
     *
     * @param jwt
     * @param email
     */
    private UserInfoViewModel(String jwt, String email) {
        mJwt = jwt;
        mEmail = email;
    }

//    public boolean isExpired() {
//        return mJwt.isExpired(0);
//    }

    /**
     *
     *
     * @return
     */
    public String getEmail() {
//        if (!mJwt.isExpired(0)) {
//            return mJwt.getClaim("email").asString();
//        } else {
//            throw new IllegalArgumentException("JWT is expired");
//        }
        return mEmail;
    }

    /**
     *
     *
     * @return
     */
    public static String getJwt() {
        return mJwt;
    }

    /**
     *
     */
    public static class UserInfoViewModelFactory implements ViewModelProvider.Factory {

//        private final JWT jwt;

        /**
         *
         */
        private final String jwt;

        /**
         *
         */
        private final String email;

//        public UserInfoViewModelFactory(JWT jwt) {
//            this.jwt = jwt;
//        }

        /**
         *
         *
         * @param jwt
         * @param email
         */
        public UserInfoViewModelFactory(final String jwt, final String email) {
            this.jwt = jwt;
            this.email = email;
        }

        /**
         *
         *
         * @param modelClass
         * @param <T>
         * @return
         */
        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass == UserInfoViewModel.class) {
                return (T) new UserInfoViewModel(jwt, email);
            }

            throw new IllegalArgumentException("Argument must be: " + UserInfoViewModel.class);
        }
    }
}