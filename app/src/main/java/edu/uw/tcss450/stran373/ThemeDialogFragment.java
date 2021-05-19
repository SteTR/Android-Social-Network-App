package edu.uw.tcss450.stran373;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import edu.uw.tcss450.stran373.utils.Utils;

/**
 * @author Bryce Fujita
 * Dialog message used to chose a theme.
 */
public class ThemeDialogFragment extends DialogFragment {

    /**
     * Method used to create a diolog message for the user
     * AKA a "pop-up" window.
     * @param savedInstanceState Bundle from previous state
     * @return The new Dialog window
     */
    @Override
    public Dialog onCreateDialog(@NonNull Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.title_themes_toolbar)
                .setItems(R.array.themes, (dialog, which) -> {
                    int var = 0;
                    if (which == 0){
                        var = Utils.THEME_DEFAULT;
                    } else if (which == 1) {
                        var = Utils.THEME_ORANGE;
                    } else if (which == 2) {
                        var = Utils.THEME_PACNW;
                    }
                    SharedPreferences prefs =
                            getActivity().getSharedPreferences(
                                    getString(R.string.keys_shared_prefs),
                                    Context.MODE_PRIVATE);
                    prefs.edit().putInt(getString(R.string.keys_prefs_theme),var).apply();
                    Utils.changeToTheme(getActivity(), var);
                    dismiss();
                });
        return builder.create();
    }
}
