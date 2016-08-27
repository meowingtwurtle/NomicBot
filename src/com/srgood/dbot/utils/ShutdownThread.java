package com.srgood.dbot.utils;

import com.srgood.dbot.BotMain;

import javax.xml.transform.TransformerException;

public class ShutdownThread extends Thread {

    public void start() {

        try {
            BotMain.writeXML();
        } catch (TransformerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        BotMain.jda.shutdown();

        System.out.println("Successfully shutdown from termination");
    }
}
