package com.jackson.andrew.colorcarvesapp;

import java.nio.ByteBuffer;

/**
 * Created by User on 9/8/2017.
 */

public class Message {

    public byte[] Message;
    public ByteBuffer mybb;


    public void SetMessagePayload(Payload payload) {
        Message = new byte[5];
        Message[0] = (byte) (0xE1); //Header Byte
        mybb.put(payload.GetPayload());
        Message[4] = mybb.get();
        Message[3] = mybb.get();
        Message[2] = mybb.get();
        Message[1] = mybb.get();

    }

    public byte[] getMessage() {
        return Message;
    }




}


