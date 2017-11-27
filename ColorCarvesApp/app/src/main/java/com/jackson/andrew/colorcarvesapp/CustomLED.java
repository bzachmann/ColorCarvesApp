package com.jackson.andrew.colorcarvesapp;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;

public class CustomLED extends AppCompatActivity {

    private CheckBox setOddLEDS;
    private CheckBox setEvenLEDs;
    private CheckBox setFirstHalfLEDs;
    private CheckBox setSecondHalfLEDs;
    private CheckBox setRainbowLEDs;
    private Button displayLEDColor;
    private Button customLEDConfirm;
    private Button customLEDCancel;
    private SeekBar seekBarLEDColor;
    private byte Red;
    private byte Blue;
    private byte Green;
    private int totalLEDS = 33; //LEDS on Longboard
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
        setContentView(R.layout.activity_custom_led);


        setOddLEDS = (CheckBox) findViewById(R.id.OddLED);
        setEvenLEDs = (CheckBox) findViewById(R.id.EvenLED);
        setFirstHalfLEDs = (CheckBox) findViewById(R.id.FirstHalfLED);
        setRainbowLEDs = (CheckBox) findViewById(R.id.RainbowLED);
        setSecondHalfLEDs = (CheckBox) findViewById(R.id.SecondHalfLED);
        seekBarLEDColor = (SeekBar) findViewById(R.id.LEDColor);
        displayLEDColor = (Button) findViewById(R.id.DisplayLED);
        customLEDConfirm = (Button) findViewById(R.id.CustomLEDConfirm);
        customLEDCancel = (Button) findViewById(R.id.CustomLEDCancel);
        displayLEDColor.setBackgroundColor(getConvertedColor());



        readSeek(seekBarLEDColor);
        payloadOfMessage = new Payload();


        customLEDConfirm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                stateOfLed = (byte)(0x11); //Initial state is dont care in protocol
                indexOfLed = (byte)(0xFF); //Initial state is led 0
                offsetOfLed = new byte[2];
                offsetOfLed[1] = (byte)0x03; //MSB 10 and 9 set to 0
                offsetOfLed[0] = (byte)0xFF;// LS byte  of offset




                if(setOddLEDS.isChecked())
                {

                    intToByteArray(offsetOfLed,seekBarLEDColor.getProgress()); //get color selected
                    for(int x =1; x< totalLEDS; x += 2)
                    {
                        indexOfLed = (byte)x; //Set to odd

                        payloadOfMessage.id.setId(id);
                        payloadOfMessage.data.setData(2,(byte)(stateOfLed & ledStateData2));
                        payloadOfMessage.data.setData(1, (byte)((indexOfLed << 2) | (offsetOfLed[1] & ledOffsetData1)));
                        payloadOfMessage.data.setData(0, (byte)(offsetOfLed[0] & ledOffsetData0));
                        CMPPort.getInstance().queueToSend(payloadOfMessage);
                    }
                }

                if(setEvenLEDs.isChecked())
                {

                    intToByteArray(offsetOfLed,seekBarLEDColor.getProgress()); //get color selected
                    for(int x =0; x< totalLEDS; x += 2)
                    {
                        indexOfLed = (byte)x; //Set to even

                        payloadOfMessage.id.setId(id);
                        payloadOfMessage.data.setData(2,(byte)(stateOfLed & ledStateData2));
                        payloadOfMessage.data.setData(1, (byte)((indexOfLed << 2) | (offsetOfLed[1] & ledOffsetData1)));
                        payloadOfMessage.data.setData(0, (byte)(offsetOfLed[0] & ledOffsetData0));
                        CMPPort.getInstance().queueToSend(payloadOfMessage);
                    }
                }

