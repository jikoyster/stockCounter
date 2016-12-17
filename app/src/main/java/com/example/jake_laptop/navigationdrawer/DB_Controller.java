package com.example.jake_laptop.navigationdrawer;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextClock;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jake-LAPTOP on 12/3/2016.
 */

public class DB_Controller extends SQLiteOpenHelper {

    //    Notification variables
    public static NotificationManager NM;
    boolean isNotificActive = false;
    int notifID = 0;

    String tbl_stocks = "STOCKS",
        tbl_category = "CATEGORY",
        tbl_destination = "DESTINATION",
        tbl_transactions = "TRANSACTIONS";

    public DB_Controller(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        //be sure database name "TEST.db" has extension .db
        super(context, "TEST027.db", factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sqlStocks = "CREATE TABLE "+ tbl_stocks +" " +
                "(" +
                "CODE           TEXT UNIQUE NOT NULL PRIMARY KEY, " +
                "NAME           TEXT UNIQUE, " +
                "UNIT           TEXT, " +
                "CATEGORY       INTEGER NOT NULL REFERENCES "+ tbl_category +"(ID), " +
                "DESTINATION    INTEGER NOT NULL REFERENCES "+ tbl_destination +"(ID), "+
                "CRITICAL_LEVEL INTEGER, " +
                "BALANCE        INTEGER, " +
                "DATE           TEXT" +
                "); ";
        String sqlCategory = "CREATE TABLE "+ tbl_category +" " +
                "(" +
                "ID         INTEGER PRIMARY KEY, " +
                "NAME       TEXT UNIQUE" +
                ");";
        String sqlDestination = "CREATE TABLE "+ tbl_destination +" " +
                "(" +
                "ID         INTEGER PRIMARY KEY, " +
                "NAME       TEXT UNIQUE" +
                ");";
        String sqlTransactions = "CREATE TABLE "+ tbl_transactions +" " +
                "(" +
                "ID         INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                "STOCK      REFERENCES STOCKS(CODE), " +
                "TYPE       TEXT, " +
                "QUANTITY   INTEGER, " +
                "DATE       TEXT " +
                ");";
        sqLiteDatabase.execSQL( sqlStocks );
        sqLiteDatabase.execSQL( sqlCategory );
        sqLiteDatabase.execSQL( sqlDestination );
        sqLiteDatabase.execSQL( sqlTransactions );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ tbl_stocks +";");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ tbl_category +";");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ tbl_destination +";");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ tbl_transactions +";");
        onCreate(sqLiteDatabase);
    }

    public void showNotification(View v){

        NotificationCompat.Builder mBuilder =(NotificationCompat.Builder) new NotificationCompat.Builder(v.getContext())
                .setSmallIcon(R.drawable.ic_alert)
                .setContentTitle("Stocks on critical level")
                .setContentText("Please check the stocks that needs update.")
                .setTicker("STOCKS ON CRITICAL LEVEL");
//                .setAutoCancel(true);
        Intent resultIntent = new Intent(v.getContext(), MainActivity.class);
        // Because clicking the notification opens a new ("special") activity, there's
        // no need to create an artificial back stack.
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        v.getContext(),
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        mBuilder.setContentIntent(resultPendingIntent);
        isNotificActive = true;
        NM.notify(notifID, mBuilder.build());
    }//*********** \showNotification

    public void stopNotification(View v){
        if(isNotificActive){
            NM.cancel(notifID);
        }
    }

    public void addStock(String code, String stockName, String unit, int catID, int destID, int cLevel, int Bal, String date)
            throws SQLException{
        System.out.println("write db");
        String sql = "INSERT INTO "+ tbl_stocks +"(CODE, NAME, UNIT, CATEGORY, DESTINATION, CRITICAL_LEVEL, BALANCE, DATE) " +
                "VALUES('"+code+"', '"+stockName+"', '"+unit+"', '"+catID+"', '"+destID+"', '"+cLevel+"', "+Bal+", '"+date+"')";
        this.getWritableDatabase().execSQL( sql );
    }

    public void delete_student(String firstname){
        this.getWritableDatabase().delete("STUDENTS", "FIRSTNAME='"+firstname+"'", null);
    }

    public void update_student(String old_firstname, String new_firstname){
        this.getWritableDatabase().execSQL("UPDATE "+ tbl_stocks +" SET FIRSTNAME='"+new_firstname+"'" +
                " WHERE FIRSTNAME='"+old_firstname+"';");
    }

    public void list_all_stocks(TextView textView){
        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT * FROM "+ tbl_stocks +";", null);
        textView.setText("");
        while(cursor.moveToNext()){
            textView.append(cursor.getString(0)+"  "+cursor.getString(1) +"  "+cursor.getString(2) +"\n");
        }
    }

    public boolean getStocks(String trans_PCode)
            throws NullPointerException{
        boolean flag = false;
        try {
            String sql = "SELECT * FROM " + tbl_stocks;
            Cursor cursor = this.getReadableDatabase().rawQuery(sql, null);
            if (cursor.moveToNext()) {
                flag = true;
            }else{
                flag = false;
            }
        }catch (NullPointerException e){
            throw new NullPointerException();
        }
        System.out.println("FLAG DB--- "+flag);
        return flag;
    }

    public void displayStocks(final TableLayout tableLayout) {
        tableLayout.removeAllViews();
        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT * FROM "+ tbl_stocks +" ORDER BY NAME ASC ;", null);
//        view.setText("");
//        setStockHeader(tableLayout);
        int intID = 0;
        while(cursor.moveToNext()){
//            view.append(cursor.getString(1)+"  "+cursor.getString(2)+"\n");
            TableRow row = new TableRow(tableLayout.getContext());
            row.setMinimumHeight(200);
            row.setLongClickable(true);

            row.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(final View v) {
                    v.setBackgroundColor(Color.LTGRAY);

                    AlertDialog.Builder innerBuilder = new AlertDialog.Builder(v.getContext());
                    innerBuilder.setMessage("DELETING THIS RECORD!:\nAre you sure?");

                    final String oldCode = ((Button)v.findViewWithTag("code")).getText().toString();
                    innerBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            getWritableDatabase().execSQL("DELETE FROM "+ tbl_stocks +" WHERE CODE='"+ oldCode +"';");
                            displayStocks(tableLayout);
                        }
                    });
                    innerBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            v.setBackgroundColor(Color.WHITE);
                        }
                    });
                    innerBuilder.show();

                    return false;
                }
            });

            //GET AND SET STOCK INFOs to Stocks Management Layout
            //CODE
            TextView lblCode = new TextView(tableLayout.getContext());
            lblCode.setText("Code: ");
            final Button code = new Button(tableLayout.getContext());
            code.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            code.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
            code.setTextSize(20);
            code.setText(cursor.getString(0));
            code.setTag("code");

            //NAME
            TextView lblName = new TextView(tableLayout.getContext());
            lblName.setText("Name: ");
            TextView name = new TextView(tableLayout.getContext());
            name.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            name.setTypeface(null, Typeface.BOLD);
            name.setTextSize(25);
            name.setText(cursor.getString(1));

            //UNIT
            TextView lblUnit = new TextView(tableLayout.getContext());
            lblUnit.setText("Unit: ");
            TextView unit = new TextView(tableLayout.getContext());
            unit.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
            unit.setText(cursor.getString(2));

            //CATEGORY
            TextView lblCat = new TextView(tableLayout.getContext());
            lblCat.setText("CATEGORY: ");
            TextView tvCat = new TextView(tableLayout.getContext());
            tvCat.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
            tvCat.setText( getCatNameById(Integer.valueOf(cursor.getString(3))+1) );

            //DESTINATION
            TextView lblDest = new TextView(tableLayout.getContext());
            lblDest.setText("DESTINATION: ");
            TextView tvDest = new TextView(tableLayout.getContext());
            tvDest.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
            tvDest.setText( getDestNameById(Integer.valueOf(cursor.getString(4))+1) );

            //CRITICAL LEVEL
            TextView lblcritcalLevel = new TextView(tableLayout.getContext());
            lblcritcalLevel.setText("Critical Level: ");
            TextView critcalLevel = new TextView(tableLayout.getContext());
            critcalLevel.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
            critcalLevel.setTextColor(Color.parseColor("#990000"));
            critcalLevel.setTextSize(16);
            critcalLevel.setTypeface(null, Typeface.BOLD);
            critcalLevel.setText( cursor.getString(5) );

            //BAL
            TextView lblBal = new TextView(tableLayout.getContext());
            lblBal.setText("Balance: ");
            TextView bal = new TextView(tableLayout.getContext());
            bal.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
            bal.setTextColor(Color.parseColor("#009900"));
            bal.setTextSize(16);
            bal.setTypeface(null, Typeface.BOLD);
            bal.setText( cursor.getString(6) );

            /* code set setOnClickListener */
            code.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String oldCode    = ((Button)v).getText().toString();
                    final String name       = getValueFromColumn("NAME", oldCode);
                    final String unit       = getValueFromColumn("UNIT", oldCode);

                    String strCat           = "0";
                    strCat                  = getValueFromColumn("CATEGORY", oldCode);
                    final int category      = Integer.parseInt( strCat );
                    System.out.println("category: "+category);

                    String strDest          = "0";
                    strDest                 = getValueFromColumn("DESTINATION", oldCode);
                    final int destination   = Integer.parseInt( strDest );
                    System.out.println("destination: "+destination);

                    final String cLevel     = getValueFromColumn("CRITICAL_LEVEL", oldCode);
                    final String bal        = getValueFromColumn("BALANCE", oldCode);

                    final AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
