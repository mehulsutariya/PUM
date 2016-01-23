package pl.polsl.pum2.shoppingapp.gui;


import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import io.realm.RealmObject;
import io.realm.RealmResults;
import pl.polsl.pum2.shoppingapp.R;
import pl.polsl.pum2.shoppingapp.database.CheckableRealmObjectWithName;


public class AutocompleteAdapter<T extends RealmObject & CheckableRealmObjectWithName> extends FilterableRealmBaseAdapter<T> {

    public AutocompleteAdapter(Context context, RealmResults<T> realmResults) {
        super(context, R.layout.drop_down_list_layout, realmResults);
    }


    @Override
    public List<T> performRealmFiltering(@NonNull CharSequence constraint, RealmResults<T> results) {
        return results.where().contains("name", constraint.toString()).findAll();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.drop_down_list_layout, null);
        } else {
            view = convertView;
        }
        TextView itemName = (TextView) view.findViewById(R.id.item_name);
        T item = getItem(position);
        itemName.setText(item.getName());
        return view;
    }

}
