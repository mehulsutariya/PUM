package pl.polsl.pum2.shoppingapp.gui;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ViewFlipper;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import io.realm.Realm;
import io.realm.RealmBasedRecyclerViewAdapter;
import io.realm.RealmResults;
import io.realm.RealmViewHolder;
import io.realm.exceptions.RealmPrimaryKeyConstraintException;
import pl.polsl.pum2.shoppingapp.R;
import pl.polsl.pum2.shoppingapp.database.Product;
import pl.polsl.pum2.shoppingapp.database.ShoppingListItem;


class ShoppingListAdapter extends RealmBasedRecyclerViewAdapter<ShoppingListItem, ShoppingListAdapter.ViewHolder> {

    public static int EDIT_SUCCEEDED = 0;
    public static int EDIT_FAILED = 1;
    OnItemClickListener itemClickListener;
    private Context context;
    private int listType;
    private Realm realm;

    public ShoppingListAdapter(Context context, Realm realm, RealmResults<ShoppingListItem> realmResults, boolean automaticUpdate, boolean animateIdType, int listType) {
        super(context, realmResults, automaticUpdate, animateIdType);
        this.context = context;
        this.realm = realm;
        this.listType = listType;
    }

    @Override
    public ShoppingListAdapter.ViewHolder onCreateRealmViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View shoppingListView = inflater.inflate(R.layout.content_shopping_list, parent, false);
        return new ViewHolder(shoppingListView, itemClickListener);
    }

    @Override
    public void onBindRealmViewHolder(ViewHolder holder, int position) {
        holder.productName.setText(realmResults.get(position).getProduct().getName());
        holder.productNameEdit.setText(realmResults.get(position).getProduct().getName());
        Double price = realmResults.get(position).getPrice();
        String priceText;
        NumberFormat numberFormat;
        if (price > 0) {
            numberFormat = NumberFormat.getCurrencyInstance();
            priceText = numberFormat.format(price);
        } else {
            priceText = context.getString(R.string.no_price);
        }
        holder.price.setText(priceText);

        if (price > 0) {
            priceText = price.toString();
        } else {
            priceText = "";
        }
        holder.priceEdit.setText(priceText);
        Double quantity = realmResults.get(position).getQuantity();
        numberFormat = NumberFormat.getNumberInstance();
        if (numberFormat instanceof DecimalFormat) {
            ((DecimalFormat) numberFormat).setDecimalSeparatorAlwaysShown(false);
        }
        holder.quantity.setText(String.format(context.getString(R.string.quantity_string), numberFormat.format(quantity)));
        holder.quantityEdit.setText(quantity.toString());
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public String getItemName(int position) {
        return realmResults.get(position).getProduct().getName();
    }


    public interface OnItemClickListener {
        void onDeleteButton(int position);

        void onEditButton(int position);

        void onDoneButton(int position, int editResult);

        void onCancelButton(int position);

        void onBuyButton(int position, int listType);
    }

    public class ViewHolder extends RealmViewHolder implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {

        ViewFlipper viewFlipper;
        AutoCompleteTextView productNameEdit;
        EditText priceEdit;
        ImageButton buyButton;
        ImageButton itemMenuButton;
        Button doneButton;
        Button cancelButton;
        ImageButton clearProductNameButton;
        ImageButton clearPriceButton;
        ImageButton clearQuantityButton;
        Spinner productCategory;
        TextView productName;
        TextView price;
        OnItemClickListener itemClickListener;
        TextView quantity;
        EditText quantityEdit;

        public ViewHolder(View itemView, OnItemClickListener itemClickListener) {
            super(itemView);
            this.itemClickListener = itemClickListener;
            viewFlipper = (ViewFlipper) itemView.findViewById(R.id.view_flipper);
            productName = (TextView) itemView.findViewById(R.id.item_name);
            productNameEdit = (AutoCompleteTextView) itemView.findViewById(R.id.product_name_edit);
            price = (TextView) itemView.findViewById(R.id.price);
            priceEdit = (EditText) itemView.findViewById(R.id.price_edit);
            quantity = (TextView) itemView.findViewById(R.id.quantity);
            quantityEdit = (EditText) itemView.findViewById(R.id.quantity_edit);
            buyButton = (ImageButton) itemView.findViewById(R.id.buy);
            itemMenuButton = (ImageButton) itemView.findViewById(R.id.item_menu);
            doneButton = (Button) itemView.findViewById(R.id.done_button);
            cancelButton = (Button) itemView.findViewById(R.id.cancel_button);
            clearProductNameButton = (ImageButton) itemView.findViewById(R.id.clear_product_name);
            clearPriceButton = (ImageButton) itemView.findViewById(R.id.clear_price);
            clearPriceButton = (ImageButton) itemView.findViewById(R.id.clear_price);
            clearQuantityButton = (ImageButton) itemView.findViewById(R.id.clear_quantity);
            productCategory = (Spinner) itemView.findViewById(R.id.product_category);

            viewFlipper.setMeasureAllChildren(false);

            itemMenuButton.setOnClickListener(this);
            doneButton.setOnClickListener(this);
            clearProductNameButton.setOnClickListener(this);
            clearPriceButton.setOnClickListener(this);
            clearQuantityButton.setOnClickListener(this);
            cancelButton.setOnClickListener(this);
            buyButton.setOnClickListener(this);
            if (listType == ShoppingListFragment.CART) {
                buyButton.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_list_white_24dp));
            }

            RealmResults<Product> realmResults = realm.where(Product.class).findAll();
            productNameEdit.setAdapter(new AutocompleteAdapter(context, realmResults));
            productNameEdit.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View arg1, int pos,
                                        long id) {
                    Product item = (Product) parent.getItemAtPosition(pos);
                    productNameEdit.setText("");
                    productNameEdit.append(item.getName());
                }
            });
        }

        @Override
        public void onClick(View v) {
            int viewId = v.getId();
            int position = getAdapterPosition();
            switch (viewId) {
                case R.id.item_menu:
                    showPopupMenu(v);
                    break;
                case R.id.done_button:
                    try {
                        insertEditedItemData(position);
                        viewFlipper.showPrevious();
                        itemClickListener.onDoneButton(position, EDIT_SUCCEEDED);
                    } catch (InvalidItemValuesException e) {
                        itemClickListener.onDoneButton(position, EDIT_FAILED);
                    }
                    break;
                case R.id.cancel_button:
                    itemClickListener.onCancelButton(position);
                    viewFlipper.showPrevious();
                    cancelEditChanges(position);
                    break;
                case R.id.clear_product_name:
                    productNameEdit.setText("");
                    break;
                case R.id.clear_price:
                    priceEdit.setText("");
                    break;
                case R.id.clear_quantity:
                    quantityEdit.setText("");
                    break;
                case R.id.buy:
                    itemClickListener.onBuyButton(position, listType);
                    break;
            }
        }

        private void showPopupMenu(View v) {
            PopupMenu popup = new PopupMenu(context, v);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.menu_shopping_list_item, popup.getMenu());
            popup.setOnMenuItemClickListener(this);
            popup.show();
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.edit:
                    itemClickListener.onEditButton(getAdapterPosition());
                    viewFlipper.showNext();
                    break;
                case R.id.delete:
                    itemClickListener.onDeleteButton(getAdapterPosition());
                    break;
            }
            return false;
        }

        private void insertEditedItemData(int position) throws InvalidItemValuesException {
            if (editedValuesAreValid()) {
                ShoppingListItem item = realmResults.get(getAdapterPosition());
                realm.beginTransaction();
                Product newProductInRealm = null;
                try {
                    Product newProduct = new Product();
                    newProduct.setName(productNameEdit.getText().toString().trim());
                    newProductInRealm = realm.copyToRealm(newProduct);
                    realm.commitTransaction();
                } catch (RealmPrimaryKeyConstraintException e) {
                    realm.cancelTransaction();
                }
                realm.beginTransaction();

                if (newProductInRealm != null) {
                    item.setProduct(newProductInRealm);
                } else {
                    Product existingProduct = realm.where(Product.class).equalTo("name", productNameEdit.getText().toString().trim()).findFirst();
                    item.setProduct(existingProduct);
                }
                Double price = Double.parseDouble(priceEdit.getText().toString());
                item.setPrice(price);
                item.setQuantity(Double.parseDouble(quantityEdit.getText().toString()));
                realm.commitTransaction();
                notifyItemChanged(position);
            } else {
                throw new InvalidItemValuesException();
            }
        }

        private void cancelEditChanges(int position) {
            ShoppingListItem item = realmResults.get(position);
            productNameEdit.setText(item.getProduct().getName());
            priceEdit.setText(Double.toString(item.getPrice()));
            quantityEdit.setText(Double.toString(item.getQuantity()));
        }

        private boolean editedValuesAreValid() {
            String productName = productNameEdit.getText().toString().trim();
            String quantity = quantityEdit.getText().toString().trim();
            return (productName.length() != 0 && quantity.length() != 0);
        }

    }

    private class InvalidItemValuesException extends Exception {
    }

}


