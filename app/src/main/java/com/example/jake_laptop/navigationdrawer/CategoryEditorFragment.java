package com.example.jake_laptop.navigationdrawer;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Jake-LAPTOP on 12/7/2016.
 */

public class CategoryEditorFragment extends Fragment {
//    DB_Controller controller;
    View myView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        myView = inflater.inflate(R.layout.category_new_editor_layout, container, false);
        return myView;
    }
}//endClass
