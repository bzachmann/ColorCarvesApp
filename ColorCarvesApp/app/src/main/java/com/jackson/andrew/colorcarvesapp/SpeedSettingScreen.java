
package com.jackson.andrew.colorcarvesapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class SpeedSettingScreen extends AppCompatActivity {

    public Button SpeedConfirm;
    public Button SpeedCancel;
    public EditText MinSpeedDisplay;
    public EditText MaxSpeedDisplay;
    public CheckBox KeepMinSpeed;
    public CheckBox KeepMaxSpeed;
    public int DefaultMinSpeed =0;       //Default speed
    public int DefaultMaxSpeed = 9;   //Default speed


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speed_setting_screen);

        SpeedConfirm = (Button) findViewById(R.id.SpeedConfirm);
        SpeedCancel = (Button) findViewById(R.id.SpeedCancel);
        MinSpeedDisplay = (EditText) findViewById(R.id.MinSpeedDisplay);
        MaxSpeedDisplay = (EditText) findViewById(R.id.MaxSpeedDisaplay);
        KeepMaxSpeed = (CheckBox) findViewById(R.id.KeepMaxSpeed);
        KeepMinSpeed =(CheckBox)findViewById(R.id.KeepMinSpeed);

        MinSpeedDisplay.setText(String.valueOf(DefaultMinSpeed));
        MaxSpeedDisplay.setText(String.valueOf(DefaultMaxSpeed));
        MinSpeedDisplay.setEnabled(false);
        MaxSpeedDisplay.setEnabled(false);




        SpeedConfirm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                ReturnToMainMenu();
            }
        });

        KeepMinSpeed.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){
                CheckStatusOfMinSpeed(KeepMinSpeed);
            }


        });

        KeepMaxSpeed.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){
                CheckStatusOfMaxSpeed(KeepMaxSpeed);
            }


        });

        SpeedCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ReturnToMainMenu();
            }
        });


    }



    public int GetMinSpeed() {   //Grabs Numpad User number to integer value passed to byte array
        String TempSpeed;
        TempSpeed = MinSpeedDisplay.getText().toString();
        int Speed;
        Speed = Integer.parseInt(TempSpeed);

        if(Speed < 0 || Speed > 100){

            Speed = 0; // Default Min speed
        }

        return Speed;
    }

    public int GetMaxSpeed(){

        String TempSpeed;
        TempSpeed = MaxSpeedDisplay.getText().toString();
        int Speed;
        Speed = Integer.parseInt(TempSpeed);
        if(Speed < 0 || Speed > 100){

            Speed = 100; // Default Max speed
        }
        return Speed;
    }

    public void ReturnToMainMenu(){
        Intent myIntent = new Intent(SpeedSettingScreen.this, MainMenu.class);

        SpeedSettingScreen.this.startActivity(myIntent);

    }

    public void CheckStatusOfMinSpeed(CheckBox mCheckbox) {

        if(mCheckbox.isChecked()){
            MinSpeedDisplay.setEnabled(false); //MinSpeed is default value

        }
        else{
            MinSpeedDisplay.setEnabled(true);
        }

    }

    public void CheckStatusOfMaxSpeed(CheckBox mCheckbox) {

        if(mCheckbox.isChecked()){
            MaxSpeedDisplay.setEnabled(false); //MaxSpeed is default value

        }
        else{
            MaxSpeedDisplay.setEnabled(true);
        }

    }
}








