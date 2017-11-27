package com.jackson.andrew.colorcarvesapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;

public class BaseSettingScreen extends AppCompatActivity {

    private Button BaseSettingConfirm;
    private Button BaseSettingCancel;
    private CheckBox checkboxKeepBrightness;
    private CheckBox checkboxKeepOffset;
    private SeekBar BrightnessSeekbar;
    private SeekBar OffsetSeekbar;
    private TextView BrightnessDisplay;
    private TextView OffsetDisplay;
    private Payload payload;
    private byte UnifBright1 = (byte)0xFC; //Set to top 6 bits of data1
    private byte UnifBright2 = (byte)0x0F; //Set to bottom 4 bits of data2
    private byte id = (byte)0x14; //Set to ID of basesetting message
    private byte UnifOffset0 = (byte)(0xFF); //data0
    private byte UnifOffset1 = (byte)(0x03); //Set to bottom 2 bits of data1
    private byte[] ByteKeepCurrentBrightness; // used to capture brightness setting from user
    private byte [] ByteKeepCurrentOffset; // captures offset of user





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_setting_screen);

        checkboxKeepBrightness = (CheckBox) findViewById(R.id.KeepBrightnessSetting);
        checkboxKeepOffset = (CheckBox) findViewById(R.id.KeepOffsetSetting);
        BaseSettingConfirm = (Button) findViewById(R.id.BaseSettingConfirm);
        BaseSettingCancel = (Button) findViewById(R.id.BaseSettingCancel);
        BrightnessSeekbar = (SeekBar) findViewById(R.id.BrightnessSeekbar);
        OffsetSeekbar = (SeekBar) findViewById(R.id.OffsetSeekbar);
        BrightnessDisplay = (TextView) findViewById(R.id.BrightnessDisplay);
        OffsetDisplay= (TextView) findViewById(R.id.OffsetDisplay);
        payload = new Payload();




        BrightnessSeekbar.setEnabled(false); // Seekbar is not available unless checkbox is unchecked
        OffsetSeekbar.setEnabled(false); // checkbox must be unchecked

        checkBrightnessSeekbar(BrightnessSeekbar);
        checkOffsetSeekbar(OffsetSeekbar);


        BaseSettingConfirm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ByteKeepCurrentOffset = new byte[2];
                ByteKeepCurrentBrightness = new byte[2];


                if(!checkboxKeepBrightness.isChecked()) //Brightness has changed
                {
                     intToByteArray(ByteKeepCurrentBrightness,BrightnessSeekbar.getProgress()); //from 0 - 100 to 0 - 700

                }
                if(!checkboxKeepOffset.isChecked())
                {
                     intToByteArray(ByteKeepCurrentOffset,OffsetSeekbar.getProgress());
                }




                if(checkboxKeepBrightness.isChecked())
                {
                    ByteKeepCurrentBrightness[0] = (byte)0xFF;
                    ByteKeepCurrentBrightness[1] = (byte)0xFF;
                }



                if(checkboxKeepOffset.isChecked())
                {
                    ByteKeepCurrentOffset[0] = (byte)0xFF;
                    ByteKeepCurrentOffset[1] = (byte)0xFF;
                }

                byte tempByte =  ByteKeepCurrentBrightness[0]; // allows for shifting without losing data
                payload.id.setId(id);
                payload.data.setData(2,(byte)(((ByteKeepCurrentBrightness[1] << 2) | (tempByte >> 6))  & UnifBright2));  //bits of bright[1] shifted to 3 and 4 of data. temp byte takes top 2 bits from bright[0] moves them to bits 1 and 2 of data
                payload.data.setData(1,(byte)((((ByteKeepCurrentBrightness[0] << 2) & UnifBright1))|(ByteKeepCurrentOffset[1]  & UnifOffset1)));  //bits 6-1 of brightness and bits 10,9 of offset
                payload.data.setData(0,(byte)(ByteKeepCurrentOffset[0] & UnifOffset0));
                CMPPortTx.getInstance().queueToSend(payload); // Payload to message queue





                ReturnToMainMenu();
            }
        });

        BaseSettingCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                ReturnToMainMenu();
            }
        });


        checkboxKeepBrightness.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                checkStatusOfBrightnessCheckbox(checkboxKeepBrightness);
            }
        });

        checkboxKeepOffset.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                checkStatusOfOffsetCheckbox(checkboxKeepOffset);
            }
        });

    }








    public void ReturnToMainMenu(){
        Intent myIntent = new Intent(BaseSettingScreen.this, MainMenu.class);

        BaseSettingScreen.this.startActivity(myIntent);

    }

    public void displayBrightness(int Brightness) {

        double tempBrightness = Brightness/7.65; //Convert from 765 to 0-100%



        BrightnessDisplay.setText(String.valueOf(((int)(tempBrightness) ) + "%")); //Int casting to display % as int
    }

    public void displayOffset(int offset){

        OffsetDisplay.setText(String.valueOf(offset));
    }




    public void checkBrightnessSeekbar(final SeekBar mSeekBar) {


        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {




            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                displayBrightness(mSeekBar.getProgress());
            }
        });
    }


    public void checkOffsetSeekbar(final SeekBar mSeekBar) {


        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {




            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                displayOffset(mSeekBar.getProgress());
            }
        });
    }

    public void checkStatusOfBrightnessCheckbox(CheckBox mcheckbox){

        if (mcheckbox.isChecked()){

            BrightnessSeekbar.setEnabled(false);
        }
        else{
            BrightnessSeekbar.setEnabled(true); // Only enable brightness if keepbrightness is not checked
        }
    }

    public void checkStatusOfOffsetCheckbox(CheckBox mcheckbox){
        if(mcheckbox.isChecked()){
            OffsetSeekbar.setEnabled(false);
        }
        else{
            OffsetSeekbar.setEnabled(true); // Only enable offset change if keepoffset is not checked
        }
    }

    public void intToByteArray(byte [] val, int data)
    {

        val[0] = (byte)(data);  //discards all but bottom 8 bits of val
        val[1] = (byte)((data >> 8 )); // takes val and discards bottom 8 bits.



    }


}

