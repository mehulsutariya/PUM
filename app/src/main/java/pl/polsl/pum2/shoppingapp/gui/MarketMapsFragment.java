package pl.polsl.pum2.shoppingapp.gui;

import io.realm.RealmResults;
import pl.polsl.pum2.shoppingapp.database.MarketMap;

public class MarketMapsFragment extends BaseRealmRecyclerViewFragment<MarketMap> {


    public MarketMapsFragment() {
        // Required empty public constructor
    }


    @Override
    protected RealmResults<MarketMap> runRealmQuery() {
        return getRealmInstance().where(MarketMap.class).findAllSorted("name");
    }


}
