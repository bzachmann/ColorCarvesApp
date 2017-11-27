package com.jackson.andrew.colorcarvesapp;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

/**
 * Created by User on 10/25/2017.
 */

public class CMPPayloadHandler {
    private CMPPortRx Rx;
    private static CMPPayloadHandler inst;
    private List<CMPDataHandler> registeredDataHandlers;
    private BlockingQueue<Payload> payloadFifo;
    private CMPDataHandler tempHandler;


    public void init()
    {
        registeredDataHandlers = new ArrayList<CMPDataHandler>();

    }

    public void run() {
        Rx = CMPPortRx.getInstance();
        Payload tempPayload;
        payloadFifo = Rx.getPayloadFifo();


        if (!payloadFifo.isEmpty()) {
            tempPayload = payloadFifo.remove();
            for (int i = 0; i < registeredDataHandlers.size(); i++) {
                if (tempPayload.id.equals(registeredDataHandlers)) {
                    tempHandler = registeredDataHandlers.get(i);
                    tempHandler.CallBack(tempPayload.data);

                }
            }


        }


    }

    public void addIDToList (CMPDataHandler newHandler)
    {


        registeredDataHandlers.add(newHandler);

    }

    public static synchronized CMPPayloadHandler getInstance()
    {
        if(inst == null)
        {
            inst = new CMPPayloadHandler();

        }
        return inst;
    }
}
