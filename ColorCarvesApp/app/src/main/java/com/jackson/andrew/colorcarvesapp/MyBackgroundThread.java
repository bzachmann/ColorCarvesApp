package com.jackson.andrew.colorcarvesapp;

import java.nio.ByteBuffer;
import java.security.Timestamp;

/**
 * Created by User on 9/8/2017.
 */

public class MyBackgroundThread extends Thread {

    private static final String MY_BACKGROUND_THREAD = "my background thread";
    private static final String TAG = MyBackgroundThread.class.getSimpleName();
    private MessageQueue ThreadQueue;
    private MessageQueue SendingQueue;
    private byte [] ByteMessage;
    private CMPPort ThreadPort;
    private MainMenu ThreadSend;
    private long CurrentTime =  System.currentTimeMillis() % 1000;
    private long PastTime = System.currentTimeMillis()%1000;


    public MyBackgroundThread(MessageQueue queue) {
        super(MY_BACKGROUND_THREAD);
        ThreadQueue = queue;


    }

    @Override
    public void run() {
        super.run();
        ThreadPort = new CMPPort();
        ThreadSend = new MainMenu();
        ByteMessage = new byte[5];


        while (true) {

            if (ThreadPort.HasTimePassed(CurrentTime, PastTime)) {

                    // Time has passsed so BLE can except a new message
            }

            SendingQueue = ThreadPort.PackMesage(ThreadQueue);   //Packs message sent from UI

            while (!SendingQueue.isEmpty()) {  //IF message us waiting on queue it should be sent

                Message UserMessage = SendingQueue.dequeue();  //First message on queue
                ByteMessage = GetBytesOfMessage(UserMessage);  //Sending message needs to happen with bytes

                ThreadSend.SendMessage(ByteMessage);  //Sending bytes via BLE


            }

            PastTime = System.currentTimeMillis() % 1000;  //update time to determine if another message can be sent

        }
    }

            private byte[] GetBytesOfMessage(Message mymessage){  //unpacks the message into bytes to be sent
                byte[] tempMessage;
                tempMessage = new byte[5];
                tempMessage = mymessage.getMessage();

                return tempMessage;


    }





        }


