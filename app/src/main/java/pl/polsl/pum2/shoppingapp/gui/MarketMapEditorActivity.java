package pl.polsl.pum2.shoppingapp.gui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import pl.polsl.pum2.shoppingapp.R;

public class MarketMapEditorActivity extends AppCompatActivity {

    private static final String MAP_EDITOR_DIALOG_TAG = "MapEditorDialogTag";
    private EditText mapName;
    private RecyclerView mapContentRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_map_editor);
        mapName = (EditText) findViewById(R.id.map_name);
        mapContentRecyclerView = (RecyclerView) findViewById(R.id.map_content_recycler_view);
        mapContentRecyclerView.setAdapter(new MarketMapContentAdapter());
        mapContentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void doneButtonClick(View view) {
        if (mapName.length() == 0) {
            showDialog();
        } else {
            //TODO: zapisanie mapy do bazy danych
            finish();
        }
    }

    private void showDialog() {
        MessageDialogFragment dialogFragment = MessageDialogFragment.newInstance(getString(R.string.map_form_message));
        dialogFragment.show(getSupportFragmentManager(), MAP_EDITOR_DIALOG_TAG);
    }

    public void addButtonClick(View view) {

    }
}
