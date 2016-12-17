package com.example.jake_laptop.navigationdrawer;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;

/**
 * Created by Jake-LAPTOP on 12/2/2016.
 */

public class CategoryNDestinationFragment extends Fragment {

    DB_Controller controller;
    View myView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.category_and_destination_layout, container, false);
        controller = new DB_Controller( myView.getContext(), "", null, 1);

        TableLayout layoutCat = (TableLayout) myView.findViewById(R.id.category_layout);
        TableLayout layoutDest = (TableLayout) myView.findViewById(R.id.destination_layout);

        controller.displayCatNDest(layoutCat, layoutDest);
        return myView;
    }
}
