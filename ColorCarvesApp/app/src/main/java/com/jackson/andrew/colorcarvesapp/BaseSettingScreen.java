package com.jackson.andrew.colorcarvesapp;

import android.content.Intent;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class BaseSettingScreen extends AppCompatActivity {

    public Button BaseSettingConfirm;
    public Button BaseSettingCancel;
    public CheckBox KeepBrightness;
    public CheckBox KeepOffset;
    public SeekBar BrightnessSeekbar;
    public SeekBar OffsetSeekbar;
    public TextView BrightnessDisplay;
    public TextView OffsetDisplay;
    public Message MessageToSend;
    public ID IdOFMessage;
    public Data DataOfMessage;
    public Payload PayloadOfMessage;
    public byte UnifBright1 = (byte)0xFC;
    public byte UnifBright2 = (byte) 0x0F;
    public MainMenu mm;
    public CMPPort MessagePort;
    private MyBackgroundThread mMyBackgroundThread;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_setting_screen);

        KeepBrightness = (CheckBox) findViewById(R.id.KeepBrightnessSetting);
        KeepOffset = (CheckBox) findViewById(R.id.KeepOffsetSetting);
        BaseSettingConfirm = (Button) findViewById(R.id.BaseSettingConfirm);
        BaseSettingCancel = (Button) findViewById(R.id.BaseSettingCancel);
        BrightnessSeekbar = (SeekBar) findViewById(R.id.BrightnessSeekbar);
        OffsetSeekbar = (SeekBar) findViewById(R.id.OffsetSeekbar);
        BrightnessDisplay = (TextView) findViewById(R.id.BrightnessDisplay);
        OffsetDisplay= (TextView) findViewById(R.id.OffsetDisplay);
        IdOFMessage = new ID();
        DataOfMessage = new Data();
        PayloadOfMessage = new Payload();
        MessagePort = new CMPPort();
        MessageToSend = new Message();
        if (mMyBackgroundThread == null) { // only start one thread at a time  //Not sure where to start the thread
            startBackgroundThread();
            Log.d("Thread","Creating Background thread");
        }




        BrightnessSeekbar.setEnabled(false); // Seekbar is not available unless checkbox is unchecked
        OffsetSeekbar.setEnabled(false); // checkbox must be unchecked

        CheckBrightnessSeekbar(BrightnessSeekbar);
        CheckOffsetSeekbar(OffsetSeekbar);


        BaseSettingConfirm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if(KeepBrightness.isChecked()){
                   IdOFMessage.setIdByte((byte)0x14);
                    DataOfMessage.SetData(2,(byte)(0x00&(UnifBright1)));
                    DataOfMessage.SetData(3,(byte)(0x00));
                }
                if(KeepOffset.isChecked()){
                    DataOfMessage.SetData(1,(byte)(0x01)); //Will change keep for testing purposes
                }
                PayloadOfMessage.SetPayload(DataOfMessage,IdOFMessage);
                MessageToSend.SetMessagePayload(PayloadOfMessage);

                MessagePort.PackMesage(MessageToSend);




                ReturnToMainMenu();
            }
        });

        BaseSettingCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                ReturnToMainMenu();
            }
        });


        KeepBrightness.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CheckStatusOfBrightness(KeepBrightness);
            }
        });

        KeepOffset.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CheckStatusOfOffset(KeepOffset);
            }
        });

    }








    public void ReturnToMainMenu(){
        Intent myIntent = new Intent(BaseSettingScreen.this, MainMenu.class);

        BaseSettingScreen.this.startActivity(myIntent);

    }

    public void DisplayBrightness(int Brightness) {

        BrightnessDisplay.setText(String.valueOf(Brightness + "%"));  // Display wheel diam divide by 10 to convert to decimal
    }

    public void DisplayOffset (int offset){

        OffsetDisplay.setText(String.valueOf(offset));
    }




    public void CheckBrightnessSeekbar(final SeekBar mSeekBar) {


        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {




            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                DisplayBrightness(mSeekBar.getProgress());
            }
        });
    }


    public void CheckOffsetSeekbar(final SeekBar mSeekBar) {


        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {




            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                DisplayOffset(mSeekBar.getProgress());
            }
        });
    }

    public void CheckStatusOfBrightness(CheckBox mcheckbox){

        if (mcheckbox.isChecked()){

            BrightnessSeekbar.setEnabled(false);
        }
        else{
            BrightnessSeekbar.setEnabled(true);
        }
    }

    public void CheckStatusOfOffset(CheckBox mcheckbox){
        if(mcheckbox.isChecked()){
            OffsetSeekbar.setEnabled(false);
        }
        else{
            OffsetSeekbar.setEnabled(true);
        }
    }

    private void startBackgroundThread() {
        mMyBackgroundThread = new MyBackgroundThread(); // Pass the Queue to the thread
        mMyBackgroundThread.start();
        Toast.makeText(this, "starting...", Toast.LENGTH_SHORT).show();

    }

    private void stopBackgroundThread() {
        if (mMyBackgroundThread != null) {
            mMyBackgroundThread = null; // thread is now dead, we need to free from memory
            Toast.makeText(this, "stopping...", Toast.LENGTH_SHORT).show();

        }
    }
}

