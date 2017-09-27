package com.jackson.andrew.colorcarvesapp;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;

public class LEDSettingScreen extends AppCompatActivity{

    private CheckBox LEDAllSelected;
    private CheckBox KeepLEDOnOff;
    private CheckBox LEDOnOff;
    private CheckBox KeepOffset;
    private Button LEDConfirm;
    private Button LEDCancel;
    private SeekBar LEDOffsetSeekbar;
    private EditText LEDIndex;
    private Button LEDColorDisplay;
    private byte Red;
    private byte Blue;
    private byte Green;
    private int ConvertedColor;
    private byte stateOfLed;
    private byte indexOfLed;
    private byte [] offsetOfLed;
    private Payload payloadOfMessage;
    private byte ledStateData2 = (byte)0x03;//2 bits for on/off
    private byte ledIndexData1 = (byte)0xFC; //top 6 bits to set index
    private byte ledOffsetData1 = (byte)0x03; // 2 bits for offset data
    private byte ledOffsetData0 = (byte)0xFF; //All of data 0
    private byte id = (byte)0x10; //protocol id for LED page




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
        payloadOfMessage = new Payload();
        LEDConfirm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                stateOfLed = (byte)(0x11); //Initial state is dont care in protocol
                indexOfLed = (byte)(0xFF); //Initial state is led 0
                offsetOfLed = new byte[2];
                offsetOfLed[1] = (byte)0x03; //MSB 10 and 9 set to 0
                offsetOfLed[0] = (byte)0xFF;// LS byte  of offset
                if(!KeepLEDOnOff.isChecked())
                {
                    if(LEDOnOff.isChecked()) //Checked to off
                    {
                        stateOfLed = (byte) 0x01;//led on
                    }
                    if(!LEDOnOff.isChecked())
                    {
                        stateOfLed = (byte)0x00; //led off
                    }
                }
                if(LEDAllSelected.isChecked())
                {
                    indexOfLed = (byte)0x3F; //set to 63 top 6 bits of data1 message
                }
                if(!KeepOffset.isChecked())
                {
                    int tempOffset = LEDOffsetSeekbar.getProgress();
                    intToByteArray(offsetOfLed,tempOffset); //Sets the two bytes to the correct value
                }
                if(!LEDAllSelected.isChecked())
                {
                    indexOfLed = (byte) (getIndex()); //sets index in users numpad to byte
                }

                payloadOfMessage.id.setId(id);
                payloadOfMessage.data.setData(2,(byte)(stateOfLed & ledStateData2));
                payloadOfMessage.data.setData(1, (byte)((indexOfLed << 2) + (offsetOfLed[1] & ledOffsetData1)));
                payloadOfMessage.data.setData(0, (byte)(offsetOfLed[0] & ledOffsetData0));
                CMPPort.getInstance().queueToSend(payloadOfMessage);






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
                colorToHex(Red, Green, Blue); //Takes the three separate colors Red Blue and Green converts them to hex

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

            public int getIndex() {   //Grabs Numpad User number to integer value passed to byte array
                String TempIndex;
                TempIndex = LEDIndex.getText().toString();

                int retVal = 0;

                try
                {
                    retVal = Integer.parseInt(TempIndex);
                }
                catch(Exception e)
                {
                    retVal = 0;
                }

                return retVal;
            }


            public void colorToHex(byte R, byte G, byte B)
            {

                String CurrentColor = String.format("#%02x%02x%02x", R, G, B);
                Log.d("current color", CurrentColor); //display color selected in hex
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
    public void intToByteArray(byte [] val, int data)
    {

        String tempColor = String.valueOf(data);
        Log.d("AndysMessage", tempColor);
        val[0] = (byte)(data);  //discards all but bottom 8 bits of val
        val[1] = (byte)((data >> 8 )); // takes val and discards bottom 8 bits.

    }
        }