//                    builder.setTitle("Update Stock Info: "+ oldCode);

                    LayoutInflater inflater = LayoutInflater.from(v.getContext());
                    View updateView = inflater.inflate(R.layout.stocks_update_editor_layout, null);
                    //setting popup with contents
                    final TextView newCode          = ((TextView)updateView.findViewById(R.id.etNewCode));
                    final TextView newName          = ((TextView)updateView.findViewById(R.id.etNewName));
                    final TextView newUnit          = ((TextView)updateView.findViewById(R.id.etNewUnit));
                    final Spinner newCategory       = ((Spinner)updateView.findViewById(R.id.spinnerNewCategory));
                    loadCategories_OnUpdate(updateView, category);
                    final Spinner newDestination    = ((Spinner)updateView.findViewById(R.id.spinnerNewDestination));
                    loadDestinations_OnUpdate(updateView, destination);
                    final TextView newCriticalLevel = ((TextView)updateView.findViewById(R.id.etNewClevel));
                    final TextView newBalance       = ((TextView)updateView.findViewById(R.id.etNewBal));

                    newCode.setText(oldCode);
                    newName.setText(name);
                    newUnit.setText(unit);

                    newCriticalLevel.setText(cLevel);
                    newBalance.setText(bal);

                    builder.setView(updateView);

                    builder.setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            getWritableDatabase().execSQL("UPDATE "+ tbl_stocks +" SET " +
                                    "CODE='"+newCode.getText().toString()+"', " +
                                    "NAME='"+newName.getText().toString()+"', " +
                                    "UNIT='"+newUnit.getText().toString()+"', " +
                                    "CATEGORY='"+newCategory.getSelectedItemPosition()+"'," +
                                    "DESTINATION='"+newDestination.getSelectedItemPosition()+"', " +
                                    "CRITICAL_LEVEL='"+newCriticalLevel.getText().toString()+"', " +
                                    "BALANCE='"+newBalance.getText().toString()+"' " +
                                    "WHERE CODE='"+ oldCode +"';");

                            displayStocks(tableLayout);
                        }
                    });

                    builder.setNegativeButton("NEW TRANSACTION", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            AlertDialog.Builder tBuilder = new AlertDialog.Builder(builder.getContext());
                            tBuilder.setTitle("New Transaction");


                            LayoutInflater inflater = LayoutInflater.from(builder.getContext());
                            final View transactionView = inflater.inflate(R.layout.transactions_new_editor_layout, null);

                            TextView pcode = (TextView)transactionView.findViewById(R.id.trans_PCode);
                            pcode.setText(oldCode);
                            //type no needed to display by default
                            TextView curBal = (TextView)transactionView.findViewById(R.id.curBal);
                            curBal.setTextColor( Color.parseColor("#009900") );
                            curBal.setText("BALANCE: "+ newBalance.getText());

                            EditText quantity = (EditText)transactionView.findViewById(R.id.trans_quantity);
                            quantity.setText("");

                            tBuilder.setView(transactionView);


                            final String trans_PCode = ((TextView)transactionView.findViewById(R.id.trans_PCode)).getText().toString();
//                            String strQty = ((EditText)transactionView.findViewById(R.id.trans_quantity)).getText().toString();
//                            final int trans_quantity = Integer.parseInt(strQty);
                            final String trans_date = ((TextClock)transactionView.findViewById(R.id.trans_date)).getText().toString();

                            //get RadioButton value on submit
                            final RadioGroup radioGroup = (RadioGroup)transactionView.findViewById(R.id.trans_type_group);
                            radioGroup.check(R.id.OUT);

                            tBuilder.setPositiveButton("SUBMIT", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                        throws NullPointerException {
                                    int checkedId = radioGroup.getCheckedRadioButtonId();
                                    String trans_date = ((TextClock)transactionView.findViewById(R.id.trans_date)).getText().toString();
                                    String strQty = ((EditText)transactionView.findViewById(R.id.trans_quantity)).getText().toString();
                                    int trans_quantity = Integer.parseInt(strQty);
                                    String trans_type = ((RadioButton)transactionView.findViewById(checkedId)).getText().toString();

                                    System.out.println(trans_PCode);
                                    System.out.println("rg: "+trans_type);
                                    System.out.println("1st: "+trans_quantity);
                                    System.out.println("DATE: "+trans_date);

                                    add_newTransaction(trans_PCode, trans_type, trans_quantity, trans_date);

                                    updateStockBalance(transactionView, trans_PCode, trans_type, trans_quantity);
                                    displayStocks(tableLayout);
                                }
                            });//tBuilder.setPositiveButton

                            tBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });//tBuilder.setNegativeButton

                            tBuilder.show();
                        }
                    });//builder.setNegativeButton

                    builder.setNeutralButton("CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });//builder.setNeutralButton



                    builder.show();
                }// onClick()
                /* \code set setOnClickListener */
            }); // \setOnClickListener


            /* \set setOnClickListener */

            row.setPadding(0,30,0,0);
            row.addView(lblCode);
            row.addView(code);

            TableRow row2 = new TableRow(tableLayout.getContext());
            row2.addView(lblName);
            row2.addView(name);

            TableRow row3 = new TableRow(tableLayout.getContext());
            row3.addView(lblUnit);
            row3.addView(unit);

            TableRow row3_1 = new TableRow(tableLayout.getContext());
            row3_1.addView(lblCat);
            row3_1.addView(tvCat);
            TableRow row3_2 = new TableRow(tableLayout.getContext());
            row3_2.addView(lblDest);
            row3_2.addView(tvDest);

            TableRow row4 = new TableRow(tableLayout.getContext());
            row4.addView(lblcritcalLevel);
            row4.addView(critcalLevel);

            TableRow row5 = new TableRow(tableLayout.getContext());
            row5.setPadding(0,0,0,30);
            row5.addView(lblBal);
            row5.addView(bal);


            TextView Hline = new TextView(tableLayout.getContext());
            Hline.setMinWidth(LinearLayout.LayoutParams.MATCH_PARENT);
            Hline.setMaxHeight(1);
            Hline.setBackgroundColor(Color.LTGRAY);

            tableLayout.addView( row );
            tableLayout.addView( row2 );
            tableLayout.addView( row3 );
            tableLayout.addView( row3_1 );
            tableLayout.addView( row3_2 );
            tableLayout.addView( row4 );
            tableLayout.addView( row5 );
            tableLayout.addView( Hline );


            intID++;
        }//endwhile
    }

    public void add_newTransaction(String trans_PCode, String trans_type, int trans_quantity, String trans_date){
        String insertTransactionSQL = "INSERT INTO "+tbl_transactions+"" +
                "(STOCK, TYPE, QUANTITY, DATE)" +
                "VALUES ('"+trans_PCode+"', '"+trans_type+"', "+trans_quantity+", '"+trans_date+"');";

        getWritableDatabase().execSQL(insertTransactionSQL);
    }

    public void updateStockBalance(View trasactionView, String trans_PCode, String trans_type, int trans_qty)
            throws NumberFormatException{
        try {
            System.out.println("---------updateStockBalance---");
            int stockBal = Integer.parseInt(getValueFromColumn("BALANCE", trans_PCode));
            int newBal = 0;

            System.out.println("stockBal=" + stockBal);
            System.out.println("2nd. trans_qty=" + trans_qty);

            switch (trans_type) {
                case "IN": //add to stocks
                    System.out.println("type:qty = " + trans_type + ":" + trans_qty);
                    newBal = stockBal + trans_qty;
                    break;
                case "OUT": //subtract to stocks
                    newBal = stockBal - trans_qty;
                    System.out.println("type:qty = " + trans_type + ":" + trans_qty);

                    if (checkSTockCriticalLevel(trans_PCode, newBal)) {
                        showNotification(trasactionView);
                    }

                    break;
            }

            System.out.println("newBal=" + newBal);

            String updateStockBal = "UPDATE " + tbl_stocks + " SET " +
                    "BALANCE=" + newBal + " WHERE CODE='" + trans_PCode + "'";
            getWritableDatabase().execSQL(updateStockBal);
        }catch (NumberFormatException e){
            //throw new NumberFormatException();
        }
    }

    private void setStockHeader(TableLayout tableLayout) {
        TableRow row = new TableRow(tableLayout.getContext());
        row.setBackgroundColor(Color.parseColor("#CCCCCC"));
        row.setPadding(0, 10, 10, 10);

        String textColor = "#000000";
            TextView code = new TextView(tableLayout.getContext());
                code.setText("CODE");
                code.setTextColor(Color.parseColor(textColor));
                code.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD), 1);
                code.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            TextView name = new TextView(tableLayout.getContext());
                name.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD), 1);
                name.setTextColor(Color.parseColor(textColor));
                name.setText("NAME");
            TextView unit = new TextView(tableLayout.getContext());
                unit.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD), 1);
                unit.setTextColor(Color.parseColor(textColor));
                unit.setText("UNIT");
            TextView criticalLevel = new TextView(tableLayout.getContext());
                criticalLevel.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD), 1);
                criticalLevel.setTextColor(Color.parseColor(textColor));
                criticalLevel.setPadding(10,0,10,0);
                criticalLevel.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                criticalLevel.setText("CRTCL\nLEVEL");
            TextView bal = new TextView(tableLayout.getContext());
                bal.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD), 1);
                bal.setTextColor(Color.parseColor(textColor));
                bal.setPadding(10,0,10,0);
                bal.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                bal.setText("BAL");

        row.addView(code);
        row.addView(name);
        row.addView(unit);
        row.addView(criticalLevel);
        row.addView(bal);

        tableLayout.addView(row);
    }

    public String getValueFromColumn(String colName, String code){
        String val = "";
        Cursor cursor = null;
        String sql = "SELECT "+colName+" FROM "+ tbl_stocks +" WHERE CODE='"+code+"';";
        cursor = this.getReadableDatabase().rawQuery(sql , null);

        while(cursor.moveToNext()){
            val = cursor.getString(0);
        }
        return val;
    }

    public boolean checkSTockCriticalLevel(String code, int newBal){
        boolean flag = false;
        int criticalLevel =  Integer.parseInt( getValueFromColumn("CRITICAL_LEVEL", code) );


        flag = (newBal <= criticalLevel)? true : false;

        return flag;
    }//checkSTockCriticalLevel
