package com.example.jake_laptop.navigationdrawer;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Spinner;

import java.util.List;

/**
 * Created by Jake-LAPTOP on 12/7/2016.
 */

public class StocksEditorFragment extends Fragment {
    DB_Controller controller;
    View myView;

    Spinner spinnerCategory, spinnerDestination;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.stocks_new_editor_layout, container, false);
        controller = new DB_Controller(myView.getContext(), "", null, 1);
        loadCategories();
        loadDestinations();

        return myView;
    }

    public void loadCategories(){
        spinnerCategory = (Spinner) myView.findViewById(R.id.spinnerCategory);

        List<String> labels = controller.populateCategories();
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter =
                new ArrayAdapter<String>(myView.getContext(),R.layout.spinner_item, labels);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);//simple_spinner_dropdown_item);

        spinnerCategory.setAdapter(dataAdapter);
    }//loadCategories

    public void loadDestinations(){
        spinnerDestination = (Spinner) myView.findViewById(R.id.spinnerDestination);

        List<String> labels = controller.populateDestinations();
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter =
                new ArrayAdapter<String>(myView.getContext(),R.layout.spinner_item, labels);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);//simple_spinner_dropdown_item);

        spinnerDestination.setAdapter(dataAdapter);
    }//loadCategories

}//endClass
