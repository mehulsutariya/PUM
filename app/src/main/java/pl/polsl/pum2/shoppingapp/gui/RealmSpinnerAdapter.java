package pl.polsl.pum2.shoppingapp.gui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import io.realm.RealmBaseAdapter;
import io.realm.RealmObject;
import io.realm.RealmResults;
import pl.polsl.pum2.shoppingapp.R;
import pl.polsl.pum2.shoppingapp.database.CheckableRealmObjectWithName;

public class RealmSpinnerAdapter<T extends RealmObject & CheckableRealmObjectWithName> extends RealmBaseAdapter<T> {

    public RealmSpinnerAdapter(android.content.Context context, RealmResults<T> realmResults, boolean automaticUpdate) {
        super(context, realmResults, automaticUpdate);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View spinnerView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            spinnerView = inflater.inflate(R.layout.drop_down_list_layout, null);
        } else {
            spinnerView = convertView;
        }
        TextView itemName = (TextView) spinnerView.findViewById(R.id.item_name);
        if (position > - 1) {
            T item = getItem(position);
            itemName.setText(item.getName());
        } else {
            itemName.setText(context.getString(R.string.no_category));
        }
        return spinnerView;
    }
}
