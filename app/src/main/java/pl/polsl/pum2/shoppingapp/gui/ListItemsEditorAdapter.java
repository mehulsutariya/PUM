package pl.polsl.pum2.shoppingapp.gui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import java.util.List;

import pl.polsl.pum2.shoppingapp.R;
import pl.polsl.pum2.shoppingapp.model.ShoppingListItemData;


public class ListItemsEditorAdapter extends RecyclerView.Adapter<ListItemsEditorAdapter.ViewHolder> {

    private List<ShoppingListItemData> dataSource;

    ListItemsEditorAdapter(List<ShoppingListItemData> dataSource) {
        this.dataSource = dataSource;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        EditText productName;
        Spinner productCategory;
        EditText price;
        EditText quantity;
        ImageButton deleteItemButton;

        public ViewHolder(View itemView) {
            super(itemView);
            productName = (EditText) itemView.findViewById(R.id.product_name);
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
        }
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
        holder.productName.setText(dataSource.get(position).getProductName());
        //TODO holder.productCategory
        if (dataSource.get(position).getPrice() == 0.0) {
            holder.price.setText("");
        } else {
            holder.price.setText(String.format("%f", dataSource.get(position).getPrice()));
        }
        if (dataSource.get(position).getQuantity() == 0) {
            holder.quantity.setText("");
        } else {
            holder.quantity.setText(String.format("%d", dataSource.get(position).getQuantity()));
        }

    }

    @Override
    public int getItemCount() {
        return dataSource.size();
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
            ShoppingListItemData item = dataSource.get(viewHolder.getAdapterPosition());
            try {
                switch (type) {
                    case PRODUCT_NAME:
                        item.setProductName(s.toString());
                        break;
                    case PRICE:
                        item.setPrice(Double.parseDouble(s.toString()));
                        break;
                    case QUANTITY:
                        item.setQuantity(Integer.parseInt(s.toString()));
                        break;
                }
            } catch (NumberFormatException e) {
                if (type == PRICE) {
                    item.setPrice(0.0);
                } else {
                    item.setQuantity(0);
                }
            }
        }
    }
}
