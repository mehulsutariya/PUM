package pl.polsl.pum2.shoppingapp.gui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import io.realm.RealmBasedRecyclerViewAdapter;
import io.realm.RealmResults;
import io.realm.RealmViewHolder;
import pl.polsl.pum2.shoppingapp.R;
import pl.polsl.pum2.shoppingapp.database.Product;

public class ProductsAdapter extends RealmBasedRecyclerViewAdapter<Product, ProductsAdapter.ViewHolder> {

    public ProductsAdapter(Context context, RealmResults<Product> realmResults, boolean automaticUpdate, boolean animateIdType) {
        super(context, realmResults, automaticUpdate, animateIdType);
    }

    @Override
    public ViewHolder onCreateRealmViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View shoppingListView = inflater.inflate(R.layout.content_main_activity_list, parent, false);
        return new ViewHolder(shoppingListView);
    }

    @Override
    public void onBindRealmViewHolder(ViewHolder holder, int position) {
        Product product = realmResults.get(position);
        holder.productName.setText(product.getName());
    }

    public class ViewHolder extends RealmViewHolder {

        TextView productName;

        public ViewHolder(View itemView) {
            super(itemView);
            productName = (TextView) itemView.findViewById(R.id.item_name);
        }
    }
}