/*****************************************************************************************/
/*************************************CATEGORY********************************************/
/*****************************************************************************************/
    public void addCategory(String name)
            throws SQLException{
        String sql = "INSERT INTO "+ tbl_category +"(NAME) " +
                "VALUES('"+name+"')";
        this.getWritableDatabase().execSQL( sql );
    }

    public String getCatID(String name){
        Cursor cursor = null;
        cursor = this.getReadableDatabase().rawQuery("SELECT ID FROM "+ tbl_category +" WHERE NAME='"+name+"';", null);
        String val = "";
        while(cursor.moveToNext()){
            val = cursor.getString(0);
        }
        return val;
    }

    public String getCatNameById(int catID){
        Cursor cursor = null;
        cursor = this.getReadableDatabase().rawQuery("SELECT NAME FROM "+ tbl_category +" WHERE ID="+catID+";", null);
        String val = "";
        while(cursor.moveToNext()){
            val = cursor.getString(0);
        }
        return val;
    }



    private void setCatNDestHeader(TableLayout tableLayout, TableLayout tableLayoutDest) {
        TableRow row = new TableRow(tableLayout.getContext());
        row.setBackgroundColor(Color.parseColor("#CCCCCC"));
        row.setPadding(10,10,10,10);

        TextView CATS = new TextView(tableLayout.getContext());
        CATS.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD), 1);
        CATS.setTextSize(18);
        CATS.setPadding(10,10,10,10);
        CATS.setTextColor(Color.parseColor("#000000"));
        CATS.setText("CATEGORIES");

        row.addView(CATS);

        tableLayout.addView(row);

        //for destinations
        TableRow rowDest = new TableRow(tableLayout.getContext());
        rowDest.setBackgroundColor(Color.parseColor("#CCCCCC"));
        rowDest.setPadding(10,10,10,10);

        TextView DEST = new TextView(tableLayout.getContext());
        DEST.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD), 1);
        DEST.setTextSize(18);
        DEST.setPadding(10,10,30,10);
        DEST.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
        DEST.setTextColor(Color.parseColor("#000000"));
        DEST.setText("DESTINATIONS");

        rowDest.addView(DEST);

        tableLayoutDest.addView(rowDest);
    }

    public void displayCatNDest(final TableLayout tableLayout, final TableLayout tableLayoutDest){
        tableLayout.removeAllViews();
        tableLayoutDest.removeAllViews();
        Cursor cursorCat  = this.getReadableDatabase().rawQuery("SELECT * FROM "+ tbl_category +" ORDER BY ID ASC ;", null);
        Cursor cursorDest = this.getReadableDatabase().rawQuery("SELECT * FROM "+ tbl_destination +" ORDER BY ID ASC ;", null);
        setCatNDestHeader(tableLayout, tableLayoutDest);

        int intID = 0;
        while(cursorCat.moveToNext()){
            TableRow rowCat = new TableRow(tableLayout.getContext());
            //NAME
            Button name = new Button(tableLayout.getContext());
            name.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            name.setTypeface(null, Typeface.BOLD);
            name.setTextSize(16);
            name.setText(cursorCat.getString(1));

            /* set setOnClickListener */
            name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String oldName = ((TextView)v).getText().toString();

                    final AlertDialog.Builder catBuilder = new AlertDialog.Builder(v.getContext());
                    catBuilder.setTitle("Update Category Info: "+ oldName);

                    LayoutInflater inflater = LayoutInflater.from(v.getContext());
                    View updateView = inflater.inflate(R.layout.category_update_editor_layout, null);
                    //setting popup with contents
                    final TextView newName = ((TextView)updateView.findViewById(R.id.etNewName));


                    newName.setText(oldName);

                    catBuilder.setView(updateView);

                    catBuilder.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            getWritableDatabase().execSQL("UPDATE "+ tbl_category +" SET " +
                                    "NAME='"+newName.getText().toString()+"' " +
                                    "WHERE ID ="+ getCatID(oldName) +";");
                            displayCatNDest(tableLayout, tableLayoutDest);
                        }
                    });

                    catBuilder.setNegativeButton("DELETE", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            AlertDialog.Builder innerBuilder = new AlertDialog.Builder(catBuilder.getContext());
                            innerBuilder.setMessage("Are you sure?");
                            innerBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    getWritableDatabase().execSQL("DELETE FROM "+ tbl_category +" WHERE ID="+ getCatID(oldName) +";");
                                    displayCatNDest(tableLayout, tableLayoutDest);
                                }
                            });
                            innerBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                            innerBuilder.show();
                            displayCatNDest(tableLayout, tableLayoutDest);
                        }
                    });

                    catBuilder.setNeutralButton("CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });


                    catBuilder.show();
                }// onClick()
            }); // \setOnClickListener Category



            /* \set setOnClickListener */

