package pl.polsl.pum2.shoppingapp.gui;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import io.realm.RealmBasedRecyclerViewAdapter;
import io.realm.RealmResults;
import io.realm.RealmViewHolder;
import pl.polsl.pum2.shoppingapp.R;
import pl.polsl.pum2.shoppingapp.database.ShoppingList;

public class AllShoppingListsAdapter extends RealmBasedRecyclerViewAdapter<ShoppingList, AllShoppingListsAdapter.ViewHolder> {

    Context context;
    OnItemClickListener itemClickListener;

    AllShoppingListsAdapter(Context context, RealmResults<ShoppingList> realmResults, boolean automaticUpdate, boolean animateIdType) {
        super(context, realmResults, automaticUpdate, animateIdType);
        this.context = context;
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
        ShoppingList shoppingList = realmResults.get(position);
        holder.listName.setText(shoppingList.getName());
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface OnItemClickListener {
        void onListItemClick(int position);
    }

    public class ViewHolder extends RealmViewHolder implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {

        TextView listName;
        ImageButton itemMenuButton;
        CardView listItem;

        public ViewHolder(View itemView) {
            super(itemView);
            listName = (TextView) itemView.findViewById(R.id.item_name);
            itemMenuButton = (ImageButton) itemView.findViewById(R.id.item_menu);
            listItem = (CardView) itemView.findViewById(R.id.list_item);
            itemMenuButton.setOnClickListener(this);
            listItem.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int viewId = v.getId();
            int position = getAdapterPosition();
            switch (viewId) {
                case R.id.list_item:
                    itemClickListener.onListItemClick(position);
                    break;
                case R.id.item_menu:
                    showPopupMenu(v);
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
                    //TODO
                    break;
                case R.id.delete:
                    //TODO
                    break;
            }
            return false;
        }
    }
}
