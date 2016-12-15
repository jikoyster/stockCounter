package com.example.jake_laptop.navigationdrawer;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    /* NOTE: -notification and critical level is ok
            - need to create another layout for critical stocks only
    */
    android.app.FragmentManager fragmentManager = getFragmentManager();

    DB_Controller controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Stocks on Critical Level");

        controller = new DB_Controller(this, "", null, 1);
        DB_Controller.NM = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        fragmentManager.beginTransaction()
                .replace(R.id.content_frame,  new CriticalLevelFragment() )
                .commit();


//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        switch(id){
            case R.id.nav_transactions:
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame,
                                new TransactionsFragment())
                        .commit();
                getSupportActionBar().setTitle("Transactions");

                break;
            case R.id.nav_stocks:
                // Replace the contents of the container with the new fragment
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame,  new StocksFragment())
                        .commit();

                getSupportActionBar().setTitle("Stocks Management");
                break;
            case R.id.nav_newStock:
                // Replace the contents of the container with the new fragment
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame,  new StocksEditorFragment())
                        .commit();

                getSupportActionBar().setTitle("Add New Stock");
                break;
            case R.id.nav_category:
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame,  new CategoryFragment())
                        .commit();

                getSupportActionBar().setTitle("Category");
                break;
            case R.id.nav_newCategory:
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame,  new CategoryEditorFragment())
                        .commit();

                getSupportActionBar().setTitle("Add New Category");
                break;
            case R.id.nav_critical_level:
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame,  new CriticalLevelFragment())
                        .commit();

                getSupportActionBar().setTitle("Displaying CL Stocks");
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }//onNavigationItemSelected


//    public void btn_click(View view){
//
//        TextView textView = (TextView) findViewById(R.id.textView);
//
//        controller = new DB_Controller(this, "", null, 1);
//
//
//        switch(view.getId()){
//            case R.id.btn_addStock:
//            case R.id.btn_add:
//                System.out.println("clicked: Button Add");
//
//                String  code        = ((EditText) findViewById(R.id.etCode)).getText().toString(),
//                        stockName   = ((EditText) findViewById(R.id.etName)).getText().toString(),
//                        unit        = ((EditText) findViewById(R.id.etUnit)).getText().toString(),
//                        date        = ((EditText) findViewById(R.id.date_field)).getText().toString();
//                int     begBal      = Integer.parseInt( ((EditText) findViewById(R.id.etBal)).getText().toString() );
//                System.out.println(code + stockName + unit + begBal + date);
//
//                try{
//                    controller.addStock( code, stockName, unit, begBal, date);
//                }catch(SQLiteException e){
//                    Toast.makeText(MainActivity.this, "ALREADY EXISTS", Toast.LENGTH_SHORT).show();
//                }
//
//
//                break;
//            case R.id.button_delete:
//                System.out.println("clicked: Button Delete");
//                controller.delete_student("##stockName");
//                break;
//            case R.id.button_update:
//                System.out.println("clicked: Button Update");
//                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
//                dialog.setTitle("Enter New Firstname:");
//                final EditText new_firstname = new EditText(this);
//                dialog.setView(new_firstname);
//
//                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        controller.update_student("##stockName",
//                                new_firstname.getText().toString());
//                    }
//                });
//
//                dialog.show();
//
//                break;
//            case R.id.list_student:
//                System.out.println("clicked: Button List");
//                controller.list_all_stocks(textView);
//                break;
//        }
//    }

    public void btn_addNewStock(View view){


        String  code        = ((EditText) findViewById(R.id.etCode)).getText().toString(),
                stockName   = ((EditText) findViewById(R.id.etName)).getText().toString(),
                unit        = ((EditText) findViewById(R.id.etUnit)).getText().toString(),
                date        = ((TextClock) findViewById(R.id.date_field)).getText().toString();
        int     catID = 0, begBal = 0, cLevel = 0;

        try{
            catID       = ((Spinner)findViewById(R.id.spinnerCategory)).getSelectedItemPosition();
            begBal      = Integer.parseInt( ((EditText) findViewById(R.id.etBal)).getText().toString() );
            cLevel      = Integer.parseInt( ((EditText) findViewById(R.id.etCriticalLevel)).getText().toString() );
        }catch(NumberFormatException e){
            Toast.makeText(MainActivity.this, "WRONG FORMAT", Toast.LENGTH_SHORT).show();
        }

        try{
            controller.addStock( code, stockName, unit, catID, cLevel, begBal, date);
            Toast.makeText(MainActivity.this, "ADDED NEW STOCK \""+stockName+"\"", Toast.LENGTH_SHORT).show();
        }catch(SQLiteException e){
            Toast.makeText(MainActivity.this, "ALREADY EXISTS", Toast.LENGTH_SHORT).show();
        }catch(NullPointerException e){
            Toast.makeText(MainActivity.this, "NullPointerException", Toast.LENGTH_SHORT).show();
        }
    }

    public void btn_addNewCategory(View view){
        controller = new DB_Controller(this, "", null, 1);
        String  name = ((EditText) findViewById(R.id.etCatName)).getText().toString();
        try{
            controller.addCategory(name);
            Toast.makeText(MainActivity.this, "ADDED NEW CATEGORY \""+name+"\"", Toast.LENGTH_SHORT).show();
        }catch(SQLiteException e){
            Toast.makeText(MainActivity.this, "ALREADY EXISTS", Toast.LENGTH_SHORT).show();
        }
    }

}//class
