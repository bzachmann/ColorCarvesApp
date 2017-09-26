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

    private CheckBox keepAngleCurrentSetting;
    private CheckBox AngleAbsoluteMode;
    private Button AngleConfirm;
    private Button AngleCancel;
    private SeekBar AngleLimitSeekBar;
    private TextView AngleLimitDisplay;
    private CheckBox KeepAbsoluteMode;
    private int DefaultAngleSetting = 0;
    private Payload messagePayload;
    private byte id;
    private byte absoluteMode;
    private byte anglesetData1;
    private byte anglesetData0;
    private byte[] angleLimit;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_angle_setting_screen);


        AngleAbsoluteMode = (CheckBox) findViewById(R.id.AngleAbsoluteMode);
        keepAngleCurrentSetting = (CheckBox) findViewById(R.id.AngleCurrentSetting);
        AngleConfirm = (Button) findViewById(R.id.AngleConfirm);
        AngleCancel = (Button) findViewById(R.id.AngleCancel);
        AngleLimitSeekBar = (SeekBar) findViewById(R.id.AngleLimitSeekBar);
        AngleLimitDisplay = (TextView) findViewById(R.id.AngleLimitDisplay);
        KeepAbsoluteMode = (CheckBox) findViewById(R.id.KeepAbsoluteMode);
        AngleLimitSeekBar.setEnabled(false);        //Grayed out when page is opened
        AngleAbsoluteMode.setEnabled(false);
        messagePayload = new Payload();


        AngleLimitDisplay.setText(String.valueOf(DefaultAngleSetting));  //Initializes to 0

        CheckMySeekBar(AngleLimitSeekBar);  //Determine if the Angle should be adjusted

        absoluteMode = (byte) 0x03;  // Dont care in protocol
        id = (byte) 0x11;
        anglesetData1 = (byte) 0x03;
        anglesetData0 = (byte) 0xFF;
        angleLimit = new byte[2];
        angleLimit[1] = (byte) 0xFF;
        angleLimit[0] = (byte) 0xFF;


        keepAngleCurrentSetting.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (keepAngleCurrentSetting.isChecked()) {
                    AngleLimitSeekBar.setEnabled(false);
                } else {
                    AngleLimitSeekBar.setEnabled(true);
                }
            }
        });
        KeepAbsoluteMode.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CheckStatusOfSetting(KeepAbsoluteMode);
            }
        });


        AngleConfirm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (!KeepAbsoluteMode.isChecked() && AngleAbsoluteMode.isChecked()) {
                    absoluteMode = (byte) 0x01; //absolute mode is on
                }

                if (!keepAngleCurrentSetting.isChecked()) {
                     intToByteArray(angleLimit,AngleLimitSeekBar.getProgress());
                }


                messagePayload.id.setId(id);
                messagePayload.data.setData(2, absoluteMode);
                messagePayload.data.setData(1, (byte) (anglesetData1 & angleLimit[1]));
                messagePayload.data.setData(0, (byte) (angleLimit[0] & anglesetData0));
                CMPPort.getInstance().queueToSend(messagePayload);




                ReturnToMainMenu();
            }
        });


        AngleCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ReturnToMainMenu();
            }
        });


    }

    public void CheckMySeekBar(final SeekBar mSeekBar) {


        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {


            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                DisplayAngleLimit(mSeekBar.getProgress());
            }
        });
    }


    public void CheckStatusOfSetting(CheckBox mcheckbox)
    {

        if (mcheckbox.isChecked())
        {
            AngleAbsoluteMode.setEnabled(false);

        }

        if (!mcheckbox.isChecked())
        {
            AngleAbsoluteMode.setEnabled(true);
        }
    }


    public void DisplayAngleLimit(int progress)
    {
        float AngleLimit = progress;
        AngleLimitDisplay.setText(String.valueOf(AngleLimit / 10) + " deg");
    }

    public void ReturnToMainMenu()
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

