package pl.polsl.pum2.shoppingapp.gui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import pl.polsl.pum2.shoppingapp.R;


public class ListEditorFragment extends Fragment {

    private static final String MESSAGE_DIALOG_TAG = "pl.polsl.pum2.shoppingapp.gui.ListEditorFragment.messageDialogTag";
    private EditText listName;

    public interface OnFragmentInteractionListener {
        void onEditList();
    }

    OnFragmentInteractionListener listener;

    public ListEditorFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_editor, container, false);
        listName = (EditText) view.findViewById(R.id.shopping_list_name);
        Button newMarketMapButton = (Button) view.findViewById(R.id.newMarketMapButton);
        newMarketMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createOrEditMarketMap();
            }
        });
        Button editMarketMapButton = (Button) view.findViewById(R.id.editMarketMapButton);
        editMarketMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createOrEditMarketMap();
            }
        });
        Button doneButton = (Button) view.findViewById(R.id.done_button);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (formIsCompleted()) {
                    getActivity().finish();
                } else {
                    showMessageDialog();
                }
            }
        });
        Button addProductsButton = (Button) view.findViewById(R.id.add_products_button);
        addProductsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (formIsCompleted()) {
                    listener.onEditList();
                } else {
                    showMessageDialog();
                }
            }
        });
        return view;
    }

    private void createOrEditMarketMap() {
        Intent intent = new Intent(getActivity(), MarketMapEditorActivity.class);
        getActivity().startActivity(intent);
    }

    private void showMessageDialog() {
        MessageDialogFragment messageDialogFragment = new MessageDialogFragment();
        Bundle message = new Bundle();
        message.putString(MessageDialogFragment.MESSAGE, getString(R.string.new_list_form_message));
        messageDialogFragment.setArguments(message);
        messageDialogFragment.show(getActivity().getSupportFragmentManager(), MESSAGE_DIALOG_TAG);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (OnFragmentInteractionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    boolean formIsCompleted() {
        //TODO: sprawdzanie stanu spinnera
        return listName.length() != 0;
    }

}