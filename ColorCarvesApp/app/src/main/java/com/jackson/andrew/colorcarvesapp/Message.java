package com.jackson.andrew.colorcarvesapp;

import java.nio.ByteBuffer;

/**
 * Created by User on 9/8/2017.
 */

public class Message {

    private byte header = (byte)0xE1;
    public Payload payload = new Payload();

    Message()
    {

    }

    public void setHeader(byte value)
    {
        header = value;
    }

    public byte getHeader()
    {
        return header;
    }
}


