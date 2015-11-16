package pl.polsl.pum2.shoppingapp.gui;


import android.support.v7.app.AlertDialog;
import android.app.Dialog;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;

import pl.polsl.pum2.shoppingapp.R;


public class MessageDialogFragment extends DialogFragment {


    public MessageDialogFragment() {
        // Required empty public constructor
    }


    @Override @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        Bundle arguments = getArguments();
        String message = arguments.getString("Message");
        if (message == null) {
            message = "";
        }
        builder.setMessage(message)
                .setPositiveButton(R.string.ok_button, null);
        return builder.create();
    }


}
