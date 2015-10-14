package com.example.nu.taxidriver;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity implements View.OnClickListener {
    Button loginbtn;
    SharedPreferences mSharedPreferences ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        getSupportActionBar().hide();

        loginbtn=(Button) findViewById(R.id.B_login);
        loginbtn.setOnClickListener(this);
        //SharedPreferences mSharedPreferences ;
        mSharedPreferences=getSharedPreferences("MyPref",0);
        //mSharedPreferences.edit().clear().commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        SharedPreferences mySharedPreferences ;
        mySharedPreferences=getSharedPreferences("MyPref",0);
        String mString= mySharedPreferences.getString("email", "");

        if(mString!="") {
            Intent intent = new Intent(this,SetStatusActivity.class);
            startActivity(intent);
            Toast.makeText(MainActivity.this, "You are already login!", Toast.LENGTH_SHORT).show();
            finish();
        }
        else{
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            Toast.makeText(MainActivity.this, "Login!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("EXIT")
                .setMessage("Are you sure you want to Exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();


    }
}
