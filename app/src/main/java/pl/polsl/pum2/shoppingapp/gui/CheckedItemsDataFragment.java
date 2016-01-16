package pl.polsl.pum2.shoppingapp.gui;


import android.os.Bundle;
import android.support.v4.app.Fragment;

import java.util.Set;


public class CheckedItemsDataFragment extends Fragment {

    private Set<Integer> checkedItemsPositions;


    public CheckedItemsDataFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public Set<Integer> getCheckedItemsPositions() {
        return checkedItemsPositions;
    }

    public void setCheckedItemsPositions(Set<Integer> checkedItemsPositions) {
        this.checkedItemsPositions = checkedItemsPositions;
    }


}
