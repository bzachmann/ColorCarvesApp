package com.jackson.andrew.colorcarvesapp;

/**
 * Created by User on 9/8/2017.
 */

public class Data {

    public byte[] Data;


    public void SetData(int ByteNumber, byte SetData){

        Data = new byte[3];
        if(ByteNumber == 0){

            Data[0] = SetData;
        }
        if(ByteNumber == 1){

            Data[1] = SetData;
        }
        if(ByteNumber==2){
            Data[2] = SetData;
        }


    }

    public byte [] GetData(){

        return Data;
    }



}
