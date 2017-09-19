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
    private static CMPPort inst;
    private byte[] byteBuffer;
    private long timeOld;
    private long sendRateMs = 200;
    //private MainMenu mymm;

    CMPPort()
    {
        timeOld = 0;
        byteBuffer = new byte[20];
        //mymm = mymm.getInstance();

        payloadFifo = new ArrayBlockingQueue<Payload>(50);
    }

    public static synchronized CMPPort getInstance()
    {
        if(inst == null)
        {
            inst = new CMPPort();
            inst.init();
        }
        return inst;
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


            MainMenu.getInstance().sendMessageOverBLE(byteBuffer,(payloadNumber * 5));//send from this buffer, this many bytes
            Log.d("message", " "+ byteBuffer);
            Log.d("Sending Message", "Should have been sent over BLE: ");

            timeOld = System.currentTimeMillis();
        }

    }

    public void testSend()
    {

        byteBuffer[0] = (byte)0x01;
        byteBuffer[1] = (byte)0x02;
        byteBuffer[2] = (byte)0x03;
        byteBuffer[3] = (byte)0x04;
        byteBuffer[4] = (byte)0x05;




        MainMenu.getInstance().sendMessageOverBLE(byteBuffer,(5));//send from this buffer, this many bytes
        Log.d("message", " "+ byteBuffer);
        Log.d("Sending Message", "Should have been sent over BLE: ");

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