                if(setFirstHalfLEDs.isChecked())
                {

                    intToByteArray(offsetOfLed,seekBarLEDColor.getProgress()); //get color selected
                    for(int x =0; x< totalLEDS/2 + 1; x ++)
                    {
                        indexOfLed = (byte)x; //Set to first half

                        payloadOfMessage.id.setId(id);
                        payloadOfMessage.data.setData(2,(byte)(stateOfLed & ledStateData2));
                        payloadOfMessage.data.setData(1, (byte)((indexOfLed << 2) | (offsetOfLed[1] & ledOffsetData1)));
                        payloadOfMessage.data.setData(0, (byte)(offsetOfLed[0] & ledOffsetData0));
                        CMPPort.getInstance().queueToSend(payloadOfMessage);
                    }
                }
                if(setSecondHalfLEDs.isChecked())
                {

                    intToByteArray(offsetOfLed,seekBarLEDColor.getProgress()); //get color selected
                    for(int x =totalLEDS/2 + 2; x< totalLEDS; x ++)
                    {
                        indexOfLed = (byte)x; //Set to second half

                        payloadOfMessage.id.setId(id);
                        payloadOfMessage.data.setData(2,(byte)(stateOfLed & ledStateData2));
                        payloadOfMessage.data.setData(1, (byte)((indexOfLed << 2) | (offsetOfLed[1] & ledOffsetData1)));
                        payloadOfMessage.data.setData(0, (byte)(offsetOfLed[0] & ledOffsetData0));
                        CMPPort.getInstance().queueToSend(payloadOfMessage);
                    }
                }
                if(setRainbowLEDs.isChecked())
                {
                    int tempcolor = totalLEDS/765;


                    for(int x =0; x< totalLEDS; x ++)
                    {
                        indexOfLed = (byte)x; //all LEDs
                        offsetOfLed[0] = (byte)(tempcolor*x);
                        offsetOfLed[1] = (byte)((tempcolor*x) >> 8);

                        payloadOfMessage.id.setId(id);
                        payloadOfMessage.data.setData(2,(byte)(stateOfLed & ledStateData2));
                        payloadOfMessage.data.setData(1, (byte)((indexOfLed << 2) | (offsetOfLed[1] & ledOffsetData1)));
                        payloadOfMessage.data.setData(0, (byte)(offsetOfLed[0] & ledOffsetData0));
                        CMPPort.getInstance().queueToSend(payloadOfMessage);
                    }
                }

                ReturnToMainMenu();
            }
        });

        customLEDCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                ReturnToMainMenu();
            }
        });

        setOddLEDS.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                CheckStatusOfCheckboxes(setOddLEDS,setEvenLEDs,setRainbowLEDs,setFirstHalfLEDs,setSecondHalfLEDs);
            }
        });
        setEvenLEDs.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                CheckStatusOfCheckboxes(setOddLEDS,setEvenLEDs,setRainbowLEDs,setFirstHalfLEDs,setSecondHalfLEDs);
            }
        });
        setRainbowLEDs.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                CheckStatusOfCheckboxes(setOddLEDS,setEvenLEDs,setRainbowLEDs,setFirstHalfLEDs,setSecondHalfLEDs);
            }
        });
        setFirstHalfLEDs.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                CheckStatusOfCheckboxes(setOddLEDS,setEvenLEDs,setRainbowLEDs,setFirstHalfLEDs,setSecondHalfLEDs);
            }
        });
        setSecondHalfLEDs.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                CheckStatusOfCheckboxes(setOddLEDS,setEvenLEDs,setRainbowLEDs,setFirstHalfLEDs,setSecondHalfLEDs);
            }
        });





    }

    public void ReturnToMainMenu(){
        Intent myIntent = new Intent(CustomLED.this, MainMenu.class);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        CustomLED.this.startActivity(myIntent);

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

                displayLEDColor.setBackgroundColor(getConvertedColor());  //sets offset text to color


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


    public void CheckStatusOfCheckboxes(CheckBox Odd, CheckBox Even, CheckBox Rainbow, CheckBox FirstHalf, CheckBox SecondHalf ){

        if(Odd.isChecked()){


            setFirstHalfLEDs.setEnabled(false);
            setEvenLEDs.setEnabled(false);
            setRainbowLEDs.setEnabled(false);
            setSecondHalfLEDs.setEnabled(false);
        }
        if(Even.isChecked()){

            setFirstHalfLEDs.setEnabled(false);
            setOddLEDS.setEnabled(false);
            setRainbowLEDs.setEnabled(false);
            setSecondHalfLEDs.setEnabled(false);
        }
        if(Rainbow.isChecked()){

            setFirstHalfLEDs.setEnabled(false);
            setEvenLEDs.setEnabled(false);
            setOddLEDS.setEnabled(false);
            setSecondHalfLEDs.setEnabled(false);
        }
        if(FirstHalf.isChecked()){

            setOddLEDS.setEnabled(false);
            setEvenLEDs.setEnabled(false);
            setRainbowLEDs.setEnabled(false);
            setSecondHalfLEDs.setEnabled(false);
        }
        if(SecondHalf.isChecked()){
            setFirstHalfLEDs.setEnabled(false);
            setEvenLEDs.setEnabled(false);
            setRainbowLEDs.setEnabled(false);
            setOddLEDS.setEnabled(false);

        }


    }
    public void intToByteArray(byte [] val, int data)
    {

        val[0] = (byte)(data);  //discards all but bottom 8 bits of val
        val[1] = (byte)((data >> 8 )); // takes val and discards bottom 8 bits.

    }


}