//            row.addView(catID);
            rowCat.addView(name);
            tableLayout.addView( rowCat );


            intID++;
        }//endwhile categories


        int intIDDest = 0;
        while(cursorDest.moveToNext()){
            TableRow rowDest = new TableRow(tableLayoutDest.getContext());
            //NAME
            Button name = new Button(tableLayoutDest.getContext());
            name.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            name.setTypeface(null, Typeface.BOLD);
            name.setTextSize(16);
            name.setText(cursorDest.getString(1));

            /* set setOnClickListener */
            name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String oldName = ((TextView)v).getText().toString();

                    final AlertDialog.Builder destBuilder = new AlertDialog.Builder(v.getContext());
                    destBuilder.setTitle("Update Destination Info: "+ oldName);

                    LayoutInflater inflater = LayoutInflater.from(v.getContext());
                    View updateView = inflater.inflate(R.layout.category_update_editor_layout, null);
                    //setting popup with contents
                    final TextView newName = ((TextView)updateView.findViewById(R.id.etNewName));
                            newName.setHint("DESTINATION NAME");

                    newName.setText(oldName);
                    destBuilder.setView(updateView);

                    destBuilder.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            getWritableDatabase().execSQL("UPDATE "+ tbl_destination +" SET " +
                                    "NAME='"+newName.getText().toString()+"' " +
                                    "WHERE ID ="+ getDestID(oldName) +";");
                            displayCatNDest(tableLayout, tableLayoutDest);
                        }
                    });

                    destBuilder.setNegativeButton("DELETE", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            AlertDialog.Builder innerBuilder = new AlertDialog.Builder(destBuilder.getContext());
                            innerBuilder.setMessage("Are you sure?");
                            innerBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String sql = "DELETE FROM "+ tbl_destination +" WHERE ID="+ getDestID(oldName) +";";
                                    System.out.println(sql);
                                    getWritableDatabase().execSQL(sql);
                                    displayCatNDest(tableLayout, tableLayoutDest);
                                }
                            });
                            innerBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                            innerBuilder.show();
                            displayCatNDest(tableLayout, tableLayoutDest);
                        }
                    });

                    destBuilder.setNeutralButton("CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });


                    destBuilder.show();
                }// onClick()
            }); // \setOnClickListener Category



            /* \set setOnClickListener */

