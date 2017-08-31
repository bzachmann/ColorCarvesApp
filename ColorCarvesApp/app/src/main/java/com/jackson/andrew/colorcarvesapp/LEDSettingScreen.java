package com.jackson.andrew.colorcarvesapp;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

public class LEDSettingScreen extends AppCompatActivity {

    public CheckBox LEDAllSelected;
    public CheckBox KeepLEDOnOff;
    public CheckBox LEDOnOff;
    public CheckBox KeepOffset;
    public Button LEDConfirm;
    public Button LEDCancel;
    public SeekBar LEDOffsetSeekbar;
    public EditText LEDIndex;
    public Button LEDColorDisplay;
    public byte Red;
    public byte Blue;
    public byte Green;
    public int ConvertedColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ledsetting_screen);

        LEDAllSelected = (CheckBox) findViewById(R.id.LEDAllSelected);
        KeepLEDOnOff = (CheckBox) findViewById(R.id.KeepOnOff);
        LEDOnOff = (CheckBox) findViewById(R.id.LEDState);
        KeepOffset = (CheckBox) findViewById(R.id.KeepOffset);
        LEDConfirm = (Button) findViewById(R.id.LEDConfirm);
        LEDCancel = (Button) findViewById(R.id.LEDCancel);
        LEDOffsetSeekbar = (SeekBar)findViewById(R.id.LEDOffsetSeekbar);
        LEDIndex = (EditText) findViewById(R.id.LEDIndex);
        LEDColorDisplay = (Button) findViewById(R.id.LEDOffsetDisplay);

        LEDColorDisplay.setBackgroundColor(getConvertedColor());



        LEDOffsetSeekbar.setEnabled(false);
        LEDOnOff.setEnabled(false);

        readSeek(LEDOffsetSeekbar);
        LEDConfirm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                ReturnToMainMenu();
            }
        });

        LEDCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                ReturnToMainMenu();
            }
        });

        KeepLEDOnOff.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                CheckStatusOfCheckboxes(LEDAllSelected,KeepLEDOnOff,LEDOnOff,KeepOffset);
            }
        });
        LEDAllSelected.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                CheckStatusOfCheckboxes(LEDAllSelected,KeepLEDOnOff,LEDOnOff,KeepOffset);
            }
        });
        LEDOnOff.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                CheckStatusOfCheckboxes(LEDAllSelected,KeepLEDOnOff,LEDOnOff,KeepOffset);
            }
        });
        KeepOffset.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                CheckStatusOfCheckboxes(LEDAllSelected,KeepLEDOnOff,LEDOnOff,KeepOffset);
            }
        });





    }

    public void ReturnToMainMenu(){
        Intent myIntent = new Intent(LEDSettingScreen.this, MainMenu.class);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        LEDSettingScreen.this.startActivity(myIntent);

    }


    public void readSeek(SeekBar seekBar) {
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                SetColor((progress + 383) % 765);  // Mods to display middle color based on tilt or speed
                Red = getRed();
                Blue = getBlue();
                Green = getGreen();
                ColorToHex(Red, Green, Blue); //Takes the three separate colors Red Blue and Green converts them to hex

                LEDColorDisplay.setBackgroundColor(getConvertedColor());  //sets offset text to color


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekbar) {


            }

            @Override
            public void onStopTrackingTouch(SeekBar seekbar) {


            }
        });
    }
            public void SetColor(int ColorToRGB) {


                if (ColorToRGB < 255) {

                    Red = (byte) ColorToRGB;
                    Blue = 0;
                    Green = (byte) (255 - ((byte) ColorToRGB));
                    // Log.d(TAG, "Error 1");  debugging

                } else if (ColorToRGB > 255 && ColorToRGB < 510) {
                    Green = 0;
                    int temp = ColorToRGB - 255;
                    Blue = ((byte) temp);
                    Red = (byte) (255 - ((byte) temp));

                } else if (ColorToRGB > 510) {
                    Red = 0;
                    int temp = ColorToRGB - 510;
                    Green = (byte) temp;
                    Blue = (byte) (255 - (byte) temp);


                }
            }







    /*public long GetSleepTime()
    {
        String TempSleep;
        TempSleep = SleepTime.getText().toString();
        long sleep;                                     used in testing to determine optimal time
        sleep = Integer.parseInt(TempSleep);
        return sleep;
    }
*/

            public int GetIndex() {   //Grabs Numpad User number to integer value passed to byte array
                String TempIndex;
                TempIndex = LEDIndex.getText().toString();
                int index;
                index = Integer.parseInt(TempIndex);

                return index;
            }


            public void ColorToHex(byte R, byte G, byte B) {

                String CurrentColor = String.format("#%02x%02x%02x", R, G, B);
                ConvertedColor = Color.parseColor(CurrentColor);

            }


            public int getConvertedColor() {
                return ConvertedColor;
            }

            public byte getBlue() {
                return Blue;
            }
            public byte getGreen() {
                return Green;
            }

            public byte getRed() {
                return Red;
            }


            public void CheckStatusOfCheckboxes(CheckBox All, CheckBox KeepOnOff, CheckBox Onoff, CheckBox KeepOffset ){

                if(All.isChecked()){

                    Onoff.setEnabled(true);
                    LEDIndex.setEnabled(false);
                }
                if(KeepOnOff.isChecked()){

                    Onoff.setEnabled(false);

                }
                if(!All.isChecked()){

                    LEDIndex.setEnabled(true);
                }
                if(!KeepOffset.isChecked()){

                    LEDOffsetSeekbar.setEnabled(true);
                }
                if(!KeepLEDOnOff.isChecked()){
                    Onoff.setEnabled(true);

                }


            }
        }