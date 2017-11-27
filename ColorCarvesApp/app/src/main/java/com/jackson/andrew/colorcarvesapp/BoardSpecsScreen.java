package com.jackson.andrew.colorcarvesapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;

public class BoardSpecsScreen extends AppCompatActivity {

    private Button BoardSpecsConfirm;
    private Button BoardSpecsCancel;
    private CheckBox checkboxKeepWheelDiam;
    private CheckBox checkboxKeepLEDSpacing;
    private SeekBar seekbarWheelDiam;
    private SeekBar seekbarLEDSpacing;
    private TextView WheelDiamDisplay;
    private TextView LEDSpacingDisplay;
    private byte id = (byte)0x13;
    private byte data2 = (byte)0x7F;
    private byte data1 = (byte)0x03;
    private byte data0 = (byte)0xFF;
    private byte byteWheelDiam;
    private byte[] ledSpacing;
    private Payload payload;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_specs_setting);

        checkboxKeepLEDSpacing = (CheckBox) findViewById(R.id.KeepLedSpacing);
        checkboxKeepWheelDiam = (CheckBox) findViewById(R.id.KeepWheelDiam);
        BoardSpecsConfirm = (Button) findViewById(R.id.BoardSpecsConfirm);
        BoardSpecsCancel = (Button) findViewById(R.id.BoardSpecsCancel);
        seekbarWheelDiam = (SeekBar) findViewById(R.id.WheelDiamSeekbar);
        seekbarLEDSpacing = (SeekBar) findViewById(R.id.LEDSpacingSeekbar);
        WheelDiamDisplay = (TextView) findViewById(R.id.WheelDiamDisplay);
        LEDSpacingDisplay= (TextView) findViewById(R.id.LEDSpacingDisplay);


        seekbarWheelDiam.setEnabled(false); // Seekbar is not available unless checkbox is unchecked
        seekbarLEDSpacing.setEnabled(false); // checkbox must be unchecked

        CheckWheelSeekbar(seekbarWheelDiam);
        CheckLEDSeekbar(seekbarLEDSpacing);

        ledSpacing = new byte[2];
        payload = new Payload();

        BoardSpecsConfirm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if(checkboxKeepWheelDiam.isChecked())
                {
                    byteWheelDiam = (byte)0x7F;
                }

                if(!checkboxKeepWheelDiam.isChecked())
                {
                    byteWheelDiam = (byte)seekbarWheelDiam.getProgress();
                }

                if(checkboxKeepLEDSpacing.isChecked())
                {
                    ledSpacing[1] = (byte)0x03;
                    ledSpacing[0] = (byte)0xFF;  //sets led spacing to 123, do not care, in protocol
                }

                if(!checkboxKeepLEDSpacing.isChecked())
                {
                    intToByteArray(ledSpacing,seekbarLEDSpacing.getProgress());

                }

                payload.id.setId(id);
                payload.data.setData(2,(byte)(data2 & byteWheelDiam));
                payload.data.setData(1, (byte)(data1 & ledSpacing[1]));
                payload.data.setData(0,(byte)(data0 & ledSpacing[0]));
                CMPPortTx.getInstance().queueToSend(payload);
                ReturnToMainMenu();
            }
        });

        BoardSpecsCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                ReturnToMainMenu();
            }
        });


        checkboxKeepWheelDiam.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CheckStatusOfWheelDiam(checkboxKeepWheelDiam);
            }
            });

        checkboxKeepLEDSpacing.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CheckStatusOfLEDSpacing(checkboxKeepLEDSpacing);
            }
        });


            }

    public void ReturnToMainMenu(){
        Intent myIntent = new Intent(BoardSpecsScreen.this, MainMenu.class);

        BoardSpecsScreen.this.startActivity(myIntent);

    }

    public void DisplayWheelDiam(int wheeldiam) {

        WheelDiamDisplay.setText(String.valueOf(wheeldiam) + " mm");  // Display wheel diam divide by 10 to convert to decimal
    }

    public void DisplayLEDSpacing (int LEDSpacing){
        float ledspace = LEDSpacing;
        LEDSpacingDisplay.setText(String.valueOf(ledspace/10) + " mm");
    }




    public void CheckWheelSeekbar (final SeekBar mSeekBar) {


        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {




            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                DisplayWheelDiam(mSeekBar.getProgress());
            }
        });
    }


    public void CheckLEDSeekbar (final SeekBar mSeekBar) {


        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {




            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                DisplayLEDSpacing(mSeekBar.getProgress());
            }
        });
    }

    public void CheckStatusOfWheelDiam(CheckBox mcheckbox){

        if (mcheckbox.isChecked()){

            seekbarWheelDiam.setEnabled(false);
        }
        else{
            seekbarWheelDiam.setEnabled(true);
        }
    }

    public void CheckStatusOfLEDSpacing(CheckBox mcheckbox){
        if(mcheckbox.isChecked()){
            seekbarLEDSpacing.setEnabled(false);
        }
        else{
            seekbarLEDSpacing.setEnabled(true);
        }
    }

    public void intToByteArray(byte [] val, int data)
    {


        val[0] = (byte)(data);  //discards all but bottom 8 bits of val
        val[1] = (byte)((data >> 8 )); // takes val and discards bottom 8 bits.




    }
}
