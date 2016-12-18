package com.example.jake_laptop.navigationdrawer;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TableLayout;

import java.util.List;

/**
 * Created by Jake-LAPTOP on 12/2/2016.
 */
public class StocksFragment extends Fragment {

    DB_Controller controller;
    View myView;

    Spinner spinnerCategory, spinnerDestination;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.stocks_layout, container, false);
        controller = new DB_Controller( myView.getContext(), "", null, 1);
//        controller.list_all_stocks((TextView) myView.findViewById(R.id.stocks_content_list));
        final TableLayout layout = (TableLayout) myView.findViewById(R.id.stocks_layout);
        controller.displayStocks(layout); //do this on submit filter


        Button btn_filterApply = (Button)myView.findViewById(R.id.btn_filter);
        btn_filterApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder filterBuilder = new AlertDialog.Builder(v.getContext());
                LayoutInflater inflater = LayoutInflater.from(v.getContext());
                final View filterView = inflater.inflate(R.layout.filter_layout, null);
                loadCategories(filterView);
                loadDestinations(filterView);

                filterBuilder.setTitle("Filter Stocks: ");
                filterBuilder.setView(filterView);

                //APPLY FILTER
                filterBuilder.setPositiveButton("Apply", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int catID = (int) spinnerCategory.getSelectedItemId();
                        String catName = controller.getCatNameById(catID+1);
                        System.out.println("CATEGORY STR: "+catName);
//                        controller.displayStocks_Filter("WHERE CATEGORY="++" ")


                        int destID = (int) spinnerDestination.getSelectedItemId();
                        String destName = controller.getDestNameById(destID+1);
                        System.out.println("CATEGORY STR: "+destName);

                        String where = "WHERE CATEGORY="+catID+" AND DESTINATION="+destID;

                        controller.displayStocks(layout, where);
                    }
                });


                //CANCEL FILTER
                filterBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });



                filterBuilder.show();
            }
        });

        return myView;
    }

    public void loadCategories(View view){
        spinnerCategory = (Spinner) view.findViewById(R.id.spinner_filterCat);

        List<String> labels = controller.populateCategories();
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter =
                new ArrayAdapter<String>(myView.getContext(),R.layout.spinner_item, labels);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);//simple_spinner_dropdown_item);

        spinnerCategory.setAdapter(dataAdapter);
    }//loadCategories

    public void loadDestinations(View view){
        spinnerDestination = (Spinner) view.findViewById(R.id.spinner_filterDest);

        List<String> labels = controller.populateDestinations();
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter =
                new ArrayAdapter<String>(myView.getContext(),R.layout.spinner_item, labels);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);//simple_spinner_dropdown_item);

        spinnerDestination.setAdapter(dataAdapter);
    }//loadDestinations

}//endclass
