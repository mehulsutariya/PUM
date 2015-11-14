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


public class CreateNewListFragment extends Fragment {

    EditText listName;

    public interface OnFragmentInteractionListener {
        void onEditList();
    }

    OnFragmentInteractionListener listener;

    public CreateNewListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_new_list, container, false);
        listName = (EditText)view.findViewById(R.id.shopping_list_name);
        Button newMarketMapButton = (Button)view.findViewById(R.id.newMarketMapButton);
        Button doneButton = (Button)view.findViewById(R.id.done_button);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFormComplete()) {
                    getActivity().finish();
                }
            }
        });
        Button addProductsButton = (Button)view.findViewById(R.id.add_products_button);
        addProductsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFormComplete()) {
                    listener.onEditList();
                }
            }
        });
        return view;
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

    boolean isFormComplete() {
        //TODO: sprawdzanie stanu spinnera
        if (listName.length() == 0) {
            return false;
        }
        return true;
    }


}