//            row.addView(catID);
            rowDest.addView(name);
            tableLayoutDest.addView( rowDest );


            intIDDest++;
        }//endwhile destinations


    }//displayCategory

    //get categories
    public List<String> populateCategories(){
        List<String> list = new ArrayList<String>();

        String selectQuery = "SELECT NAME FROM " + tbl_category;
        Cursor cursor = this.getReadableDatabase().rawQuery(selectQuery, null);

        // looping through all rows and adding to list
//        if (cursor.moveToFirst()) {
//            do {
//                list.add(cursor.getString(0));//adding 2nd column data
//            } while (cursor.moveToNext());
//        }
        while(cursor.moveToNext()){
            list.add(cursor.getString(0));
        }

        return list;
    }


    public void loadCategories_OnUpdate(View view, int catID){
        Spinner spinnerCategory = (Spinner) view.findViewById(R.id.spinnerNewCategory);

        List<String> labels = populateCategories();
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter =
                new ArrayAdapter<String>(view.getContext(),android.R.layout.simple_spinner_item, labels);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);//simple_spinner_dropdown_item);

        spinnerCategory.setAdapter(dataAdapter);


        spinnerCategory.setSelection(catID);
    }//loadCategories


    /*****************************************************************************************/
/*************************************DESTINATIONS********************************************/
    /*****************************************************************************************/
    public void addDestination(String name)
            throws SQLException{
        String sql = "INSERT INTO "+ tbl_destination +"(NAME) " +
                "VALUES('"+name+"')";
        this.getWritableDatabase().execSQL( sql );
    }

    public String getDestID(String name){
        Cursor cursor = null;
        cursor = this.getReadableDatabase().rawQuery("SELECT ID FROM "+ tbl_destination +" WHERE NAME='"+name+"';", null);
        String val = "";
        while(cursor.moveToNext()){
            val = cursor.getString(0);
        }
        return val;
    }

    public String getDestNameById(int destID){
        Cursor cursor = null;
        cursor = this.getReadableDatabase().rawQuery("SELECT NAME FROM "+ tbl_destination +" WHERE ID='"+destID+"';", null);
        String val = "";
        while(cursor.moveToNext()){
            val = cursor.getString(0);
        }
        return val;
    }

    //get destinations
    public List<String> populateDestinations(){
        List<String> list = new ArrayList<String>();

        String selectQuery = "SELECT NAME FROM " + tbl_destination;
        Cursor cursor = this.getReadableDatabase().rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                list.add(cursor.getString(0));//adding 2nd column data
            } while (cursor.moveToNext());
        }

        return list;
    }

    public void loadDestinations_OnUpdate(View view, int destID){
        Spinner spinnerDestination = (Spinner) view.findViewById(R.id.spinnerNewDestination);

        List<String> labels = populateDestinations();
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter =
                new ArrayAdapter<String>(view.getContext(),android.R.layout.simple_spinner_item, labels);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);//simple_spinner_dropdown_item);

        spinnerDestination.setAdapter(dataAdapter);


        spinnerDestination.setSelection(destID);
    }//loadCategories


    /******* TRANSACTIONS *************************************************************************/
    private void setTransactionsHeader(TableLayout tableLayout) {
        TableRow row = new TableRow(tableLayout.getContext());
        row.setBackgroundColor(Color.parseColor("#CCCCCC"));
        row.setPadding(0, 10, 10, 10);

        String textColor = "#000000";
        TextView ID = new TextView(tableLayout.getContext());
        ID.setText("ID");
        ID.setTextColor(Color.parseColor(textColor));
        ID.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD), 1);
        ID.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        TextView STOCK = new TextView(tableLayout.getContext());
        STOCK.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD), 1);
        STOCK.setTextColor(Color.parseColor(textColor));
        STOCK.setText("STOCK");
        TextView TYPE = new TextView(tableLayout.getContext());
        TYPE.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD), 1);
        TYPE.setTextColor(Color.parseColor(textColor));
        TYPE.setText("TYPE");
        TextView QTY = new TextView(tableLayout.getContext());
        QTY.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD), 1);
        QTY.setTextColor(Color.parseColor(textColor));
        QTY.setPadding(10,0,10,0);
        QTY.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        QTY.setText("QTY");
        TextView DATE = new TextView(tableLayout.getContext());
        DATE.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD), 1);
        DATE.setTextColor(Color.parseColor(textColor));
        DATE.setPadding(10,0,10,0);
        DATE.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        DATE.setText("DATE");

        row.addView(ID);
        row.addView(STOCK);
        row.addView(TYPE);
        row.addView(QTY);
        row.addView(DATE);

        tableLayout.addView(row);
    }

    public void displayTransactions(final TableLayout tableLayout){
        tableLayout.removeAllViews();
        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT * FROM "+ tbl_transactions +" ORDER BY ID DESC;", null);
//        view.setText("");
//        setTransactionsHeader(tableLayout);

        int intID = 0;
        while(cursor.moveToNext()){
//            view.append(cursor.getString(1)+"  "+cursor.getString(2)+"\n");
            TableRow row = new TableRow(tableLayout.getContext());

            //GET AND SET STOCK INFOs
            //ID
            TextView lblID = new TextView(tableLayout.getContext());
            lblID.setText("Transaction ID");
            Button ID = new Button(tableLayout.getContext());
            ID.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            ID.setText(cursor.getString(0));
            ID.setMaxWidth(70);

            //STOCK
            TextView lblStock = new TextView(tableLayout.getContext());
            lblStock.setText("Stock");
            TextView STOCK = new TextView(tableLayout.getContext());
            STOCK.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            STOCK.setTypeface(null, Typeface.BOLD);
            STOCK.setTextSize(16);
            STOCK.setText(cursor.getString(1));
            //NAME
            TextView lblStockName = new TextView(tableLayout.getContext());
            lblStockName.setText("Name");
            TextView NAME = new TextView(tableLayout.getContext());
            NAME.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            NAME.setTypeface(null, Typeface.BOLD);
            NAME.setTextSize(16);
            NAME.setText( getValueFromColumn("NAME", cursor.getString(1)) );

            //TYPE
            TextView lblType = new TextView(tableLayout.getContext());
            lblType.setText("Type");
            TextView TYPE = new TextView(tableLayout.getContext());
            TYPE.setMaxWidth(80);
            TYPE.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
            String strType = cursor.getString(2);
            TYPE.setText(strType);

            //QTY
            TextView lblQty = new TextView(tableLayout.getContext());
            lblQty.setText("Qty");
            TextView QTY = new TextView(tableLayout.getContext());
            QTY.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
            String qtyColor = (strType.equals("OUT"))? "#990000" : "#009900";
            QTY.setTextColor(Color.parseColor(qtyColor));
            QTY.setTextSize(16);
            QTY.setTypeface(null, Typeface.BOLD);
            QTY.setText( cursor.getString(3) );

            //DATE
            TextView lblDate = new TextView(tableLayout.getContext());
            lblDate.setText("Date");
            TextView DATE = new TextView(tableLayout.getContext());
            DATE.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
            DATE.setTextSize(16);
            DATE.setText( cursor.getString(4) );



            /* code set setOnClickListener */
            ID.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                   final int transID =  Integer.parseInt( ((Button)v).getText().toString() );

                    AlertDialog.Builder checkTrans = new AlertDialog.Builder(v.getContext());
                    checkTrans.setTitle("Check Transaction");

                    checkTrans.setPositiveButton("Delete Record", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //SIMPLY DELETE A TRANSACTION
                            AlertDialog.Builder deleteRecordConfirmation = new AlertDialog.Builder(v.getContext());
                            deleteRecordConfirmation.setTitle("Are you sure?");
                            deleteRecordConfirmation.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String deleteTransactionSql = "DELETE FROM "+ tbl_transactions +" WHERE ID="+transID;
                                    getWritableDatabase().execSQL(deleteTransactionSql);
                                    displayTransactions(tableLayout);
                                }
                            });
                            deleteRecordConfirmation.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //DO NOTHING
                                }
                            });
                            deleteRecordConfirmation.show();
                        }
                    });//Delete Record

                    checkTrans.setNegativeButton("Cancel Transaction", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //CANCEL A TRANSACTION AND RETURN THE QUANTITY TO THE STOCK'S BALANCE
                            //THEN DELETE THE RECORD
                        }
                    });
                    checkTrans.show();
                }// onClick()
                /* \code set setOnClickListener */
            }); // \setOnClickListener


            /* \set setOnClickListener */

            row.setPadding(0,30,0,0);
            row.addView(lblID);
            row.addView(ID);


            TableRow row2 = new TableRow(tableLayout.getContext());
            row2.addView(lblStock);
            row2.addView(STOCK);
            TableRow row2_1 = new TableRow(tableLayout.getContext());
            row2_1.addView(lblStockName);
            row2_1.addView(NAME);

            TableRow row3 = new TableRow(tableLayout.getContext());
            row3.addView(lblType);
            row3.addView(TYPE);

            TableRow row4 = new TableRow(tableLayout.getContext());
            row4.addView(lblQty);
            row4.addView(QTY);

            TableRow row5 = new TableRow(tableLayout.getContext());
            row5.addView(lblDate);
            row5.addView(DATE);
            row5.setPadding(0,0,0,30);

            TextView Hline = new TextView(tableLayout.getContext());
            Hline.setMinWidth(LinearLayout.LayoutParams.MATCH_PARENT);
            Hline.setMaxHeight(1);
            Hline.setBackgroundColor(Color.LTGRAY);


            tableLayout.addView( row );
            tableLayout.addView( row2 );
            tableLayout.addView( row2_1 );
            tableLayout.addView( row3 );
            tableLayout.addView( row4 );
            tableLayout.addView( row5 );
            tableLayout.addView( Hline );


            intID++;
        }//endwhile
    }


