package edu.uw.tcss450.stran373;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import edu.uw.tcss450.stran373.utils.Utils;

public class ThemeDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.title_themes_toolbar)
                .setItems(R.array.themes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
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
                    }
                });
        return builder.create();
    }
}
