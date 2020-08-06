package com.universal.homear;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

/**
 * A simple DialogFragment used to show quickstart instructions for the ARViewer
 */
public class ArViewerHelpDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("\n1. To place an object, simply touch the screen.\n\n" +
                "2. You can rotate the object, using two fingers.\n\n" +
                "3. To move the object, drag the object with one finger.\n\n" +
                "4. To resize the object, use two fingers to pinch the screen.\n\n" +
                "5. To clear an object, press the clear button.\n\n\n")
        .setTitle("Quickstart")
        .setPositiveButton("Close", null);
        return builder.create();
    }
}
