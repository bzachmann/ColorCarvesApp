package com.jackson.andrew.colorcarvesapp;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by User on 10/24/2017.
 */

public class CMPPortRx
{
    private BlockingQueue<Payload> payloadFifo;
    private static CMPPortRx inst;
    private byte[] receivedBytes;
    private boolean HeaderRecived = false;
    private boolean IDRecivied = false;
    private int DataCount = 2;






    CMPPortRx()
    {


        payloadFifo = new ArrayBlockingQueue<Payload>(50);
    }

    public static synchronized CMPPortRx getInstance()
    {
        if(inst == null)
        {
            inst = new CMPPortRx();
            inst.init();
        }
        return inst;
    }

    public void init()
    {
        payloadFifo.clear();
    }

    public void run()
    {
        receivedBytes = new byte[20];  // 5 messages longest allowed for BLE
        boolean NewData;


        if(true)
        {
            Payload myPayload = new Payload();
            for(int x = 0; x< 20; x ++)
            {
                byte tempByte = receivedBytes[x];
                if(!HeaderRecived)
                {
                    if(tempByte == (byte)(0xE1))
                    {
                        HeaderRecived = true;
                        IDRecivied = false;
                    }
                }
                else if(!IDRecivied)
                {
                    myPayload.id.setId(tempByte);
                    IDRecivied = true;
                    DataCount = 2;
                }
                else if(DataCount >= 0)
                {
                    myPayload.data.setData(DataCount,tempByte);
                    DataCount --;
                    if(DataCount < 0)
                    {
                        payloadFifo.add(myPayload);
                        HeaderRecived = false;
                        IDRecivied = false;
                        DataCount = 2;

                    }
                }
            }


        }
    }

    public BlockingQueue<Payload> getPayloadFifo()
    {
        return payloadFifo;
    }






}
