package com.jackson.andrew.colorcarvesapp;

import android.content.Intent;
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

    private Button BaseSettingConfirm;
    private Button BaseSettingCancel;
    private CheckBox KeepBrightness;
    private CheckBox KeepOffset;
    private SeekBar BrightnessSeekbar;
    private SeekBar OffsetSeekbar;
    private TextView BrightnessDisplay;
    private TextView OffsetDisplay;
    private Payload PayloadOfMessage;
    private byte UnifBright1 = (byte)0xFC; //Set to top 6 bits of data1
    private byte UnifBright2 = (byte)0x0F; //Set to bottom 4 bits of data2
    private byte id = (byte)0x14; //Set to ID of basesetting
    private byte UnifOffset0 = (byte)(0xFF); //data0
    private byte UnifOffset1 = (byte)(0x03); //Set to bottom 2 bits of data1
    private byte[] ByteKeepCurrentBrightness;
    private byte [] ByteKeepCurrentOffset;





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
        PayloadOfMessage = new Payload();




        BrightnessSeekbar.setEnabled(false); // Seekbar is not available unless checkbox is unchecked
        OffsetSeekbar.setEnabled(false); // checkbox must be unchecked

        CheckBrightnessSeekbar(BrightnessSeekbar);
        CheckOffsetSeekbar(OffsetSeekbar);


        BaseSettingConfirm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ByteKeepCurrentOffset = new byte[2];
                ByteKeepCurrentBrightness = new byte[2];


                if(!KeepBrightness.isChecked()) //Brightness has changed
                {
                     intToByteArray(ByteKeepCurrentOffset,BrightnessSeekbar.getProgress()); //from 0 - 100 to 0 - 700

                }
                if(!KeepOffset.isChecked())
                {
                     intToByteArray(ByteKeepCurrentOffset,OffsetSeekbar.getProgress());
                }




                if(KeepBrightness.isChecked())
                {
                    ByteKeepCurrentBrightness[0] = (byte)0xFF;
                    ByteKeepCurrentBrightness[1] = (byte)0xFF;
                }



                if(KeepOffset.isChecked())
                {
                    ByteKeepCurrentOffset[0] = (byte)0xFF;
                    ByteKeepCurrentOffset[1] = (byte)0xFF;
                }

                byte tempByte =  ByteKeepCurrentBrightness[0]; // allows for shifting without losing data
                PayloadOfMessage.id.setId(id);
                PayloadOfMessage.data.setData(2,(byte)((ByteKeepCurrentBrightness[1] << 2) + (tempByte >> 6  & UnifBright2)));  //bits 10 - 7 of Brightness
                PayloadOfMessage.data.setData(1,(byte)(((ByteKeepCurrentBrightness[0] << 2) & UnifBright1)+((ByteKeepCurrentOffset[1]  & UnifOffset1))));  //bits 6-1 of brightness and bits 10,9 of offset
                PayloadOfMessage.data.setData(0,(byte)(ByteKeepCurrentOffset[0] & UnifOffset0));
                CMPPort.getInstance().queueToSend(PayloadOfMessage); // Payload to message queue





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

        double tempBrightness = Brightness/7.65; //Convert from 765 to 0-100%

        BrightnessDisplay.setText(String.valueOf(tempBrightness + "%"));
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

    public void intToByteArray(byte [] val, int data)
    {

        val[0] = (byte)(data & 0xFF);  //discards all but bottom 8 bits of val
        val[1] = (byte)((data >> 8 )); // takes val and discards bottom 8 bits.



    }


}

