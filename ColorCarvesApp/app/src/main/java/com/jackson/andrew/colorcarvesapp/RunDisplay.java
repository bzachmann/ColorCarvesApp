package com.jackson.andrew.colorcarvesapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.concurrent.ArrayBlockingQueue;

public class RunDisplay extends AppCompatActivity {

    public TextView SpeedDisplay;
    public TextView TiltDisplay;
    public TextView Version;
    private Button ReturnToMainMenu;
    private Message messageFromCCD;
    private Payload payloadFromCCD;
    private Byte SpeedData2 = (byte)0x00;
    private Byte SpeedData1 = (byte)0x00;
    private Byte TiltData1 = (byte)0x00;
    private Byte TiltData0 = (byte)0x00;
    private byte[] data;
    private static RunDisplay inst;
    private ArrayBlockingQueue<Message> messageQueueFromCCD;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run_display);
        SpeedDisplay = (TextView) findViewById(R.id.currentSpeedDisplay);
        TiltDisplay = (TextView) findViewById(R.id.runTimeTiltDisplay);
        Version = (TextView) findViewById(R.id.VersionDisplay);
        messageQueueFromCCD = new ArrayBlockingQueue<Message>(1000);
        ReturnToMainMenu = (Button) findViewById(R.id.ReturnToMainMenu);











        ReturnToMainMenu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                returnToMainMenu();

            }
        });

    }

    public void returnToMainMenu() {
        Intent myIntent = new Intent(RunDisplay.this, MainMenu.class);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        RunDisplay.this.startActivity(myIntent);

    }

    public static synchronized RunDisplay getInstance()
    {
        if(inst == null)
        {
            inst = new RunDisplay();

        }
        return inst;
    }



    public void addToQueue(Message messageToCheck)
    {
        messageQueueFromCCD.offer(messageToCheck);
    }


    public void run()
    {
        while(true)
        {
            //Log.d("READ", "while true");

                if(SpeedDisplay!= null) {

                    Log.d("READ", "run: speed display");
                    SpeedDisplay.setText(DisplaySpeed());
                }
                if(TiltDisplay!= null) {
                    Log.d("READ", "run: tilt display");
                    TiltDisplay.setText(DisplayTilt());
                }





        }
    }




    public void checkForValidMessage(Message message) {


        Log.d("READ", "checkForValidMessage: entered");



           messageFromCCD = new Message();
        messageFromCCD = message;

           payloadFromCCD = new Payload();


           if (messageFromCCD!= null) {



               if (messageFromCCD.getHeader() == (byte) (0xE1)) {
                   payloadFromCCD.id.setId(messageFromCCD.payload.id.getId());
                   payloadFromCCD.data.setData(2, messageFromCCD.payload.data.getByte(2));
                   payloadFromCCD.data.setData(1, messageFromCCD.payload.data.getByte(1));
                   payloadFromCCD.data.setData(0, messageFromCCD.payload.data.getByte(0));

                   Log.d("", "checkForValidMessage: meassage ");


               }
           }

           if (payloadFromCCD.id.getId() == (byte) (0x81)) {

               SpeedData2 = payloadFromCCD.data.getByte(2);
               SpeedData1 = (byte) (payloadFromCCD.data.getByte(1) & 0xF0);
               TiltData1 = (byte) (payloadFromCCD.data.getByte(1) & 0x0F);
               TiltData0 = payloadFromCCD.data.getByte(0);
               Log.d("READ", "checkForValidMessage:payload ");
           }


       }

    public String DisplaySpeed()
    {
        byte[] tempByte;
        tempByte = new byte[2];
        tempByte[1] = SpeedData2;
        tempByte[0] = SpeedData1;
        String tempString;
        tempString = tempByte.toString();
        if(tempString!= null)
        {
            return tempString;
        }
        else {
            tempString = "0.0";
        }
        Log.d("READ", "Speed Display: " + tempString);

        return tempString;

    }
    public String DisplayTilt()
    {
        byte[] tempByte;
        tempByte = new byte[2];
        tempByte[1] = TiltData1;
        tempByte[0] = TiltData0;
        String tempString;
        tempString = tempByte.toString();

        if(tempString!= null)
        {
            return tempString;
        }
        else {
            tempString = " 0.0 ";
        }
        Log.d("READ", "DisplayTilt: " + tempString);

        return tempString;
    }


}
