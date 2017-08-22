package com.jackson.andrew.colorcarvesapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

public class EnableOptionsSettingScreen extends AppCompatActivity {

    public Button EnableOptionsConfirm;
    public Button EnableOptionsCancel;
    public CheckBox KeepOffsetSettings;
    public CheckBox KeepBrightnessSettings;
    public CheckBox KeepPatternSettings;
    public CheckBox OffsetInfluence;
    public CheckBox PatternInfluence;
    public CheckBox BrightnessInfluence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enable_options_setting_screen);

        EnableOptionsConfirm = (Button) findViewById(R.id.EnableOptionsConfirm);
        EnableOptionsCancel = (Button) findViewById(R.id.EnableOptionsCancel);
        KeepOffsetSettings = (CheckBox) findViewById(R.id.KeepOffsetSetting);
        KeepBrightnessSettings = (CheckBox) findViewById(R.id.KeepBrightnessSetting);
        KeepPatternSettings = (CheckBox) findViewById(R.id.KeepPatternSetting);
        OffsetInfluence = (CheckBox) findViewById(R.id.OffSetInfluenced);
        PatternInfluence = (CheckBox) findViewById(R.id.PatternInfluenced);
        BrightnessInfluence = (CheckBox) findViewById(R.id.BrightnessInfluenced);

        OffsetInfluence.setEnabled(false);
        PatternInfluence.setEnabled(false); // All checkboxes are grayed out untill keep settings is not selected
        BrightnessInfluence.setEnabled(false);

        EnableOptionsConfirm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                ReturnToMainMenu();
            }
        });

        EnableOptionsCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                ReturnToMainMenu();
            }
        });

        KeepOffsetSettings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CheckStatusOFSettings(KeepOffsetSettings,OffsetInfluence);
            }
        });

        KeepBrightnessSettings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CheckStatusOFSettings(KeepBrightnessSettings,BrightnessInfluence);
            }
        });
        KeepPatternSettings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CheckStatusOFSettings(KeepPatternSettings,PatternInfluence);
            }
        });

        OffsetInfluence.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CheckStatusOFSettings(KeepOffsetSettings,OffsetInfluence);
            }
        });
        BrightnessInfluence.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CheckStatusOFSettings(KeepBrightnessSettings,BrightnessInfluence);
            }
        });
        PatternInfluence.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CheckStatusOFSettings(KeepPatternSettings,PatternInfluence);
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
        if(influence.isChecked()) {
            currentsettings.setChecked(false);
            currentsettings.setEnabled(false);
        }
        else{
            currentsettings.setEnabled(true);

        }
    }

    public void ReturnToMainMenu(){
        Intent myIntent = new Intent(EnableOptionsSettingScreen.this, MainMenu.class);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        EnableOptionsSettingScreen.this.startActivity(myIntent);

    }
}
