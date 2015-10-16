package pl.polsl.pum2.shoppingapp.gui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.List;

import pl.polsl.pum2.shoppingapp.R;
import pl.polsl.pum2.shoppingapp.model.ShoppingListItemData;


public class NewItemsListAdapter extends RecyclerView.Adapter<NewItemsListAdapter.ViewHolder>{

    List<ShoppingListItemData> dataSource;

    NewItemsListAdapter(List<ShoppingListItemData> dataSource){
        this.dataSource = dataSource;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        EditText productName;
        Spinner productCategory;
        EditText price;

        public ViewHolder(View itemView) {
            super(itemView);
            productName = (EditText)itemView.findViewById(R.id.product_name);
            productCategory = (Spinner)itemView.findViewById(R.id.product_category);
            price = (EditText)itemView.findViewById(R.id.price);
        }
    }

    @Override
    public NewItemsListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View shoppingListView = inflater.inflate(R.layout.content_add_new_item, parent, false);
        return new ViewHolder(shoppingListView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.productName.setText(dataSource.get(position).getProductName());
        //TODO holder.productCategory
        if (dataSource.get(position).getPriceString().equals("0.0")) { //do poprawki
            holder.price.setText("");
        } else {
            holder.price.setText(dataSource.get(position).getPriceString());
        }
    }

    @Override
    public int getItemCount() {
        return dataSource.size();
    }
}
