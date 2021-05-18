package edu.uw.tcss450.stran373;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
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
                        if (which == 0){
                            Utils.changeToTheme(getActivity(), Utils.THEME_DEFAULT);
                        } else if (which == 1) {
                            Utils.changeToTheme(getActivity(), Utils.THEME_ORANGE);
                        } else if (which == 2) {
                            Utils.changeToTheme(getActivity(), Utils.THEME_PACNW);
                        }
                    }
                });
        return builder.create();
    }
}
