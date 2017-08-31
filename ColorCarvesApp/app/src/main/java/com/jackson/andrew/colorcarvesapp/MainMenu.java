package com.jackson.andrew.colorcarvesapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainMenu extends AppCompatActivity {

    public Button ToLEDScreen;
    public Button ToAngleScreen;
    public Button ToSpeedScreen;
    public Button ToBoardSpecs;
    public Button ToBaseSetting;
    public Button ToEnableOptions;


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



}
