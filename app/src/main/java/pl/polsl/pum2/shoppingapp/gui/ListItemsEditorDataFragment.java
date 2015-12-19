package pl.polsl.pum2.shoppingapp.gui;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import java.util.List;

import pl.polsl.pum2.shoppingapp.database.ShoppingListItem;


public class ListItemsEditorDataFragment extends Fragment {

    private List<ShoppingListItem> listItems;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public List<ShoppingListItem> getData() {
        return listItems;
    }

    public void setData(List<ShoppingListItem> listItems) {
        this.listItems = listItems;
    }

}
