package pl.polsl.pum2.shoppingapp.gui;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.ViewFlipper;

import io.realm.Realm;
import io.realm.RealmBasedRecyclerViewAdapter;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.RealmViewHolder;
import io.realm.exceptions.RealmPrimaryKeyConstraintException;
import pl.polsl.pum2.shoppingapp.R;
import pl.polsl.pum2.shoppingapp.database.CheckableRealmObjectWithName;


public class BaseRecyclerViewRealmAdapter<T extends RealmObject & CheckableRealmObjectWithName> extends RealmBasedRecyclerViewAdapter<T, BaseRecyclerViewRealmAdapter<T>.ViewHolder> {

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
        holder.itemNameEdit.setText(item.getName());
        if (item.isChecked()) {
            holder.checkBox.setChecked(true);
        } else {
            holder.checkBox.setChecked(false);
        }
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }


    public interface OnItemClickListener {
        void onListItemClick(int position);

        boolean onListItemLongClick(int position);

        void onDelete(int position);

        void onItemEdit();

        void onItemEditFailed(int position);

        void onItemChecked(int position);

        void onItemUnchecked(int position);
    }

    public class ViewHolder extends RealmViewHolder implements View.OnClickListener, PopupMenu.OnMenuItemClickListener, View.OnLongClickListener, CompoundButton.OnCheckedChangeListener {

        TextView itemName;
        ImageButton itemMenuButton;
        CardView listItem;
        EditText itemNameEdit;
        Button doneButton;
        Button cancelButton;
        ImageButton clearButton;
        ViewFlipper viewFlipper;
        CheckBox checkBox;


        public ViewHolder(View itemView) {
            super(itemView);
            itemName = (TextView) itemView.findViewById(R.id.item_name);
            itemMenuButton = (ImageButton) itemView.findViewById(R.id.item_menu);
            listItem = (CardView) itemView.findViewById(R.id.list_item);
            itemNameEdit = (EditText) itemView.findViewById(R.id.item_name_edit);
            doneButton = (Button) itemView.findViewById(R.id.done_button);
            cancelButton = (Button) itemView.findViewById(R.id.cancel_button);
            clearButton = (ImageButton) itemView.findViewById(R.id.clear_item_name);
            viewFlipper = (ViewFlipper) itemView.findViewById(R.id.view_flipper);
            checkBox = (CheckBox) itemView.findViewById(R.id.checkbox);
            itemMenuButton.setOnClickListener(this);
            listItem.setOnClickListener(this);
            listItem.setOnLongClickListener(this);
            doneButton.setOnClickListener(this);
            cancelButton.setOnClickListener(this);
            clearButton.setOnClickListener(this);
            viewFlipper.setMeasureAllChildren(false);
            checkBox.setOnCheckedChangeListener(this);
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
                case R.id.done_button:
                    insertEditedItemData(position);
                    break;
                case R.id.cancel_button:
                    exitEditMode();
                    cancelEditChanges(position);
                    break;
                case R.id.clear_item_name:
                    itemNameEdit.setText("");
            }
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                itemMenuButton.setEnabled(false);
                listItem.setEnabled(false);
                itemClickListener.onItemChecked(getAdapterPosition());
            } else {
                itemMenuButton.setEnabled(true);
                listItem.setEnabled(true);
                itemClickListener.onItemUnchecked(getAdapterPosition());
            }
        }

        private void exitEditMode() {
            viewFlipper.showPrevious();
            listItem.setOnClickListener(this);
            listItem.setOnLongClickListener(this);
        }

        private void insertEditedItemData(int position) {
            if (editedValuesAreValid()) {
                T item = realmResults.get(getAdapterPosition());
                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                try {
                    item.setName(itemNameEdit.getText().toString().trim());
                    realm.commitTransaction();
                    notifyItemChanged(position);
                    exitEditMode();
                } catch (RealmPrimaryKeyConstraintException e) {
                    realm.cancelTransaction();
                    itemClickListener.onItemEditFailed(position);
                }
                realm.close();
            }
        }

        private void cancelEditChanges(int position) {
            T item = realmResults.get(position);
            itemNameEdit.setText(item.getName());
        }

        private boolean editedValuesAreValid() {
            String productName = itemNameEdit.getText().toString().trim();
            return (productName.length() != 0);
        }

        @Override
        public boolean onLongClick(View view) {
            view.setSelected(true);
            if (itemClickListener != null) {
                return itemClickListener.onListItemLongClick(getAdapterPosition());
            }
            return false;
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
                    enterEditMode();
                    break;
                case R.id.delete:
                    if (itemClickListener != null) {
                        itemClickListener.onDelete(getAdapterPosition());
                    }
                    break;
            }
            return false;
        }

        private void enterEditMode() {
            viewFlipper.showNext();
            listItem.setOnClickListener(null);
            listItem.setOnLongClickListener(null);
            itemClickListener.onItemEdit();
        }
    }
}

