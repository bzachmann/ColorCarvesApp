
package com.jackson.andrew.colorcarvesapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class SpeedSettingScreen extends AppCompatActivity {

    private Button SpeedConfirm;
    private Button SpeedCancel;
    private EditText MinSpeedDisplay;
    private EditText MaxSpeedDisplay;
    private CheckBox checkboxKeepMinSpeed;
    private CheckBox checkboxKeepMaxSpeed;
    private int DefaultMinSpeed =0;       //Default speed
    private int DefaultMaxSpeed = 9;   //Default speed
    private byte id = (byte)0x12;
    private byte data2 = (byte)0x00;
    private byte data1MaxSpeed = (byte)0x7F;
    private byte data0MinSpeed = (byte)0x7F;
    private byte byteMinSpeed = (byte)0x00; //default of min speed
    private byte byteMaxSpeed = (byte)0x7F; // default of max speed
    private Payload payload;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speed_setting_screen);

        SpeedConfirm = (Button) findViewById(R.id.SpeedConfirm);
        SpeedCancel = (Button) findViewById(R.id.SpeedCancel);
        MinSpeedDisplay = (EditText) findViewById(R.id.MinSpeedDisplay);
        MaxSpeedDisplay = (EditText) findViewById(R.id.MaxSpeedDisaplay);
        checkboxKeepMaxSpeed = (CheckBox) findViewById(R.id.KeepMaxSpeed);
        checkboxKeepMinSpeed =(CheckBox)findViewById(R.id.KeepMinSpeed);

        MinSpeedDisplay.setText(String.valueOf(DefaultMinSpeed));
        MaxSpeedDisplay.setText(String.valueOf(DefaultMaxSpeed));
        MinSpeedDisplay.setEnabled(false);
        MaxSpeedDisplay.setEnabled(false);
        payload = new Payload();




        SpeedConfirm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if(!checkboxKeepMinSpeed.isChecked())
                {
                    if (getMaxSpeed() > getMinSpeed())  //valid speed entered min < max
                    {
                        intToByteArray(byteMinSpeed, getSpeedSetting(MinSpeedDisplay));  //Get value from numpad and send it to a byte

                    }
                }

                if(!checkboxKeepMaxSpeed.isChecked())
                {
                    if (getMaxSpeed() > getMinSpeed())  //valid speed entered min < max
                    {
                        intToByteArray(byteMaxSpeed, getSpeedSetting(MaxSpeedDisplay));  //Get value from numpad and send it to a byte

                    }

                }

                payload.id.setId(id);
                payload.data.setData(2,data2);
                payload.data.setData(1,(byte)(data1MaxSpeed & byteMaxSpeed));
                payload.data.setData(0,(byte)(data0MinSpeed & byteMinSpeed));
                CMPPort.getInstance().queueToSend(payload);
                ReturnToMainMenu();
            }
        });

        checkboxKeepMinSpeed.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){
                CheckStatusOfMinSpeed(checkboxKeepMinSpeed);
            }


        });

        checkboxKeepMaxSpeed.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){
                CheckStatusOfMaxSpeed(checkboxKeepMaxSpeed);
            }


        });

        SpeedCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ReturnToMainMenu();
            }
        });


    }



    public int getMinSpeed() {   //Grabs Numpad User number to integer value passed to byte array
        String TempSpeed;
        TempSpeed = MinSpeedDisplay.getText().toString();
        int Speed;
        Speed = Integer.parseInt(TempSpeed);

                return Speed;
    }

    public int getMaxSpeed(){

        String TempSpeed;
        TempSpeed = MaxSpeedDisplay.getText().toString();
        int Speed;
        Speed = Integer.parseInt(TempSpeed);

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

    public int getSpeedSetting(TextView displayText) {   //Grabs Numpad User number to integer value passed to byte array
        String TempIndex;
        TempIndex = displayText.getText().toString();

        int retVal = 0;

        try
        {
            retVal = Integer.parseInt(TempIndex);
            retVal = retVal * 10; //convert to 0-126 for byte value of speed

            if(retVal < 0 || retVal > 127)
            {
                retVal = 0;   //Makes sure valid number was entered on the numpad

            }
        }
        catch(Exception e)
        {
            retVal = 0;
        }

        return retVal;
    }


    public void intToByteArray(byte val, int data)
    {
        val = (byte)data;

    }

}








