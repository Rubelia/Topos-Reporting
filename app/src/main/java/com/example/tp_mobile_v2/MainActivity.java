package com.example.tp_mobile_v2;

import android.app.Activity;
import android.app.AlertDialog;
import android.database.SQLException;
import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.tp_mobile_v2.AsynTask.Intance_AsyncTask;
import com.example.tp_mobile_v2.Object.NhanVien;
import com.example.tp_mobile_v2.database.SQLController;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    Activity activity = this;
    SQLController sqlController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sqlController = new SQLController(activity);
        sqlController.instance();
        sqlController.open();
        try {
            if(sqlController.checkIP()) {
                //don't do anything
//            Log.d("Main", "Run here");
            } else sqlController.insertIP();
        } catch (SQLException err) {
            ArrayList<String> arr_err = new ArrayList<>();
            arr_err.add(err.getMessage());
            showNotice(arr_err);
        }
        sqlController.close();
        NhanVien nhanVien = new NhanVien();
        Intance_AsyncTask intance_asyncTask = new Intance_AsyncTask(activity,nhanVien,0);
        intance_asyncTask.execute();

//        Intent ds_group_view = new Intent (getApplicationContext(), Login.class);
//		startActivity(ds_group_view);
//		finish();
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
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void showNotice (ArrayList<String> str) {
        String outStr = "";
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        for(int i=0;i<str.size();i++) {
            if(i== str.size()-1) {
                outStr += str.get(i);
            } else outStr += str.get(i) + " /n";
        }
        builder.setMessage(outStr);
        AlertDialog alert = builder.create();
        alert.show();
    }
}
