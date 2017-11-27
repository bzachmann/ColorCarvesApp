
package com.jackson.andrew.colorcarvesapp;

        import android.os.SystemClock;
        import android.util.Log;

        import java.util.concurrent.ArrayBlockingQueue;
        import java.util.concurrent.Semaphore;

public  class CMPPortTx {

    private ArrayBlockingQueue<Payload> payloadFifo;
    private static CMPPortTx inst;
    private byte[] byteBuffer;
    private long timeOld;
    private long sendRateMs = 200;
    private Semaphore sem;
    private int payloadNumber = 0;

    CMPPortTx()
    {
        timeOld = 0;
        byteBuffer = new byte[20];
        payloadFifo = new ArrayBlockingQueue<Payload>(1000);
    }

    public static synchronized CMPPortTx getInstance()
    {
        if (inst == null)
        {
            inst = new CMPPortTx();
            inst.init();
        }
        return inst;
    }

    public void init() {
        payloadFifo.clear();
        timeOld = System.currentTimeMillis();
        sem = new Semaphore(1);
    }

    public void run()
    {
        long timeNew = System.currentTimeMillis();
        if (((timeNew - timeOld) > sendRateMs) && (!payloadFifo.isEmpty()))  //Been 200ms and not empty
        {
            payloadNumber = 0;
            while (queueNotEmpty() && (payloadNumber < 4))  //only true if payloadfifo is not empty and less then 20 bytes per 200ms
            {
                Payload tempPayload = removeFromQueue();
                byteBuffer[(payloadNumber * 5) + 0] = (byte) 0xE1;
                byteBuffer[(payloadNumber * 5) + 1] = tempPayload.id.getId();
                byteBuffer[(payloadNumber * 5) + 2] = tempPayload.data.getByte(2);
                byteBuffer[(payloadNumber * 5) + 3] = tempPayload.data.getByte(1);
                byteBuffer[(payloadNumber * 5) + 4] = tempPayload.data.getByte(0);
                payloadNumber++;
            }

            MainMenu.getInstance().sendMessageOverBLE(byteBuffer, (payloadNumber * 5));//send from this buffer, this many bytes

            timeOld = System.currentTimeMillis(); //get new time
        }
    }


    public void queueToSend(Payload payload)
    {
        //sem.acquire();
        payloadFifo.offer(payload); //Place message on Queue
        //sem.release();
    }

    public Payload removeFromQueue()
    {
        Payload payloadToSend = new Payload();
        if (queueNotEmpty())
        {
            //sem.acquire();
            payloadToSend = payloadFifo.poll();  //Not empty so remove top element
            //sem.release();
        }
        return payloadToSend;
    }

    public boolean queueNotEmpty()
    {
        //sem.acquire();
        boolean retVal = !payloadFifo.isEmpty();
        //sem.release();
        return retVal;
    }
}















