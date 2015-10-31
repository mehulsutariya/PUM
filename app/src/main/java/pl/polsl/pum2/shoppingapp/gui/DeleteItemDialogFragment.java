package pl.polsl.pum2.shoppingapp.gui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;

import pl.polsl.pum2.shoppingapp.R;


public class DeleteItemDialogFragment extends DialogFragment {

    public DeleteItemDialogFragment() {
        // Required empty public constructor
    }


    @Override @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final ShoppingListFragment targetFragment = (ShoppingListFragment)getTargetFragment();
        Bundle arguments = getArguments();
        String productName = arguments.getString("ProductName");
        if (productName == null) {
            productName = "";
        }
        String message = String.format(getString(R.string.delete_dialog_message), productName);
        builder.setMessage(Html.fromHtml(message))
                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        targetFragment.removeItem();
                    }
                })
                .setNegativeButton(R.string.cancel_button, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        targetFragment.cancelItemRemoving();
                    }
                });
        return builder.create();
    }

}
