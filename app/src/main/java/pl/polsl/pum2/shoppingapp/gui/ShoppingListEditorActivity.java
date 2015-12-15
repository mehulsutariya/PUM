package pl.polsl.pum2.shoppingapp.gui;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import pl.polsl.pum2.shoppingapp.R;

public class ShoppingListEditorActivity extends AppCompatActivity implements ListEditorFragment.OnFragmentInteractionListener {

    public static final String MODE = "pl.polsl.pum2.shoppingapp.gui.mode";
    public static final String LIST_NAME = "pl.polsl.pum2.shoppingapp.gui.ShoppingListEditorActivity.listName";

    final static int CREATE_NEW_LIST = 0;
    final static int ADD_LIST_ITEMS = 1;
    private static final String NEW_LIST_FRAGMENT = "newListFragment";
    private static final String NEW_ITEMS_FRAGMENT = "newItemsFragment";
    private static final String IS_EDIT_MODE_DURING_LIST_CREATION = "isEditModeDuringListCreation";
    private ListItemsEditorFragment listItemsEditorFragment;
    private ListEditorFragment newListFragment;
    private FragmentManager fragmentManager;
    private boolean isEditModeDuringListCreation;
    private int mode;
    private String listName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list_editor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (savedInstanceState != null) {
            isEditModeDuringListCreation = savedInstanceState.getBoolean(IS_EDIT_MODE_DURING_LIST_CREATION);
        } else {
            isEditModeDuringListCreation = false;
        }
        setupFragments();
    }

    private void setupFragments() {
        mode = getIntent().getIntExtra(MODE, CREATE_NEW_LIST);
        fragmentManager = getSupportFragmentManager();

        if (mode == CREATE_NEW_LIST) {
            newListFragment = (ListEditorFragment) fragmentManager.findFragmentByTag(NEW_LIST_FRAGMENT);
            if (newListFragment == null) {
                newListFragment = new ListEditorFragment();
            }
            listItemsEditorFragment = (ListItemsEditorFragment) fragmentManager.findFragmentByTag(NEW_ITEMS_FRAGMENT);
            if (listItemsEditorFragment == null) {
                listItemsEditorFragment = new ListItemsEditorFragment();
            }
        } else {
            listName = getIntent().getStringExtra(LIST_NAME);
            listItemsEditorFragment = ListItemsEditorFragment.newInstance(listName);
        }

        setCurrentFragment();
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

    private void setCurrentFragment() {
        if (mode == ADD_LIST_ITEMS || isEditModeDuringListCreation) {
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, listItemsEditorFragment, NEW_ITEMS_FRAGMENT)
                    .commit();
            setTitle(R.string.title_activity_add_new_item);
        } else {
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, newListFragment, NEW_LIST_FRAGMENT)
                    .commit();
        }
    }

    @Override
    public void onEditList() {
        Bundle arguments = new Bundle();
        arguments.putString(ListItemsEditorFragment.LIST_NAME, listName);
        listItemsEditorFragment.setArguments(arguments);
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, listItemsEditorFragment, NEW_ITEMS_FRAGMENT)
                .addToBackStack(null)
                .commit();
        isEditModeDuringListCreation = true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(IS_EDIT_MODE_DURING_LIST_CREATION, isEditModeDuringListCreation);
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

}
