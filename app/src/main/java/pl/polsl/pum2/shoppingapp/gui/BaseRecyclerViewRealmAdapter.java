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
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.RealmViewHolder;
import pl.polsl.pum2.shoppingapp.R;
import pl.polsl.pum2.shoppingapp.database.RealmObjectWithName;


public class BaseRecyclerViewRealmAdapter<T extends RealmObject & RealmObjectWithName> extends RealmBasedRecyclerViewAdapter<T, BaseRecyclerViewRealmAdapter<T>.ViewHolder> {

    Context context;
    OnItemClickListener itemClickListener;

    BaseRecyclerViewRealmAdapter(Context context, RealmResults<T> realmResults, boolean automaticUpdate, boolean animateIdType) {
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
        T item = realmResults.get(position);
        holder.itemName.setText(item.getName());
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface OnItemClickListener {
        void onListItemClick(int position);

        boolean onListItemLongClick(int position);
    }

    public class ViewHolder extends RealmViewHolder implements View.OnClickListener, PopupMenu.OnMenuItemClickListener, View.OnLongClickListener {

        TextView itemName;
        ImageButton itemMenuButton;
        CardView listItem;

        public ViewHolder(View itemView) {
            super(itemView);
            itemName = (TextView) itemView.findViewById(R.id.item_name);
            itemMenuButton = (ImageButton) itemView.findViewById(R.id.item_menu);
            listItem = (CardView) itemView.findViewById(R.id.list_item);
            itemMenuButton.setOnClickListener(this);
            listItem.setOnClickListener(this);
            listItem.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int viewId = v.getId();
            int position = getAdapterPosition();
            switch (viewId) {
                case R.id.list_item:
                    if (itemClickListener != null) {
                        itemClickListener.onListItemClick(position);
                    }
                    break;
                case R.id.item_menu:
                    showPopupMenu(v);
                    break;
            }
        }

        @Override
        public boolean onLongClick(View view) {
            view.setSelected(true);
            return itemClickListener.onListItemLongClick(getAdapterPosition());
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