/********** NOTIFICATION - PRODUCTS THAT REACH CRITICAL LEVEL ***********************************************/
    public void displayCLStocks(final TableLayout tableLayout) {
        tableLayout.removeAllViews();
        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT * FROM "+ tbl_stocks +" WHERE BALANCE <= CRITICAL_LEVEL ORDER BY NAME ASC ;", null);
//        view.setText("");
//        setStockHeader(tableLayout);

        int intID = 0;
        while(cursor.moveToNext()){
//            view.append(cursor.getString(1)+"  "+cursor.getString(2)+"\n");
            TableRow row = new TableRow(tableLayout.getContext());
//            row.setMinimumHeight(200);

            //GET AND SET STOCK INFOs
            //CODE
            TextView lblCode = new TextView(tableLayout.getContext());
            lblCode.setText("Code");
            final Button code = new Button(tableLayout.getContext());
            code.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            code.setText(cursor.getString(0));
            code.setMaxWidth(100);
            code.setPadding(30,0,0,0);
//            code.setOnClickListener(new Stocks_UpdateDialog( tableLayout.getContext() ));

            //NAME
            TextView lblName = new TextView(tableLayout.getContext());
            lblName.setText("Name");
            TextView name = new TextView(tableLayout.getContext());
            name.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            name.setMaxWidth(200);
            name.setTypeface(null, Typeface.BOLD);
            name.setPadding(30,0,0,0);
            name.setTextSize(16);
            name.setText(cursor.getString(1));

            //UNIT
            TextView lblUnit = new TextView(tableLayout.getContext());
            lblUnit.setText("Unit");
            TextView unit = new TextView(tableLayout.getContext());
            unit.setMaxWidth(80);
            unit.setPadding(30,0,0,0);
            unit.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
            unit.setText(cursor.getString(2));

            //CRITICAL LEVEL
            TextView lblCriticalLevel = new TextView(tableLayout.getContext());
            lblCriticalLevel.setText("Critical Level");
            TextView critcalLevel = new TextView(tableLayout.getContext());
            critcalLevel.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
            critcalLevel.setTextColor(Color.parseColor("#990000"));
            critcalLevel.setTextSize(16);
            critcalLevel.setPadding(30,0,0,0);
            critcalLevel.setTypeface(null, Typeface.BOLD);
            critcalLevel.setText( cursor.getString(4) );

            //BAL
            TextView lblBalance = new TextView(tableLayout.getContext());
            lblBalance.setText("Balance");
            TextView bal = new TextView(tableLayout.getContext());
            bal.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
            bal.setTextColor(Color.parseColor("#009900"));
            bal.setTextSize(16);
            bal.setPadding(30,0,0,0);
            bal.setTypeface(null, Typeface.BOLD);
            bal.setText( cursor.getString(5) );

            /* code set setOnClickListener */
            code.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String oldCode = ((Button) v).getText().toString();
                    final String name = getValueFromColumn("NAME", oldCode);
                    final String unit = getValueFromColumn("UNIT", oldCode);
                    String strCat = "0";
                    strCat = getValueFromColumn("CATEGORY", oldCode);
                    final int category = Integer.parseInt(strCat);
                    final String cLevel = getValueFromColumn("CRITICAL_LEVEL", oldCode);
                    final String bal = getValueFromColumn("BALANCE", oldCode);

                    final AlertDialog.Builder tBuilder = new AlertDialog.Builder(v.getContext());
                    tBuilder.setTitle("New Transaction");


                    LayoutInflater inflater = LayoutInflater.from(v.getContext());
                    final View transactionView = inflater.inflate(R.layout.transactions_new_editor_layout, null);

                    TextView pcode = (TextView) transactionView.findViewById(R.id.trans_PCode);
                    pcode.setText(oldCode);
                    //type no needed to display by default
                    TextView curBal = (TextView) transactionView.findViewById(R.id.curBal);
                    curBal.setTextColor(Color.parseColor("#009900"));
                    curBal.setText("BALANCE: " + bal);

                    EditText quantity = (EditText) transactionView.findViewById(R.id.trans_quantity);
                    quantity.setText("");

                    tBuilder.setView(transactionView);


                    final String trans_PCode = ((TextView) transactionView.findViewById(R.id.trans_PCode)).getText().toString();
//                            String strQty = ((EditText)transactionView.findViewById(R.id.trans_quantity)).getText().toString();
//                            final int trans_quantity = Integer.parseInt(strQty);
                    final String trans_date = ((TextClock) transactionView.findViewById(R.id.trans_date)).getText().toString();

                    //get RadioButton value on submit
                    final RadioGroup radioGroup = (RadioGroup) transactionView.findViewById(R.id.trans_type_group);
                    radioGroup.check(R.id.OUT);

                    tBuilder.setPositiveButton("SUBMIT", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                                throws NullPointerException {
                            int checkedId = radioGroup.getCheckedRadioButtonId();
                            String trans_date = ((TextClock) transactionView.findViewById(R.id.trans_date)).getText().toString();
                            String strQty = ((EditText) transactionView.findViewById(R.id.trans_quantity)).getText().toString();
                            int trans_quantity = Integer.parseInt(strQty);
                            String trans_type = ((RadioButton) transactionView.findViewById(checkedId)).getText().toString();

                            System.out.println(trans_PCode);
                            System.out.println("rg: " + trans_type);
                            System.out.println("1st: " + trans_quantity);
                            System.out.println("DATE: " + trans_date);

                            String insertTransactionSQL = "INSERT INTO " + tbl_transactions + "" +
                                    "(STOCK, TYPE, QUANTITY, DATE)" +
                                    "VALUES ('" + trans_PCode + "', '" + trans_type + "', " + trans_quantity + ", '" + trans_date + "');";

                            getWritableDatabase().execSQL(insertTransactionSQL);


                            updateStockBalance(transactionView, trans_PCode, trans_type, trans_quantity);
                            int newBal = Integer.parseInt( getValueFromColumn("BALANCE", trans_PCode) );
                            if (!checkSTockCriticalLevel(trans_PCode,newBal)){
                                NM.cancel(notifID);
                            }
                            displayCLStocks(tableLayout);
                        }

                    });

                    tBuilder.show();
                        /* \setPositiveButton */
                    } // \setPositiveButton

            });/* \code.setOnClickListener(new View.OnClickListener() { */


            row.addView(lblCode);
            row.addView(code);
            row.setPadding(0,30,0,0);

            TableRow row2 = new TableRow(tableLayout.getContext());
            row2.addView(lblName);
            row2.addView(name);

            TableRow row3 = new TableRow(tableLayout.getContext());
            row3.addView(lblUnit);
            row3.addView(unit);

            TableRow row4 = new TableRow(tableLayout.getContext());
            row4.addView(lblCriticalLevel);
            row4.addView(critcalLevel);

            TableRow row5 = new TableRow(tableLayout.getContext());
            row5.addView(lblBalance);
            row5.addView(bal);
            row5.setPadding(0,0,0,30);

            TextView Hline = new TextView(tableLayout.getContext());
            Hline.setMinWidth(LinearLayout.LayoutParams.MATCH_PARENT);
            Hline.setMaxHeight(1);
            Hline.setBackgroundColor(Color.LTGRAY);

            tableLayout.addView( row );
            tableLayout.addView( row2 );
            tableLayout.addView( row3 );
            tableLayout.addView( row4 );
            tableLayout.addView( row5 );
            tableLayout.addView( Hline );



            intID++;
        }//endwhile

        if (intID == 0){
            TextView noDisp = new TextView(tableLayout.getContext());
            noDisp.setTextSize(20);
            noDisp.setText("No Critical Level Stocks");
            noDisp.setMinimumHeight(LinearLayout.LayoutParams.MATCH_PARENT);
            noDisp.setMinimumWidth(LinearLayout.LayoutParams.MATCH_PARENT);
            noDisp.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            noDisp.setPadding(0,30,0,0);

            tableLayout.addView(noDisp);
        }
    }
}//endClass
