package com.example.jake_laptop.navigationdrawer;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;


/**
 * Created by Jake-LAPTOP on 12/2/2016.
 */

public class TransactionsNewFragment extends Fragment {

    DB_Controller controller;
    View myView;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.transactions_new_layout, container, false);
        controller = new DB_Controller( myView.getContext(), "", null, 1);
//        controller.list_all_stocks((TextView) myView.findViewById(R.id.stocks_content_list));

        final EditText ETtrans_PCode = (EditText) myView.findViewById(R.id.trans_PCode);
        final String trans_PCode = ETtrans_PCode.getText().toString();
        Button btn_trans_submit = (Button) myView.findViewById(R.id.btn_trans_submit);

        //bal default value
        TextView trans_curBal = (TextView)myView.findViewById(R.id.trans_curBal);
        trans_curBal.setText("0");

        ETtrans_PCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String bal = controller.getValueFromColumn("BALANCE", s.toString());
                System.out.println( bal );

                TextView trans_curBal = (TextView)myView.findViewById(R.id.trans_curBal);
                trans_curBal.setText(bal);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btn_trans_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            String trans_type = null;
            int trans_quantity = -1;

            EditText ETtrans_PCode = (EditText) myView.findViewById(R.id.trans_PCode);
            String trans_PCode = ETtrans_PCode.getText().toString();

            RadioGroup radioGroup = (RadioGroup)myView.findViewById(R.id.trans_type_group);
            int checkedId = radioGroup.getCheckedRadioButtonId();
            trans_type = ((RadioButton)myView.findViewById(checkedId)).getText().toString();

            String strQty = ((EditText) myView.findViewById(R.id.trans_quantity)).getText().toString();
            trans_quantity = Integer.parseInt(strQty);

            final String trans_date = ((TextClock)myView.findViewById(R.id.trans_date)).getText().toString();

                System.out.println(trans_PCode);
                System.out.println(trans_type);
                System.out.println(trans_quantity);
                System.out.println(trans_date);

                controller.add_newTransaction(trans_PCode, trans_type, trans_quantity, trans_date);
                controller.updateStockBalance(myView, trans_PCode, trans_type, trans_quantity);

                Toast.makeText(myView.getContext(), "Transaction Recorded!", Toast.LENGTH_SHORT).show();
            }
        });

        return myView;
    }


}
