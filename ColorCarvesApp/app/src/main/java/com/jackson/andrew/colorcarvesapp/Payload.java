package com.jackson.andrew.colorcarvesapp;

import java.nio.ByteBuffer;

/**
 * Created by User on 9/8/2017.
 */

public class Payload {
    public byte [] Payload;
    public ByteBuffer bb;




    public void SetPayload(Data PayloadData, ID PayloadID){
        bb = ByteBuffer.allocate(20);


        Payload = new byte[4];
        Payload[0] = PayloadID.getIdByte();
        bb.put(PayloadData.GetData());
        Payload[3] = bb.get();
        Payload[2] = bb.get();
        Payload[1] = bb.get();


    }

    public byte[] GetPayload(){

        return Payload;
    }
}
