package com.example.jake_laptop.navigationdrawer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;


/**
 * Created by Jake-LAPTOP on 12/2/2016.
 */

//public class Notification extends Activity{
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.critical_level_layout);
//    }
//}

public class Notification extends Fragment {
    DB_Controller controller;
    View myView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.critical_level_layout, container, false);
        controller = new DB_Controller( myView.getContext(), "", null, 1);
//        controller.list_all_stocks((TextView) myView.findViewById(R.id.stocks_content_list));
        TableLayout layout = (TableLayout) myView.findViewById(R.id.critical_level_layout);
        controller.displayCLStocks(layout);
        return myView;
    }
}
