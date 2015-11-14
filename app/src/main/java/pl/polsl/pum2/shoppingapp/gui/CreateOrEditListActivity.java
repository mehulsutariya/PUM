package pl.polsl.pum2.shoppingapp.gui;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;

import pl.polsl.pum2.shoppingapp.R;

public class CreateOrEditListActivity extends AppCompatActivity implements CreateNewListFragment.OnFragmentInteractionListener {

    final static int CREATE_NEW_LIST = 0;
    final static int ADD_LIST_ITEMS = 1;
    FrameLayout fragmentContainer;
    AddNewItemsFragment newItemsFragment;
    CreateNewListFragment newListFragment;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    boolean isEditModeDuringListCreation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_or_edit_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupFragments();

    }

    private void setupFragments() {
        int mode = getIntent().getIntExtra("pl.polsl.pum2.shoppingapp.gui.mode", CREATE_NEW_LIST);

        fragmentContainer = (FrameLayout)findViewById(R.id.fragment_container);
        fragmentManager = getSupportFragmentManager();

        newItemsFragment = (AddNewItemsFragment)fragmentManager.findFragmentByTag("newItemsFragment");
        boolean newItemsFragmentAdded = true;
        if (newItemsFragment == null) {
            newItemsFragment = new AddNewItemsFragment();
            newItemsFragmentAdded = false;
        }

        fragmentTransaction = fragmentManager.beginTransaction();
        if (mode == ADD_LIST_ITEMS) {
            if (newItemsFragmentAdded == false) {
                fragmentTransaction.add(R.id.fragment_container, newItemsFragment, "newItemsFragment");
            }
            setTitle(R.string.title_activity_add_new_item);
        } else {
            newListFragment = (CreateNewListFragment)fragmentManager.findFragmentByTag("newListFragment");
            if (newListFragment == null) {
                newListFragment = new CreateNewListFragment();
                fragmentTransaction.add(R.id.fragment_container, newListFragment, "newListFragment");
            }
        }
        fragmentTransaction.commit();
        fragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                int count = fragmentManager.getBackStackEntryCount();
                if (count == 0 && isEditModeDuringListCreation) {
                    isEditModeDuringListCreation = false;
                }
            }
        });
    }

    @Override
    public void onEditList() {
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, newItemsFragment, "newItemsFragment");
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        isEditModeDuringListCreation = true;
    }

}
