package com.example.jake_laptop.navigationdrawer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Jake-LAPTOP on 12/5/2016.
 */

public class Stocks_UpdateDialog extends Dialog
        implements View.OnClickListener{

    public View myView;

    EditText code, name, unit;

    public Stocks_UpdateDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setTitle("Enter Stock Information:");

//        LayoutInflater inflater = LayoutInflater.from(getContext());
//        this.myView = inflater.inflate(R.layout.stocks_update_editor_layout, null);
//
//        this.setContentView(this.myView);
        this.myView = getCurrentFocus();
        setContentView(R.layout.stocks_update_editor_layout);
    }

    @Override
    public void onClick(View v) {
        String code1 = ((Button)v).getText().toString();

//        EditText etNewCode = (EditText)this.myView.findViewById(R.id.etNewCode);

        System.out.println( "set code: "+ this.myView );
        show();
    }

    public void populateDialog(View v){
        //get button text from stock list
//        String newCode = ((Button)v.getId()).getText().toString();
//        System.out.println( "CODE: "+newCode );

//        System.out.println( this.myView.findViewById(R.id.updateLayout) );

        //pass newCode to dialog's etNewCode

    }
}//end class
