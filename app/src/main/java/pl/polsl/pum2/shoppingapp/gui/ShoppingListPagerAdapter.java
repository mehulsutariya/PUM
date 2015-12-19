package pl.polsl.pum2.shoppingapp.gui;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

public class ShoppingListPagerAdapter extends FragmentPagerAdapter {
    private int numOfTabs;
    private String listName;

    public ShoppingListPagerAdapter(FragmentManager fm, int numOfTabs, String listName) {
        super(fm);
        this.numOfTabs = numOfTabs;
        this.listName = listName;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return getFragment(ShoppingListFragment.SHOPPING_LIST);
            case 1:
                return getFragment(ShoppingListFragment.CART);
            default:
                return null;
        }
    }

    @NonNull
    private Fragment getFragment(int type) {
        ShoppingListFragment fragment = ShoppingListFragment.newInstance(listName, type);
        return fragment;
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }

}