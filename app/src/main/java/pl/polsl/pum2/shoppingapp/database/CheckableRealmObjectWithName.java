package pl.polsl.pum2.shoppingapp.database;

public interface CheckableRealmObjectWithName {
    String getName();

    void setName(String name);

    boolean isChecked();

    void setChecked(boolean checked);
}
