package pl.polsl.pum2.shoppingapp.gui;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ViewFlipper;

import io.realm.RealmList;
import pl.polsl.pum2.shoppingapp.R;
import pl.polsl.pum2.shoppingapp.database.ProductCategory;

public class MarketMapContentAdapter extends RecyclerView.Adapter<MarketMapContentAdapter.ViewHolder> {

    private RealmList<ProductCategory> productCategories;

    public MarketMapContentAdapter(RealmList<ProductCategory> productCategories) {
        this.productCategories = productCategories;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.content_main_activity_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ProductCategory item = productCategories.get(position);
        holder.itemName.setText(item.getName());
        holder.itemNameEdit.setText(item.getName());
    }

    @Override
    public int getItemCount() {
        return productCategories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

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
            viewFlipper.setMeasureAllChildren(false);
            /*
            itemMenuButton.setOnClickListener(this);
            listItem.setOnClickListener(this);
            listItem.setOnLongClickListener(this);
            doneButton.setOnClickListener(this);
            cancelButton.setOnClickListener(this);
            clearButton.setOnClickListener(this);
            viewFlipper.setMeasureAllChildren(false);
            checkBox.setOnCheckedChangeListener(this);
            */
        }
    }
}


