package pl.polsl.pum2.shoppingapp.gui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Html;

import pl.polsl.pum2.shoppingapp.R;


public class DeleteItemDialogFragment extends DialogFragment {

    public static final String ITEM_NAME = "itemName";
    public static final String MESSAGE = "message";

    public static DeleteItemDialogFragment newInstance(String message, String itemName, Fragment targetFragment) {
        DeleteItemDialogFragment dialogFragment = new DeleteItemDialogFragment();
        Bundle arguments = new Bundle();
        arguments.putString(MESSAGE, message);
        arguments.putString(ITEM_NAME, itemName);
        dialogFragment.setArguments(arguments);
        dialogFragment.setTargetFragment(targetFragment, 1);

        return dialogFragment;
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final DeleteItemDialogListener targetFragment = (DeleteItemDialogListener) getTargetFragment();
        Bundle arguments = getArguments();
        String message = arguments.getString(MESSAGE, "");
        String itemName = arguments.getString(ITEM_NAME, "");

        String dialogMessage = String.format("%s <b>%s</b>?", message, itemName);
        builder.setMessage(Html.fromHtml(dialogMessage))
                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        targetFragment.onDeleteItemDialogOK();
                    }
                })
                .setNegativeButton(R.string.cancel_button, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        targetFragment.onDeleteItemDialogCancel();
                    }
                });
        return builder.create();
    }

    public interface DeleteItemDialogListener {
        void onDeleteItemDialogOK();

        void onDeleteItemDialogCancel();
    }

}
