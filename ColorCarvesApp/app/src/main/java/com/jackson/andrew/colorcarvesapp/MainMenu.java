package com.jackson.andrew.colorcarvesapp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainMenu extends AppCompatActivity {

    public Button ToLEDScreen;
    public Button ToAngleScreen;
    public Button ToSpeedScreen;
    public Button ToBoardSpecs;
    public Button ToBaseSetting;
    public Button ToEnableOptions;

    private BlueLowEnergyAdapter mLeDeviceListAdapter;
    private BluetoothAdapter mBluetoothAdapter;
    private boolean mScanning;
    private Handler mHandler;

    public static final String DEVICE_NAME = "DEVICE_NAME";
    public static final String DEVICE_ADDRESS = "DEVICE_ADDRESS";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);




        ToAngleScreen = (Button) findViewById(R.id.ToAngleScreen);
        ToSpeedScreen = (Button) findViewById(R.id.ToSpeedScreen);
        ToLEDScreen = (Button) findViewById(R.id.ToLEDScreen);
        ToBaseSetting = (Button) findViewById(R.id.ToBaseSettingScreen);
        ToBoardSpecs = (Button) findViewById(R.id.ToBoardSpecs);
        ToEnableOptions = (Button) findViewById(R.id.ToEnableOptions);




        ToSpeedScreen.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                GoToSpeedSetting();
            }
        });

        ToAngleScreen.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                GoToAngleSet();
            }
        });

        ToLEDScreen.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                GoToLedSetting();
            }
        });


        ToEnableOptions.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                GoToEnableSetting();
            }
        });

        ToBoardSpecs.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                GoToBoardSpecs();
            }
        });


        ToBaseSetting.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                GoToBaseSetting();
            }
        });
    }


    protected void onListItemClick(ListView l, View v, int position, long id) {
        final BluetoothDevice device = mLeDeviceListAdapter.getDevice(position);
        if (device == null) return;
        final Intent intent = new Intent(this, MainMenu.class);
        intent.putExtra(MainMenu.DEVICE_NAME, device.getName());
        intent.putExtra(MainMenu.DEVICE_ADDRESS, device.getAddress());
        if (mScanning) {
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
            mScanning = false;
        }
        startActivity(intent);
    }






    public void GoToLedSetting (){
        Intent myIntent = new Intent(MainMenu.this, LEDSettingScreen.class);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

        MainMenu.this.startActivity(myIntent);

    }

    public void GoToAngleSet (){
        Intent myIntent = new Intent(MainMenu.this, AngleSettingScreen.class);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

        MainMenu.this.startActivity(myIntent);

    }

    public void GoToSpeedSetting (){
        Intent myIntent = new Intent(MainMenu.this, SpeedSettingScreen.class);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

        MainMenu.this.startActivity(myIntent);

    }

    public void GoToBaseSetting (){
        Intent myIntent = new Intent(MainMenu.this, BaseSettingScreen.class);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

        MainMenu.this.startActivity(myIntent);

    }

    public void GoToBoardSpecs (){
        Intent myIntent = new Intent(MainMenu.this, BoardSpecsScreen.class);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

        MainMenu.this.startActivity(myIntent);

    }

    public void GoToEnableSetting(){
        Intent myIntent = new Intent(MainMenu.this, EnableOptionsSettingScreen.class);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

        MainMenu.this.startActivity(myIntent);

    }

    private class BlueLowEnergyAdapter extends BaseAdapter {
        private ArrayList<BluetoothDevice>mLeDevices;
        private LayoutInflater mInflator;

        public BlueLowEnergyAdapter() {
            super();
            mLeDevices = new ArrayList<BluetoothDevice>();
            mInflator = MainMenu.this.getLayoutInflater();
        }

        public void addDevice(BluetoothDevice device) {
            if(!mLeDevices.contains(device)) {
                mLeDevices.add(device);
            }
        }

        public BluetoothDevice getDevice(int position) {
            return mLeDevices.get(position);
        }

        public void clear() {
            mLeDevices.clear();
        }

        @Override
        public int getCount() {
            return mLeDevices.size();
        }

        @Override
        public Object getItem(int i) {
            return mLeDevices.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            // General ListView optimization code.
            if (view == null) {
                view = mInflator.inflate(R.layout.bluedevices, null);
                viewHolder = new ViewHolder();
                viewHolder.deviceAddress = (TextView) view.findViewById(R.id.device_address);
                viewHolder.deviceName = (TextView) view.findViewById(R.id.device_name);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }

            BluetoothDevice device = mLeDevices.get(i);
            final String deviceName = device.getName();
            if (deviceName != null && deviceName.length() > 0)
                viewHolder.deviceName.setText(deviceName);
            else
                viewHolder.deviceName.setText("Unknown Device");
            viewHolder.deviceAddress.setText(device.getAddress());

            return view;
        }
    }

    // Device scan callback.
    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {

                @Override
                public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mLeDeviceListAdapter.addDevice(device);
                            mLeDeviceListAdapter.notifyDataSetChanged();
                        }
                    });
                }
            };

    static class ViewHolder {
        TextView deviceName;
        TextView deviceAddress;
    }



}
