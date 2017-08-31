package com.jackson.andrew.colorcarvesapp;

import android.app.ActionBar;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.view.View;

public class AngleSettingScreen extends AppCompatActivity {

    public CheckBox AngleCurrentSetting;
    public CheckBox AngleAbsoluteMode;
    public Button AngleConfirm;
    public Button AngleCancel;
    public SeekBar AngleLimitSeekBar;
    public TextView AngleLimitDisplay;
    public int DefaultAngleSetting = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_angle_setting_screen);



        AngleAbsoluteMode = (CheckBox) findViewById(R.id.AngleAbsoluteMode);
        AngleCurrentSetting = (CheckBox) findViewById(R.id.AngleCurrentSetting);
        AngleConfirm = (Button) findViewById(R.id.AngleConfirm);
        AngleCancel = (Button) findViewById(R.id.AngleCancel);
        AngleLimitSeekBar = (SeekBar) findViewById(R.id.AngleLimitSeekBar);
        AngleLimitDisplay = (TextView) findViewById(R.id.AngleLimitDisplay);
        AngleLimitSeekBar.setEnabled(false);        //Grayed out when page is opened

        AngleLimitDisplay.setText(String.valueOf(DefaultAngleSetting));  //Initializes to 0
        



        CheckMySeekBar(AngleLimitSeekBar);  //Determine if the Angle should be adjusted


        AngleCurrentSetting.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(AngleCurrentSetting.isChecked())
                {
                    AngleLimitSeekBar.setEnabled(false);
                }
                else{
                    AngleLimitSeekBar.setEnabled(true);
                }
            }
        });
        
        

        AngleConfirm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                ReturnToMainMenu();
            }
        });


        AngleCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ReturnToMainMenu();
            }
        });


    }

    public void CheckMySeekBar (final SeekBar mSeekBar) {


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


    
    public void CheckStatusOfSetting(CheckBox mcheckbox){
        
        if(mcheckbox.isChecked()){
            AngleLimitSeekBar.setEnabled(false);
            
        }

        if(!mcheckbox.isChecked()){
            AngleLimitSeekBar.setEnabled(true);
        }
    }


    public void DisplayAngleLimit (int progress) {
        float AngleLimit = progress;
        AngleLimitDisplay.setText(String.valueOf(AngleLimit/10));
    }

    public void ReturnToMainMenu(){
        Intent myIntent = new Intent(AngleSettingScreen.this, MainMenu.class);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        AngleSettingScreen.this.startActivity(myIntent);

    }



}

