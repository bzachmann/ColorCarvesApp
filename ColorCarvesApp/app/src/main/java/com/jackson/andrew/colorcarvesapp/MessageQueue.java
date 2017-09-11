package com.jackson.andrew.colorcarvesapp;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by User on 9/8/2017.
 */

public class MessageQueue {
    private BlockingQueue<Message> MyMessageQueue;




    public boolean isEmpty()
    // Post: Returns true if the queue is empty. Otherwise, false.
    {
        return (MyMessageQueue.size() == 0);
    }

    public void enqueue(Message MessageToQueue)   //Add Message to Queue

    {
        // Append the item to the end of our linked list. Back of the Queue
        MyMessageQueue.add(MessageToQueue);
    }

    public Message dequeue()

    {
        Message MyMessage;
        MyMessage =MyMessageQueue.remove();


        // Return the item
        return MyMessage;   //This will be used as the message to be sent from BLE
    }

    public int QueueSize(){
        int temp;
        temp = MyMessageQueue.size();

        return temp;

    }

    public BlockingQueue<Message> getMyMessageQueue() {
        return MyMessageQueue;
    }


    public void setMyMessageQueue(BlockingQueue<Message> myMessageQueue) {
        MyMessageQueue = myMessageQueue;
    }
}


