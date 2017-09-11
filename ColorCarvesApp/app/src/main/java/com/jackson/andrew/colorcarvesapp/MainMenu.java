package com.jackson.andrew.colorcarvesapp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.nfc.Tag;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class MainMenu extends AppCompatActivity{


    private BlueToothLowEnergyService mBluetoothLeService;

    private boolean mConnected = false;
    private BluetoothGattCharacteristic characteristicTX;
    private BluetoothGattCharacteristic characteristicRX;


    private String mDeviceName;
    private String mDeviceAddress;

    private BlueLowEnergyAdapter mLeDeviceListAdapter;
    private BluetoothAdapter mBluetoothAdapter;
    private boolean mScanning;
    private Handler mHandler;

    public Button ToLEDScreen;
    public Button ToAngleScreen;
    public Button ToSpeedScreen;
    public Button ToBoardSpecs;
    public Button ToBaseSetting;
    public Button ToEnableOptions;
    public TextView DeviceAdressDisplay;
    public MessageQueue AppMessageQueue;

    public static final String DEVICE_NAME = "DEVICE_NAME";
    public static final String DEVICE_ADDRESS = "DEVICE_ADDRESS";


        public final static UUID HM_RX_TX =
                UUID.fromString(SampleGattAttributes.HM_RX_TX);

        private final String LIST_NAME = "NAME";
        private final String LIST_UUID = "UUID";

        // Code to manage Service lifecycle.
        private final ServiceConnection mServiceConnection = new ServiceConnection() {

            @Override
            public void onServiceConnected(ComponentName componentName, IBinder service) {
                mBluetoothLeService = ((BlueToothLowEnergyService.LocalBinder) service).getService();
                if (!mBluetoothLeService.initialize()) {
                   Log.d("error on service","Unable to connect to bluetooth");
                    finish();
                }
                // Automatically connects to the device upon successful start-up initialization.
                mBluetoothLeService.connect(mDeviceAddress);
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                mBluetoothLeService = null;
            }
        };

        // Handles various events fired by the Service.
        // ACTION_GATT_CONNECTED: connected to a GATT server.
        // ACTION_GATT_DISCONNECTED: disconnected from a GATT server.
        // ACTION_GATT_SERVICES_DISCOVERED: discovered GATT services.
        // ACTION_DATA_AVAILABLE: received data from the device.  This can be a result of read
        //                        or notification operations.
        private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                final String action = intent.getAction();
                if (BlueToothLowEnergyService.ACTION_GATT_CONNECTED.equals(action)) {
                    mConnected = true;

                    invalidateOptionsMenu();
                } else if (BlueToothLowEnergyService.ACTION_GATT_DISCONNECTED.equals(action)) {
                    mConnected = false;

                    invalidateOptionsMenu();
                    clearUI();
                } else if (BlueToothLowEnergyService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                    // Show all the supported services and characteristics on the user interface.
                    displayGattServices(mBluetoothLeService.getSupportedGattServices());


                } else if (BlueToothLowEnergyService.ACTION_DATA_AVAILABLE.equals(action)) {
                    displayData(intent.getStringExtra(mBluetoothLeService.EXTRA_DATA));
                }

            }
        };


            private void clearUI() {
                // mDataField.setText(R.string.no_data);
            }







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        final Intent intent = getIntent();
        mDeviceName = intent.getStringExtra(DEVICE_NAME);
        mDeviceAddress = intent.getStringExtra(DEVICE_ADDRESS);
        Intent gattServiceIntent = new Intent(this, BluetoothScanActivity.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);

        ToAngleScreen = (Button) findViewById(R.id.ToAngleScreen);
        ToSpeedScreen = (Button) findViewById(R.id.ToSpeedScreen);
        ToLEDScreen = (Button) findViewById(R.id.ToLEDScreen);
        ToBaseSetting = (Button) findViewById(R.id.ToBaseSettingScreen);
        ToBoardSpecs = (Button) findViewById(R.id.ToBoardSpecs);
        ToEnableOptions = (Button) findViewById(R.id.ToEnableOptions);
        DeviceAdressDisplay=(TextView)findViewById(R.id.DeviceAddressDisplay);





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


    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        if (mBluetoothLeService != null) {
            final boolean result = mBluetoothLeService.connect(mDeviceAddress);
            Log.d("Connection", "Connect request result=" + result);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mGattUpdateReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);
        mBluetoothLeService = null;
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



    // Demonstrates how to iterate through the supported GATT Services/Characteristics.
    // In this sample, we populate the data structure that is bound to the ExpandableListView
    // on the UI.
    private void displayGattServices(List<BluetoothGattService> gattServices) {
        if (gattServices == null) return;
        String uuid = null;
        String unknownServiceString =("Unknown Device");
        ArrayList<HashMap<String, String>> gattServiceData = new ArrayList<HashMap<String, String>>();


        // Loops through available GATT Services.
        for (BluetoothGattService gattService : gattServices) {
            HashMap<String, String> currentServiceData = new HashMap<String, String>();
            uuid = gattService.getUuid().toString();
            currentServiceData.put(
                    LIST_NAME, SampleGattAttributes.lookup(uuid, unknownServiceString));

            // If the service exists for HM 10 Serial, say so.

            currentServiceData.put(LIST_UUID, uuid);
            gattServiceData.add(currentServiceData);

            // get characteristic when UUID matches RX/TX UUID
            characteristicTX = gattService.getCharacteristic(BlueToothLowEnergyService.UUID_HM_RX_TX);
            characteristicRX = gattService.getCharacteristic(BlueToothLowEnergyService.UUID_HM_RX_TX);
        }

    }

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BlueToothLowEnergyService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BlueToothLowEnergyService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BlueToothLowEnergyService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BlueToothLowEnergyService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }

    private void displayData(String data) {

        if (data != null) {
            DeviceAdressDisplay.setText(data);
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


    public void SendMessage(byte[] UserMessage){

        if (mConnected) {
            characteristicTX.setValue(UserMessage);
            mBluetoothLeService.writeCharacteristic(characteristicTX);
            mBluetoothLeService.setCharacteristicNotification(characteristicRX, true);


        }
    }



}
