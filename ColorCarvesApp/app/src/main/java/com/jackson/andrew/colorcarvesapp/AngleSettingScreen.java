package com.jackson.andrew.colorcarvesapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;
import android.view.View;

public class AngleSettingScreen extends AppCompatActivity {

    private CheckBox checkboxKeepAngleCurrentSetting;
    private CheckBox checkboxAngleAbsoluteMode;
    private Button AngleConfirm;
    private Button AngleCancel;
    private SeekBar AngleLimitSeekBar;
    private TextView AngleLimitDisplay;
    private CheckBox checkboxKeepAbsoluteMode;
    private int DefaultAngleSetting = 0;
    private Payload payload;
    private byte id = (byte) 0x11; // Message protocol
    private byte absoluteMode = (byte) 0x03; // dont care in message protocol
    private byte anglesetData1 = (byte) 0x03; // bottom two bits of data 1 set to on
    private byte anglesetData0 = (byte) 0xFF; // data zero set to all ones
    private byte[] angleLimit;









    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_angle_setting_screen);


        checkboxAngleAbsoluteMode = (CheckBox) findViewById(R.id.AngleAbsoluteMode);
        checkboxKeepAngleCurrentSetting = (CheckBox) findViewById(R.id.AngleCurrentSetting);
        AngleConfirm = (Button) findViewById(R.id.AngleConfirm);
        AngleCancel = (Button) findViewById(R.id.AngleCancel);
        AngleLimitSeekBar = (SeekBar) findViewById(R.id.AngleLimitSeekBar);
        AngleLimitDisplay = (TextView) findViewById(R.id.AngleLimitDisplay);
        checkboxKeepAbsoluteMode = (CheckBox) findViewById(R.id.KeepAbsoluteMode);
        AngleLimitSeekBar.setEnabled(false);        //Grayed out when page is opened
        checkboxAngleAbsoluteMode.setEnabled(false);
        payload = new Payload();


        AngleLimitDisplay.setText(String.valueOf(DefaultAngleSetting));  //Initializes to 0

        checkMySeekBar(AngleLimitSeekBar);  //Determine if the Angle should be adjusted

        angleLimit = new byte[2];
        angleLimit[1] = (byte) 0xFF;
        angleLimit[0] = (byte) 0xFF; // set up a 16 bits of data to capture angle set



        checkboxKeepAngleCurrentSetting.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (checkboxKeepAngleCurrentSetting.isChecked()) {
                    AngleLimitSeekBar.setEnabled(false);
                } else {
                    AngleLimitSeekBar.setEnabled(true);
                }
            }
        });
        checkboxKeepAbsoluteMode.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                checkStatusOfCheckbox(checkboxKeepAbsoluteMode);
            }
        });


        AngleConfirm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (!checkboxKeepAbsoluteMode.isChecked() && checkboxAngleAbsoluteMode.isChecked()) {
                    absoluteMode = (byte) 0x01; //absolute mode is on
                }
                if (!checkboxKeepAbsoluteMode.isChecked() && !checkboxAngleAbsoluteMode.isChecked())
                {
                    absoluteMode = (byte) 0x00; //absolute mode is turned off
                }

                if (!checkboxKeepAngleCurrentSetting.isChecked()) {
                     intToByteArray(angleLimit,AngleLimitSeekBar.getProgress());
                }


                 payload.id.setId(id);
                 payload.data.setData(2, absoluteMode);
                 payload.data.setData(1, (byte) (anglesetData1 & angleLimit[1]));
                 payload.data.setData(0, (byte) (angleLimit[0] & anglesetData0));
                CMPPort.getInstance().queueToSend(payload); // check to see if a message port has already been called




                returnToMainMenu();
            }
        });


        AngleCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                returnToMainMenu();
            }
        });


    }

    public void checkMySeekBar(final SeekBar mSeekBar) {


        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {


            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                displayAngleLimit(mSeekBar.getProgress());
            }
        });
    }


    public void checkStatusOfCheckbox(CheckBox mcheckbox)
    {

        if (mcheckbox.isChecked())
        {
            checkboxAngleAbsoluteMode.setEnabled(false);

        }

        if (!mcheckbox.isChecked())
        {
            checkboxAngleAbsoluteMode.setEnabled(true);
        }
    }


    public void displayAngleLimit(int progress)
    {
        float AngleLimit = progress;
        AngleLimitDisplay.setText(String.valueOf(AngleLimit / 10) + " deg");
    }

    public void returnToMainMenu()
    {
        Intent myIntent = new Intent(AngleSettingScreen.this, MainMenu.class);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        AngleSettingScreen.this.startActivity(myIntent);

    }

    public void intToByteArray(byte [] val, int data)
    {

        val[0] = (byte)(data);  //discards all but bottom 8 bits of val
        val[1] = (byte)((data >> 8 )); // takes val and discards bottom 8 bits.



    }
}

