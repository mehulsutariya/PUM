package pl.polsl.pum2.shoppingapp.gui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ViewFlipper;

import java.text.NumberFormat;
import java.util.List;

import pl.polsl.pum2.shoppingapp.R;
import pl.polsl.pum2.shoppingapp.model.ShoppingListItemData;


class ShoppingListAdapter extends RecyclerView.Adapter<ShoppingListAdapter.ViewHolder> {

    private List<ShoppingListItemData> dataSource;
    private Context context;
    private int listType;

    public interface OnItemClickListener {
        void onDeleteButton(int position);

        void onEditButton(int position);

        void onDoneButton(int position);

        void onCancelButton(int position);

        void onBuyButton(int position);
    }

    OnItemClickListener itemClickListener;

    public ShoppingListAdapter(List<ShoppingListItemData> dataSource, Context context, int listType) {
        this.dataSource = dataSource;
        this.context = context;
        this.listType = listType;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {

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
            productName = (TextView) itemView.findViewById(R.id.product_name);
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
            if (listType == ShoppingListActivity.CART) {
                buyButton.setVisibility(View.GONE);
            }
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
                    itemClickListener.onDoneButton(position);
                    viewFlipper.showPrevious();
                    insertEditedItemData(position);
                    break;
                case R.id.cancel_button:
                    itemClickListener.onCancelButton(position);
                    viewFlipper.showPrevious();
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
                    itemClickListener.onBuyButton(position);
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

        private void insertEditedItemData(int position) {
            ShoppingListItemData item;
            item = dataSource.get(getAdapterPosition());
            item.setProductName(productNameEdit.getText().toString());
            if (priceEdit.getText().length() > 0) {
                item.setPrice(Double.parseDouble(priceEdit.getText().toString()));
            } else {
                item.setPrice(0.0);
            }
            item.setQuantity(Integer.parseInt(quantityEdit.getText().toString()));
            dataSource.set(position, item);
            notifyItemChanged(position);
        }
    }

    @Override
    public ShoppingListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View shoppingListView = inflater.inflate(R.layout.content_shopping_list, parent, false);
        return new ViewHolder(shoppingListView, itemClickListener);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.productName.setText(dataSource.get(position).getProductName());
        holder.productNameEdit.setText(dataSource.get(position).getProductName());
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
        String priceText = numberFormat.format(dataSource.get(position).getPrice());
        holder.price.setText(priceText);
        holder.priceEdit.setText(dataSource.get(position).getPriceString());
        holder.quantity.setText(String.format(context.getString(R.string.quantity_string), dataSource.get(position).getQuantity()));
        holder.quantityEdit.setText(String.format("%d", dataSource.get(position).getQuantity()));
    }

    @Override
    public int getItemCount() {
        return dataSource.size();
    }


    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public String getItemName(int position) {
        return dataSource.get(position).getProductName();
    }

}


