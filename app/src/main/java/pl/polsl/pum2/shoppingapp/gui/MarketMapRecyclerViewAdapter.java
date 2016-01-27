package pl.polsl.pum2.shoppingapp.gui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.util.List;

import io.realm.Realm;
import pl.polsl.pum2.shoppingapp.R;
import pl.polsl.pum2.shoppingapp.database.ProductCategory;


public class MarketMapRecyclerViewAdapter extends RecyclerView.Adapter<MarketMapRecyclerViewAdapter.ViewHolder> {

    private List<ProductCategory> dataSource;
    private Context context;

    public MarketMapRecyclerViewAdapter(Context context, List<ProductCategory> dataSource) {
        this.context = context;
        this.dataSource = dataSource;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View shoppingListView = inflater.inflate(R.layout.content_market_map, parent, false);
        return new ViewHolder(shoppingListView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        ProductCategory item = dataSource.get(position);
        holder.categoryName.setText(item.getName());
    }

    @Override
    public int getItemCount() {
        return dataSource.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {
        TextView categoryName;
        ImageButton menuButton;

        public ViewHolder(View itemView) {
            super(itemView);
            categoryName = (TextView) itemView.findViewById(R.id.category_name);
            menuButton = (ImageButton) itemView.findViewById(R.id.item_menu);
            menuButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.item_menu) {
                showPopupMenu(v);
            }
        }

        private void showPopupMenu(View v) {
            PopupMenu popup = new PopupMenu(context, v);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.menu_market_map_item, popup.getMenu());
            popup.setOnMenuItemClickListener(this);
            popup.show();
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.delete:
                    Realm realm = Realm.getDefaultInstance();
                    int position = getAdapterPosition();
                    realm.beginTransaction();
                    dataSource.remove(position);
                    realm.commitTransaction();
                    realm.close();
                    notifyItemRemoved(position);
                    break;
            }
            return false;
        }
    }
}
