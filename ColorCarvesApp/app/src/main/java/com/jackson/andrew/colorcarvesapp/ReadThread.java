package com.jackson.andrew.colorcarvesapp;

/**
 * Created by User on 11/6/2017.
 */

public class ReadThread extends Thread {


        private static final String MY_BACKGROUND_THREAD = "readThread";

        private RunDisplay runTimeInformation;// = new CMPPort();



        public ReadThread(RunDisplay runDisplay) {
            super(MY_BACKGROUND_THREAD);
            runTimeInformation =runDisplay;


        }

        @Override
        public void run() {
            super.run();


            while(true) {
                runTimeInformation.run();
            }
            //SendingQueue = ThreadPort.getCMPQueue();
        }
    }

