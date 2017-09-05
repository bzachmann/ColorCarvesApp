package com.jackson.andrew.colorcarvesapp;

import android.app.Activity;
import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {


    public Button ToMainMenu;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ToMainMenu = (Button) findViewById(R.id.ScanButton);




        ToMainMenu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                StartBluetoothScan();
            }
        });
    }





public void StartBluetoothScan() {


    Intent myIntent = new Intent(MainActivity.this, BluetoothScanActivity.class);

    MainActivity.this.startActivity(myIntent);

}
}


