package pl.polsl.pum2.shoppingapp.gui;

import android.content.Context;
import android.content.Intent;
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

import io.realm.RealmResults;
import pl.polsl.pum2.shoppingapp.R;
import pl.polsl.pum2.shoppingapp.database.Product;
import pl.polsl.pum2.shoppingapp.database.ProductCategory;
import pl.polsl.pum2.shoppingapp.database.ShoppingListItem;


public class ListEditorAdapter extends RecyclerView.Adapter<ListEditorAdapter.ViewHolder> {

    private List<ShoppingListItem> dataSource;
    private Context context;
    private RealmResults<Product> products;
    private RealmResults<ProductCategory> productCategories;
    private String marketMapName;

    ListEditorAdapter(Context context, List<ShoppingListItem> dataSource, RealmResults<Product> products, RealmResults<ProductCategory> productCategories, String marketMapName) {
        this.context = context;
        this.dataSource = dataSource;
        this.products = products;
        this.productCategories = productCategories;
        this.marketMapName = marketMapName;
    }

    @Override
    public ListEditorAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View shoppingListView = inflater.inflate(R.layout.content_list_item_editor, parent, false);
        return new ViewHolder(shoppingListView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        ShoppingListItem item = dataSource.get(position);
        Product product = item.getProduct();
        if (product != null) {
            holder.productName.setText(product.getName());
        } else {
            holder.productName.setText("");
        }
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        if (numberFormat instanceof DecimalFormat) {
            ((DecimalFormat) numberFormat).setDecimalSeparatorAlwaysShown(false);
        }
        if (item.getPrice() == 0.0) {
            holder.price.setText("");
        } else {
            holder.price.setText(numberFormat.format(item.getPrice()));
        }
        if (item.getQuantity() == 0) {
            holder.quantity.setText("");
        } else {
            holder.quantity.setText(numberFormat.format(item.getQuantity()));
        }
        if (productCategories != null) {
            holder.productCategory.setSelection(item.getCategoryIndex());
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
        ImageButton addCategoryButton;

        public ViewHolder(View itemView) {
            super(itemView);
            productName = (AutoCompleteTextView) itemView.findViewById(R.id.item_name);
            productCategory = (Spinner) itemView.findViewById(R.id.product_category);
            price = (EditText) itemView.findViewById(R.id.price);
            quantity = (EditText) itemView.findViewById(R.id.quantity);
            deleteItemButton = (ImageButton) itemView.findViewById(R.id.delete_item_button);
            addCategoryButton = (ImageButton) itemView.findViewById(R.id.add_category_button);
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

            productName.setAdapter(new AutocompleteAdapter<>(context, products));
            productName.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View arg1, int pos,
                                        long id) {
                    Product item = (Product) parent.getItemAtPosition(pos);
                    //Po to aby kursor był na końcu zmienionego tekstu:
                    productName.setText("");
                    productName.append(item.getName());
                }
            });
            if (productCategories != null) {
                RealmSpinnerAdapter<ProductCategory> spinnerAdapter = new RealmSpinnerAdapter<>(context, productCategories, true);
                productCategory.setAdapter(spinnerAdapter);
                productCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        ShoppingListItem item = dataSource.get(getAdapterPosition());
                        item.setCategory((ProductCategory) parent.getAdapter().getItem(position));
                        item.setCategoryIndex(position);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                addCategoryButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, MarketMapEditorActivity.class);
                        intent.putExtra(MarketMapEditorActivity.MAP_NAME, marketMapName);
                        context.startActivity(intent);
                    }
                });
            } else {
                productCategory.setVisibility(View.GONE);
                addCategoryButton.setVisibility(View.GONE);
            }
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
            try {
                switch (type) {
                    case PRODUCT_NAME:
                        Product product = item.getProduct();
                        if (product == null) {
                            product = new Product();
                        }
                        product.setName(s.toString().trim());
                        item.setProduct(product);
                        break;
                    case PRICE:
                        item.setPrice(Double.parseDouble(s.toString()));
                        break;
                    case QUANTITY:
                        item.setQuantity(Double.parseDouble(s.toString()));
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
