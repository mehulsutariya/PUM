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
import pl.polsl.pum2.shoppingapp.database.ProductCategory;

public class ProductCategoriesAdapter extends RealmBasedRecyclerViewAdapter<ProductCategory, ProductCategoriesAdapter.ViewHolder> {

    ProductCategoriesAdapter(Context context, RealmResults<ProductCategory> realmResults, boolean automaticUpdate, boolean animateIdType) {
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
        ProductCategory category = realmResults.get(position);
        holder.categoryName.setText(category.getName());
    }

    public class ViewHolder extends RealmViewHolder {

        TextView categoryName;

        public ViewHolder(View itemView) {
            super(itemView);
            categoryName = (TextView) itemView.findViewById(R.id.category_name);
        }
    }
}
