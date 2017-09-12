package com.jackson.andrew.colorcarvesapp;

import java.nio.ByteBuffer;
import java.security.Timestamp;
import java.util.concurrent.BlockingQueue;

/**
 * Created by User on 9/8/2017.
 */

public class CommThread extends Thread {

    private static final String MY_BACKGROUND_THREAD = "commThread";
    //private static final String TAG = MyBackgroundThread.class.getSimpleName();
    //private BlockingQueue<Message> SendingQueue;
    //private Message UserMessage;
    //private byte [] ByteMessage;
    private CMPPort porttx;// = new CMPPort();



    public CommThread() {
        super(MY_BACKGROUND_THREAD);
        porttx = new CMPPort();
        //ThreadSend = new MainMenu();
        //ByteMessage = new byte[5];


    }

    @Override
    public void run() {
        super.run();
        porttx.run();
        //SendingQueue = ThreadPort.getCMPQueue();
    }
}





