package com.jackson.andrew.colorcarvesapp;

/**
 * Created by User on 11/6/2017.
 */

public class ReadThread extends Thread {


        private static final String MY_BACKGROUND_THREAD = "readThread";

        private CMPPortRx cmpPortRx;// = new CMPPort();



        public ReadThread(CMPPortRx RX) {
            super(MY_BACKGROUND_THREAD);
            cmpPortRx =RX;


        }

        @Override
        public void run() {
            super.run();


            while(true) {
                cmpPortRx.run();
            }
            //SendingQueue = ThreadPort.getCMPQueue();
        }
    }

