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

    public Button BoardSpecsConfirm;
    public Button BoardSpecsCancel;
    public CheckBox KeepWheelDiam;
    public CheckBox KeepLEDSpacing;
    public SeekBar WheelDiam;
    public SeekBar LEDSpacing;
    public TextView WheelDiamDisplay;
    public TextView LEDSpacingDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_specs_setting);

        KeepLEDSpacing = (CheckBox) findViewById(R.id.KeepLedSpacing);
        KeepWheelDiam = (CheckBox) findViewById(R.id.KeepWheelDiam);
        BoardSpecsConfirm = (Button) findViewById(R.id.BoardSpecsConfirm);
        BoardSpecsCancel = (Button) findViewById(R.id.BoardSpecsCancel);
        WheelDiam = (SeekBar) findViewById(R.id.WheelDiamSeekbar);
        LEDSpacing = (SeekBar) findViewById(R.id.LEDSpacingSeekbar);
        WheelDiamDisplay = (TextView) findViewById(R.id.WheelDiamDisplay);
        LEDSpacingDisplay= (TextView) findViewById(R.id.LEDSpacingDisplay);


        WheelDiam.setEnabled(false); // Seekbar is not available unless checkbox is unchecked
        LEDSpacing.setEnabled(false); // checkbox must be unchecked

        CheckWheelSeekbar(WheelDiam);
        CheckLEDSeekbar(LEDSpacing);

        BoardSpecsConfirm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                ReturnToMainMenu();
            }
        });

        BoardSpecsCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                ReturnToMainMenu();
            }
        });


        KeepWheelDiam.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CheckStatusOfWheelDiam(KeepWheelDiam);
            }
            });

        KeepLEDSpacing.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CheckStatusOfLEDSpacing(KeepLEDSpacing);
            }
        });


            }

    public void ReturnToMainMenu(){
        Intent myIntent = new Intent(BoardSpecsScreen.this, MainMenu.class);

        BoardSpecsScreen.this.startActivity(myIntent);

    }

    public void DisplayWheelDiam(int wheeldiam) {

        WheelDiamDisplay.setText(String.valueOf(wheeldiam));  // Display wheel diam divide by 10 to convert to decimal
    }

    public void DisplayLEDSpacing (int LEDSpacing){
        float ledspace = LEDSpacing;
        LEDSpacingDisplay.setText(String.valueOf(ledspace/10));
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

            WheelDiam.setEnabled(false);
        }
        else{
            WheelDiam.setEnabled(true);
        }
    }

    public void CheckStatusOfLEDSpacing(CheckBox mcheckbox){
        if(mcheckbox.isChecked()){
            LEDSpacing.setEnabled(false);
        }
        else{
            LEDSpacing.setEnabled(true);
        }
    }
}
