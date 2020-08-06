package com.universal.homear;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

/**
 * A simple dialog fragment for the checkout button
 */
public class CheckoutDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("\nCheckout has not been implemented due to the business complexity of " +
                "the feature i.e. online financial payments and transaction.\n\n\n")
                .setTitle("Checkout To Be Added")
                .setPositiveButton("Close", null);
        return builder.create();
    }
}
