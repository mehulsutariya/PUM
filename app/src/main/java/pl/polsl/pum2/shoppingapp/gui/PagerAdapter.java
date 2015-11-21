package pl.polsl.pum2.shoppingapp.gui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

public class PagerAdapter extends FragmentPagerAdapter {
    private int numOfTabs;
    private Fragment currentFragment;

    public PagerAdapter(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return getFragment(ShoppingListActivity.SHOPPING_LIST);
            case 1:
                return getFragment(ShoppingListActivity.CART);
            default:
                return null;
        }
    }

    @NonNull
    private Fragment getFragment(int type) {
        Bundle arguments = new Bundle();
        ShoppingListFragment fragment = new ShoppingListFragment();
        arguments.putInt("ListType", type);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
        currentFragment = (Fragment) object;
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }

    public Fragment getCurrentFragment() {
        return currentFragment;
    }
}