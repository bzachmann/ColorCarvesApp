package com.jackson.andrew.colorcarvesapp;

import android.util.Log;

import java.nio.ByteBuffer;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by User on 9/8/2017.
 */

public  class CMPPort {


    private BlockingQueue<Payload> payloadFifo;
    private static CMPPort cmpPort;
    private byte[] byteBuffer;
    private long timeOld;
    private long sendRateMs = 200;
    private MainMenu mymm;

    CMPPort()
    {
        timeOld = 0;
        byteBuffer = new byte[20];
        this.mymm = mymm.getInstance();
        payloadFifo = new ArrayBlockingQueue<Payload>(50);
    }

    public static synchronized CMPPort getInstance()
    {
        if(cmpPort == null)
        {
            cmpPort = new CMPPort();
        }
        return cmpPort;
    }


    public void init()
    {
        payloadFifo.clear();
        timeOld = System.currentTimeMillis();
    }

    public void run()
    {
        long timeNew = System.currentTimeMillis();
        if (((timeNew - timeOld) > sendRateMs) && (!payloadFifo.isEmpty())) {
            //make sure there is mutual exclusion between threads in the blocking queue
            //semaphore here

            int payloadNumber = 0;
            while ((payloadNumber >= 0) && (payloadNumber < 4) && (!payloadFifo.isEmpty())) {
                Payload tempPayload = new Payload();
                tempPayload = payloadFifo.remove(); //check this

                byteBuffer[(payloadNumber * 5) + 0] = (byte) 0xE1;
                byteBuffer[(payloadNumber * 5) + 1] = tempPayload.id.getId();
                byteBuffer[(payloadNumber * 5) + 2] = tempPayload.data.getByte(2);
                byteBuffer[(payloadNumber * 5) + 3] = tempPayload.data.getByte(1);
                byteBuffer[(payloadNumber * 5) + 4] = tempPayload.data.getByte(0);
                payloadNumber++;
            }


            mymm.sendMessageOverBLE(byteBuffer,(payloadNumber * 5));//send from this buffer, this many bytes

            Log.d("Sending Message", "Should have been sent over BLE: ");

            timeOld = System.currentTimeMillis();
        }

    }

    public void queueToSend(Payload payload)
    {
        payloadFifo.add(payload);
    }
}




//    public Boolean HasTimePassed(long Current, long Past){
//        boolean TimePassed;
//        if ((Current - Past) > 200){
//             TimePassed= true;
//            Current = System.currentTimeMillis()%1000;
//
//        }
//        else{
//            TimePassed = false;
//            Current = System.currentTimeMillis()%1000;
//
//        }
//        return TimePassed;
//    }
//
//
//
//
//}






