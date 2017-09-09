package com.jackson.andrew.colorcarvesapp;

import java.nio.ByteBuffer;

/**
 * Created by User on 9/8/2017.
 */

public class CMPPort {


    MessageQueue CMPQueue;



    public Boolean HasTimePassed(long Current, long Past){
        boolean TimePassed;
        if ((Current - Past) > 200){
             TimePassed= true;
            Current = System.currentTimeMillis()%1000;

        }
        else{
            TimePassed = false;
            Current = System.currentTimeMillis()%1000;

        }
        return TimePassed;
    }

    public MessageQueue PackMesage(MessageQueue Queue){

        while(!Queue.isEmpty() && CMPQueue.QueueSize() != 4){  //Still messages in queue or on buffer

            Message Tosend;
            Tosend = Queue.dequeue();

            CMPQueue.enqueue(Tosend);

            }
            return CMPQueue;
        }

    }






