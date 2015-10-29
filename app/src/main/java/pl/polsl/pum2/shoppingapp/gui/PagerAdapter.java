package pl.polsl.pum2.shoppingapp.gui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

public class PagerAdapter extends FragmentPagerAdapter {
    int numOfTabs;
    private Fragment currentFragment;

    public PagerAdapter(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new ShoppingListFragment();
            case 1:
                return new ShoppingCartFragment();
            default:
                return null;
        }
    }

    @Override
    public void setPrimaryItem (ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
        currentFragment = (Fragment)object;
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }

    public Fragment getCurrentFragment() {
        return currentFragment;
    }
}