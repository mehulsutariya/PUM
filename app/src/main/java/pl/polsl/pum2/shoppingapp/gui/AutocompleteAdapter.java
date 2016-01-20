package pl.polsl.pum2.shoppingapp.gui;


import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import io.realm.RealmObject;
import io.realm.RealmResults;
import pl.polsl.pum2.shoppingapp.database.CheckableRealmObjectWithName;
import pl.polsl.pum2.shoppingapp.database.Product;


public class AutocompleteAdapter<T extends RealmObject & CheckableRealmObjectWithName> extends FilterableRealmBaseAdapter<T> {

    public AutocompleteAdapter(Context context, RealmResults<T> realmResults) {
        super(context, android.R.layout.simple_list_item_1, realmResults);
    }


    @Override
    public List<T> performRealmFiltering(@NonNull CharSequence constraint, RealmResults<T> results) {
        return results.where().contains("name", constraint.toString()).findAll();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView = new TextView(getContext());
        textView.setText(getItem(position).getName());
        textView.setTextSize(23);
        return textView;
    }

}
