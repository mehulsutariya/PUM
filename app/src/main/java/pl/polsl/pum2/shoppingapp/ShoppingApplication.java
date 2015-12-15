package pl.polsl.pum2.shoppingapp;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class ShoppingApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this).build();
        Realm.setDefaultConfiguration(realmConfiguration);
    }
}
