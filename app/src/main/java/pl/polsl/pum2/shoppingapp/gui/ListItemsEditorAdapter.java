package pl.polsl.pum2.shoppingapp.gui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import pl.polsl.pum2.shoppingapp.R;
import pl.polsl.pum2.shoppingapp.database.Product;
import pl.polsl.pum2.shoppingapp.database.ShoppingListItem;
import pl.polsl.pum2.shoppingapp.helpers.DoubleParser;


public class ListItemsEditorAdapter extends RecyclerView.Adapter<ListItemsEditorAdapter.ViewHolder> {

    private List<ShoppingListItem> dataSource;
    private Realm realm;
    private Context context;

    ListItemsEditorAdapter(Context context, Realm realm, List<ShoppingListItem> dataSource) {
        this.realm = realm;
        this.context = context;
        this.dataSource = dataSource;
    }

    @Override
    public ListItemsEditorAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View shoppingListView = inflater.inflate(R.layout.content_list_item_editor, parent, false);
        return new ViewHolder(shoppingListView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Product product = dataSource.get(position).getProduct();
        if (product != null) {
            holder.productName.setText(product.getName());
        }
        //TODO holder.productCategory
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        if (numberFormat instanceof DecimalFormat) {
            ((DecimalFormat) numberFormat).setDecimalSeparatorAlwaysShown(false);
        }
        if (dataSource.get(position).getPrice() == 0.0) {
            holder.price.setText("");
        } else {
            holder.price.setText(numberFormat.format(dataSource.get(position).getPrice()));
        }
        if (dataSource.get(position).getQuantity() == 0) {
            holder.quantity.setText("");
        } else {
            holder.quantity.setText(numberFormat.format(dataSource.get(position).getQuantity()));
        }

    }

    @Override
    public int getItemCount() {
        return dataSource.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        AutoCompleteTextView productName;
        Spinner productCategory;
        EditText price;
        EditText quantity;
        ImageButton deleteItemButton;

        public ViewHolder(View itemView) {
            super(itemView);
            productName = (AutoCompleteTextView) itemView.findViewById(R.id.item_name);
            productCategory = (Spinner) itemView.findViewById(R.id.product_category);
            price = (EditText) itemView.findViewById(R.id.price);
            quantity = (EditText) itemView.findViewById(R.id.quantity);
            deleteItemButton = (ImageButton) itemView.findViewById(R.id.delete_item_button);
            deleteItemButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    dataSource.remove(position);
                    notifyItemRemoved(position);
                }
            });
            productName.addTextChangedListener(new TextListener(TextListener.PRODUCT_NAME, this));
            price.addTextChangedListener(new TextListener(TextListener.PRICE, this));
            quantity.addTextChangedListener(new TextListener(TextListener.QUANTITY, this));

            RealmResults<Product> realmResults = realm.where(Product.class).findAll();
            productName.setAdapter(new AutocompleteAdapter(context, realmResults));
            productName.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View arg1, int pos,
                                        long id) {
                    Product item = (Product) parent.getItemAtPosition(pos);
                    productName.setText("");
                    productName.append(item.getName());
                }
            });
        }
    }

    class TextListener implements TextWatcher {

        final static int PRODUCT_NAME = 0;
        final static int PRICE = 1;
        final static int QUANTITY = 2;


        RecyclerView.ViewHolder viewHolder;
        int type;


        public TextListener(int type, RecyclerView.ViewHolder viewHolder) {
            this.type = type;
            this.viewHolder = viewHolder;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            ShoppingListItem item = dataSource.get(viewHolder.getAdapterPosition());
            Realm realm = Realm.getDefaultInstance();
            try {
                switch (type) {
                    case PRODUCT_NAME:
                        Product product = item.getProduct();
                        if (product == null) {
                            product = new Product();
                        }
                        realm.beginTransaction();
                        product.setName(s.toString().trim());
                        realm.commitTransaction();
                        item.setProduct(product);
                        break;
                    case PRICE:
                        item.setPrice(DoubleParser.parse(s.toString()));
                        break;
                    case QUANTITY:
                        item.setQuantity(DoubleParser.parse(s.toString()));
                        break;
                }
            } catch (NumberFormatException e) {
                if (type == PRICE) {
                    item.setPrice(0.0);
                } else {
                    item.setQuantity(0);
                }
            }
            dataSource.set(viewHolder.getAdapterPosition(), item);
        }
    }
}
