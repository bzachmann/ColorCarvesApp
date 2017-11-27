package com.jackson.andrew.colorcarvesapp;

/**
 * Created by User on 10/31/2017.
 */

public class SpeedandTiltMessageHandler extends CMPDataHandler {

    private Byte SpeedData2;
    private Byte SpeedData1;
    private Byte TiltData1;
    private Byte TiltData0;
    private Byte id = (byte)(0x81);
    public static SpeedandTiltMessageHandler inst;








    public void init()
    {
        this.setId(id);
        CMPPayloadHandler.getInstance().addIDToList(this);

    }




    public void CallBack (Data validData)
    {
        byte tempByte;

        SpeedData2 = validData.getByte(2);
        SpeedData1 =(byte)(validData.getByte(1) & 0xF0);
        TiltData1 = (byte)(validData.getByte(1) & 0x0F);
        TiltData0 = validData.getByte(0);

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
            tempString = " ";
        }
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
            tempString = " ";
        }
        return tempString;

    }

    public static synchronized SpeedandTiltMessageHandler getInstance()
    {
        if(inst == null)
        {
            inst = new SpeedandTiltMessageHandler();

        }
        return inst;
    }



}
