package com.jackson.andrew.colorcarvesapp;

/**
 * Created by User on 9/8/2017.
 */

public class CommThread extends Thread {

    private static final String MY_BACKGROUND_THREAD = "commThread";

    private CMPPortTx porttx;// = new CMPPort();



    public CommThread(CMPPortTx cmpPort) {
        super(MY_BACKGROUND_THREAD);
        porttx =cmpPort;
        //ThreadSend = new MainMenu();
        //ByteMessage = new byte[5];


    }

    @Override
    public void run() {
        super.run();
        porttx.init();

        while(true) {
            porttx.run();
        }
        //SendingQueue = ThreadPort.getCMPQueue();
    }
}





