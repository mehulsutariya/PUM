package pl.polsl.pum2.shoppingapp.gui;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;

import io.realm.Realm;
import io.realm.RealmResults;
import pl.polsl.pum2.shoppingapp.R;
import pl.polsl.pum2.shoppingapp.database.ProductCategory;

public class ProductCategoryDialogFragment extends DialogFragment {

    private ProductCategoryDialogListener listener;
    private Realm realm;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (ProductCategoryDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement ProductCategoryDialogListener");
        }
    }

    /*
    @Override
    public void onStart() {
        super.onStart();

    }
    */

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_product_category, null);
        realm = Realm.getDefaultInstance();
        RealmResults<ProductCategory> productCategories = realm.where(ProductCategory.class).findAll();
        AutocompleteAdapter autocompleteAdapter = new AutocompleteAdapter(getContext(), productCategories);
        final AutoCompleteTextView categoryName = (AutoCompleteTextView) view.findViewById(R.id.category_name_edit);
        categoryName.setAdapter(autocompleteAdapter);
        categoryName.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int pos,
                                    long id) {
                ProductCategory item = (ProductCategory) parent.getItemAtPosition(pos);
                //Po to aby kursor był na końcu zmienionego tekstu:
                categoryName.setText("");
                categoryName.append(item.getName());
            }
        });

        builder.setView(view)
                .setPositiveButton(R.string.ok_button, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        listener.onProductCategoryDialogOk(categoryName.getText().toString());
                    }
                });
        return builder.create();
    }

    @Override
    public void onStop() {
        super.onStop();
        realm.close();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public interface ProductCategoryDialogListener {
        void onProductCategoryDialogOk(String categoryName);
    }
}
