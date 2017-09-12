package com.jackson.andrew.colorcarvesapp;

/**
 * Created by User on 9/8/2017.
 */

//ID of the message

public class ID {

    private byte id = (byte)0xFF;

    ID()
    {
        this.id = (byte)0xFF;
    }
    public void setId(byte value)
    {
        id = value;
    }
    public Byte getId()
    {
        return id;
    }
}
