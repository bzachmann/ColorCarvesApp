package com.jackson.andrew.colorcarvesapp;

/**
 * Created by User on 9/8/2017.
 */

public class Data {

    private byte[] data = new byte[3];
    private int dataByteLength = 3;

    public Data()
    {
        data[0] = (byte)0xFF;
        data[1] = (byte)0xFF;
        data[2] = (byte)0xFF;
    }

    public boolean setData(int index, byte value)
    {
        boolean retVal = false;
        if((index >= 0) && (index < dataByteLength))
        {
            data[index] = value;
            retVal = true;
        }
        return retVal;
    }

     public byte getByte(int index)
    {
        byte retVal = (byte)0xFF;
        if((index >= 0) && (index < dataByteLength))
        {
            retVal = data[index];
        }
        return retVal;
    }
}
