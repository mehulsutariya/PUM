package pl.polsl.pum2.shoppingapp.gui;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import io.realm.Realm;

public abstract class BaseRealmFragment extends Fragment {
    protected Realm realm;

    public BaseRealmFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = Realm.getDefaultInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        //realm = Realm.getDefaultInstance();
    }

    @Override
    public void onStop() {
        super.onStop();
        //realm.close();
    }
}
