package com.jackson.andrew.colorcarvesapp;

import java.nio.ByteBuffer;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by User on 9/8/2017.
 */

public class CMPPort {


   public BlockingQueue<Message> CMPQueue = new ArrayBlockingQueue<Message>(66);





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

    public void PackMesage(Message SendMessage){


            CMPQueue.add(SendMessage);

            }




        public Message SendViaThread(){
                int x = 0;
           if(!CMPQueue.isEmpty() && ( x  < 4)){    //Send up to 4 messages if available
               Message MessageReadyToBeSent;
               MessageReadyToBeSent = CMPQueue.remove();
               x++;
               return MessageReadyToBeSent;
           }
           return null;
        }

    public BlockingQueue<Message> getCMPQueue() {
        return CMPQueue;
    }
}






