package com.universal.homear;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

/**
 * Simple dialog fragment for disabled features
 */
public class DisabledFeatureDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("\n\n")
                .setTitle("Feature Has Been Disabled")
                .setPositiveButton("Close", null);
        return builder.create();
    }
}
