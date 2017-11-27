
package com.jackson.andrew.colorcarvesapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

public class SpeedSettingScreen extends AppCompatActivity {

    private Button SpeedConfirm;
    private Button SpeedCancel;
    private TextView MinSpeedDisplay;
    private TextView MaxSpeedDisplay;
    private CheckBox checkboxKeepMinSpeed;
    private CheckBox checkboxKeepMaxSpeed;
    private double DefaultMinSpeed =0;       //Default speed
    private double DefaultMaxSpeed = 12.6;   //Default speed
    private byte id = (byte)0x12;
    private byte data2 = (byte)0x00;
    private byte data1MaxSpeed = (byte)0x7F;
    private byte data0MinSpeed = (byte)0x7F;
    private byte byteMinSpeed = (byte)0x00; //default of min speed
    private byte byteMaxSpeed = (byte)0x7F; // default of max speed
    private Payload payload;
    private SeekBar seekBarMinSpeed;
    private SeekBar seekBarMaxSpeed;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speed_setting_screen);

        SpeedConfirm = (Button) findViewById(R.id.SpeedConfirm);
        SpeedCancel = (Button) findViewById(R.id.SpeedCancel);
        MinSpeedDisplay = (TextView) findViewById(R.id.MinSpeedDisplay);
        MaxSpeedDisplay = (TextView) findViewById(R.id.MaxSpeedDisplay);
        checkboxKeepMaxSpeed = (CheckBox) findViewById(R.id.KeepMaxSpeed);
        checkboxKeepMinSpeed =(CheckBox)findViewById(R.id.KeepMinSpeed);
        seekBarMinSpeed = (SeekBar)findViewById(R.id.seekBarMinSpeed);
        seekBarMaxSpeed = (SeekBar)findViewById(R.id.seekBarMaxSpeed);

        MinSpeedDisplay.setText(String.valueOf(DefaultMinSpeed));
        MaxSpeedDisplay.setText(String.valueOf(DefaultMaxSpeed));
        MinSpeedDisplay.setEnabled(false);
        MaxSpeedDisplay.setEnabled(false);
        seekBarMinSpeed.setEnabled(false);
        seekBarMaxSpeed.setEnabled(false);
        payload = new Payload();



        checkminSeekbar(seekBarMinSpeed);
        checkmaxSeekbar(seekBarMaxSpeed);






        SpeedConfirm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if(!checkboxKeepMinSpeed.isChecked())
                {

                   byteMinSpeed = (byte)seekBarMinSpeed.getProgress();


                }

                if(!checkboxKeepMaxSpeed.isChecked())
                {
                    byteMaxSpeed=(byte)seekBarMaxSpeed.getProgress();

                }

                payload.id.setId(id);
                payload.data.setData(2,data2);
                payload.data.setData(1,(byte)(data1MaxSpeed & byteMaxSpeed));
                payload.data.setData(0,(byte)(data0MinSpeed & byteMinSpeed));
                CMPPortTx.getInstance().queueToSend(payload);
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



    public void checkminSeekbar(final SeekBar mSeekBar) {


        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {


            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                displayminSpeedSpeed(mSeekBar.getProgress());
            }
        });
    }
    public void checkmaxSeekbar(final SeekBar mSeekBar) {


        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {


            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                displaymaxSpeed(mSeekBar.getProgress());
            }
        });
    }

    public void displayminSpeedSpeed(int progress)
    {
        float speed = progress;
        MinSpeedDisplay.setText(String.valueOf(speed/10) + " m/s");
    }

    public void displaymaxSpeed(int progress)
    {
        float speed = progress;
        MaxSpeedDisplay.setText(String.valueOf(speed/10)+ "m/s");
    }





    public void ReturnToMainMenu(){
        Intent myIntent = new Intent(SpeedSettingScreen.this, MainMenu.class);

        SpeedSettingScreen.this.startActivity(myIntent);

    }

    public void CheckStatusOfMinSpeed(CheckBox mCheckbox) {

        if(mCheckbox.isChecked()){
            MinSpeedDisplay.setEnabled(false); //MinSpeed is default value
            seekBarMinSpeed.setEnabled(false);

        }
        else{
            MinSpeedDisplay.setEnabled(true);
            seekBarMinSpeed.setEnabled(true);
        }

    }

    public void CheckStatusOfMaxSpeed(CheckBox mCheckbox) {

        if(mCheckbox.isChecked()){
            MaxSpeedDisplay.setEnabled(false); //MaxSpeed is default value
            seekBarMaxSpeed.setEnabled(false);

        }
        else{
            MaxSpeedDisplay.setEnabled(true);
            seekBarMaxSpeed.setEnabled(true);
        }

    }




    public void intToByte(byte val, int data)
    {
        val = (byte)(data);


    }

}








