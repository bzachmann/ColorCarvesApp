package com.jackson.andrew.colorcarvesapp;

import android.util.Log;

import java.util.concurrent.BlockingQueue;

/**
 * Created by User on 10/24/2017.
 */

public class CMPPortRx
{
    private BlockingQueue<Payload> payloadFifo;
    private static CMPPortRx inst;

    private Message messageFromCCD;
    private Payload payloadFromCCD;
    private Byte SpeedData2 = (byte)0x00;
    private Byte SpeedData1 = (byte)0x00;
    private Byte TiltData1 = (byte)0x00;
    private Byte TiltData0 = (byte)0x00;
    private byte[] data;








    public static synchronized CMPPortRx getInstance()
    {
        if(inst == null)
        {
            inst = new CMPPortRx();
        }
        return inst;
    }




    public void run()
    {

        RunDisplay.getInstance().getSpeed().setText(DisplaySpeed());
        RunDisplay.getInstance().getTilt().setText(DisplayTilt());

    }




    public BlockingQueue<Payload> getPayloadFifo()
    {
        return payloadFifo;
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
        byte [] tempByte;
        StringBuilder sbSpeed = new StringBuilder();
        tempByte = new byte[2];
        tempByte[1] = SpeedData2;
        tempByte[0] = SpeedData1;
        String tempString;
        tempString = tempByte.toString();
        if(tempString!= null) {
            for (byte b : tempByte) {

                sbSpeed.append(String.format("%02X", b));


            }
            tempString = sbSpeed.toString();
            tempString = hexValueToDecimal(tempString);
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
        StringBuilder sbTilt = new StringBuilder();
        tempByte = new byte[2];
        tempByte[1] = TiltData1;
        tempByte[0] = TiltData0;
        String tempString;
        tempString = tempByte.toString();

        if(tempString!= null)
        {

            for(byte b: tempByte)
            {

                sbTilt.append(String.format("%02X", b));

            }
            tempString = sbTilt.toString();

           tempString = hexValueToDecimal(tempString);



            return tempString;
        }
        else {
            tempString = " 0.0 ";
        }
        Log.d("READ", "DisplayTilt: " + tempString);

        return tempString;
    }


    public String hexValueToDecimal (String hexvalue)
    {
        String tempString;

      tempString = String.valueOf(Integer.parseInt(hexvalue,16));

        return tempString;





    }
    }







