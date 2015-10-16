package pl.polsl.pum2.shoppingapp.gui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

import pl.polsl.pum2.shoppingapp.R;
import pl.polsl.pum2.shoppingapp.model.ShoppingListItemData;


class ShoppingListAdapter extends RecyclerView.Adapter<ShoppingListAdapter.ViewHolder> {

    List<ShoppingListItemData> dataSource;

    public interface OnItemClickListener {
        void onDeleteButton(View view, int position);
        void onEditButton(View view, int position);
        void onDoneButton(View view, int position);
    }
    OnItemClickListener itemClickListener;

    public ShoppingListAdapter(List<ShoppingListItemData> dataSource) {
        this.dataSource = dataSource;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //CardView mShoppingListItem;
        AutoCompleteTextView productNameEdit;
        EditText priceEdit;
        CheckBox buyCheckBox;
        ImageButton editButton;
        ImageButton deleteButton;
        Button doneButton;
        ImageButton clearProductNameButton;
        ImageButton clearPriceButton;
        Spinner productCategory;
        TextView productName;
        TextView price;
        OnItemClickListener itemClickListener;


        public ViewHolder(View itemView, OnItemClickListener itemClickListener) {
            super(itemView);
            this.itemClickListener = itemClickListener;
            //mShoppingListItem = (CardView)itemView.findViewById(R.id.shopping_list_item);
            productName = (TextView)itemView.findViewById(R.id.product_name);
            productNameEdit = (AutoCompleteTextView)itemView.findViewById(R.id.product_name_edit);
            price = (TextView)itemView.findViewById(R.id.price);
            priceEdit = (EditText)itemView.findViewById(R.id.price_edit);
            buyCheckBox = (CheckBox)itemView.findViewById(R.id.buyCheckBox);
            editButton = (ImageButton)itemView.findViewById(R.id.edit_button);
            deleteButton = (ImageButton)itemView.findViewById(R.id.delete_button);
            doneButton = (Button)itemView.findViewById(R.id.done_button);
            clearProductNameButton = (ImageButton)itemView.findViewById(R.id.clear_product_name);
            clearPriceButton = (ImageButton)itemView.findViewById(R.id.clear_price);
            productCategory = (Spinner)itemView.findViewById(R.id.product_category);

            editButton.setOnClickListener(this);
            doneButton.setOnClickListener(this);
            deleteButton.setOnClickListener(this);
            clearProductNameButton.setOnClickListener(this);
            clearPriceButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int viewId = v.getId();
            int position = getAdapterPosition();
            switch (viewId) {
                case R.id.edit_button:
                    itemClickListener.onEditButton(v, position);
                    setEditMode(true);
                    break;
                case R.id.done_button:
                    itemClickListener.onDoneButton(v, position);
                    setEditMode(false);
                    insertEditedItemData(position);
                    break;
                case R.id.delete_button:
                    itemClickListener.onDeleteButton(v, position);
                    break;
                case R.id.clear_product_name:
                    productNameEdit.setText("");
                    break;
                case R.id.clear_price:
                    priceEdit.setText("");
                    break;
            }
        }

        private void setEditMode(boolean isEditMode){
            int normalVisibility;
            int editVisibility;
            if (isEditMode) {
                normalVisibility = View.GONE;
                editVisibility = View.VISIBLE;
            } else  {
                normalVisibility = View.VISIBLE;
                editVisibility = View.GONE;
            }
            productName.setVisibility(normalVisibility);
            productNameEdit.setVisibility(editVisibility);
            price.setVisibility(normalVisibility);
            priceEdit.setVisibility(editVisibility);
            editButton.setVisibility(normalVisibility);
            deleteButton.setVisibility(normalVisibility);
            doneButton.setVisibility(editVisibility);
            buyCheckBox.setVisibility(normalVisibility);
            clearProductNameButton.setVisibility(editVisibility);
            clearPriceButton.setVisibility(editVisibility);
            productCategory.setVisibility(editVisibility);
        }

        private void insertEditedItemData(int position) {
            ShoppingListItemData item;
            item = dataSource.get(getAdapterPosition());
            item.setProductName(productNameEdit.getText().toString());
            if (priceEdit.getText().length() > 0) {
                item.setPriceValue(Double.parseDouble(priceEdit.getText().toString()));
            } else {
                item.setPriceValue(0.0);
            }
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
        holder.price.setText(dataSource.get(position).getPriceString());
        holder.priceEdit.setText(dataSource.get(position).getPriceString());
        if (dataSource.get(position).getIsBought()) {
            holder.buyCheckBox.setChecked(true);
        } else {
            holder.buyCheckBox.setChecked(false);
        }
    }

    @Override
    public int getItemCount() {
        return dataSource.size();
    }


    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

}


