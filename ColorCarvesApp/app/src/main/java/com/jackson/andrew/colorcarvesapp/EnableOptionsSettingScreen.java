package com.jackson.andrew.colorcarvesapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

public class EnableOptionsSettingScreen extends AppCompatActivity {

    private Button EnableOptionsConfirm;
    private Button EnableOptionsCancel;
    private CheckBox checkboxKeepOffsetSettings;
    private CheckBox checkboxKeepBrightnessSettings;
    private CheckBox checkboxKeepPatternSettings;
    private CheckBox checkboxOffsetInfluence;
    private CheckBox checkboxPatternInfluence;
    private CheckBox checkboxBrightnessInfluence;
    private Payload payload;
    private byte id = (byte)0X15;
    private byte data2Pattern = (byte)0x0F;
    private byte data1Brightness = (byte)0xF0;
    private byte data1Offset = (byte)0x0F;
    private byte data0Pattern = (byte)0x30;
    private byte data0Bright = (byte)0x0C;
    private byte data0Offset = (byte)0x03;
    private byte bytePatternOn = (byte)0x30; //set as a dont care in correct position of protocol
    private byte byteBrightOn = (byte)0x0C; //set as a dont care as protocol
    private byte byteOffsetOn = (byte)0x03; //protocol for dont care


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enable_options_setting_screen);

        EnableOptionsConfirm = (Button) findViewById(R.id.EnableOptionsConfirm);
        EnableOptionsCancel = (Button) findViewById(R.id.EnableOptionsCancel);
        checkboxKeepOffsetSettings = (CheckBox) findViewById(R.id.KeepOffsetSetting);
        checkboxKeepBrightnessSettings = (CheckBox) findViewById(R.id.KeepBrightnessSetting);
        checkboxKeepPatternSettings = (CheckBox) findViewById(R.id.KeepPatternSetting);
        checkboxOffsetInfluence = (CheckBox) findViewById(R.id.OffSetInfluenced);
        checkboxPatternInfluence = (CheckBox) findViewById(R.id.PatternInfluenced);
        checkboxBrightnessInfluence = (CheckBox) findViewById(R.id.BrightnessInfluenced);

        checkboxOffsetInfluence.setEnabled(false);
        checkboxPatternInfluence.setEnabled(false); // All checkboxes are grayed out untill keep settings is not selected
        checkboxBrightnessInfluence.setEnabled(false);

        payload = new Payload();


        EnableOptionsConfirm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if(checkboxOffsetInfluence.isChecked())
                {
                    byteOffsetOn = (byte)0x01;//offset influence on
                }
                if(!checkboxOffsetInfluence.isChecked() && !checkboxKeepOffsetSettings.isChecked())
                {
                    byteOffsetOn = (byte)0x00; //offset influence off
                }
                if(checkboxBrightnessInfluence.isChecked())
                {
                    byteBrightOn = (byte)0x04;//brightness influence on
                }
                if(!checkboxBrightnessInfluence.isChecked() && !checkboxKeepBrightnessSettings.isChecked())
                {
                    byteBrightOn = (byte)0x00; //brightness influence off
                }
                if(checkboxPatternInfluence.isChecked())
                {
                    bytePatternOn = (byte)0x10;//pattern influence on
                }
                if(!checkboxPatternInfluence.isChecked() && !checkboxKeepPatternSettings.isChecked())
                {
                    bytePatternOn = (byte)0x00; //pattern influence off
                }

                payload.id.setId(id);
                payload.data.setData(2, data2Pattern); // will and with pattern later
                payload.data.setData(1,(byte)(data1Brightness + data1Offset)); //will and with bright and offset later
                payload.data.setData(0, (byte)((data0Pattern & bytePatternOn)+(data0Bright & byteBrightOn)+ (data0Offset & byteOffsetOn)));
                CMPPort.getInstance().queueToSend(payload); //send to queue

                ReturnToMainMenu();
            }
        });

        EnableOptionsCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                ReturnToMainMenu();
            }
        });

        checkboxKeepOffsetSettings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CheckStatusOFSettings(checkboxKeepOffsetSettings, checkboxOffsetInfluence);
            }
        });

        checkboxKeepBrightnessSettings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CheckStatusOFSettings(checkboxKeepBrightnessSettings, checkboxBrightnessInfluence);
            }
        });
        checkboxKeepPatternSettings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CheckStatusOFSettings(checkboxKeepPatternSettings, checkboxPatternInfluence);
            }
        });

        checkboxOffsetInfluence.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CheckStatusOFSettings(checkboxKeepOffsetSettings, checkboxOffsetInfluence);
            }
        });
        checkboxBrightnessInfluence.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CheckStatusOFSettings(checkboxKeepBrightnessSettings, checkboxBrightnessInfluence);
            }
        });
        checkboxPatternInfluence.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CheckStatusOFSettings(checkboxKeepPatternSettings, checkboxPatternInfluence);
            }
        });


    }


    public void CheckStatusOFSettings(CheckBox currentsettings, CheckBox influence){

        if(currentsettings.isChecked()){
            influence.setChecked(false);
            influence.setEnabled(false);
        }
        else {
            influence.setEnabled(true);
        }

    }

    public void ReturnToMainMenu(){
        Intent myIntent = new Intent(EnableOptionsSettingScreen.this, MainMenu.class);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        EnableOptionsSettingScreen.this.startActivity(myIntent);

    }
}
