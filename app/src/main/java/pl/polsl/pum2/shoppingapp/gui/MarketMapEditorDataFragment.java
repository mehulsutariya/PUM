package pl.polsl.pum2.shoppingapp.gui;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import java.util.List;

import pl.polsl.pum2.shoppingapp.database.ProductCategory;

public class MarketMapEditorDataFragment extends Fragment {
    private List<ProductCategory> oldProductCategories;

    public MarketMapEditorDataFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public List<ProductCategory> getOldProductCategories() {
        return oldProductCategories;
    }

    public void setOldProductCategories(List<ProductCategory> oldProductCategories) {
        this.oldProductCategories = oldProductCategories;
    }

}
