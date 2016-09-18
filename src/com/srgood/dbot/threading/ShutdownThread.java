package com.srgood.dbot.threading;

import com.srgood.dbot.BotMain;
import com.srgood.dbot.utils.SaveUtils;

import javax.xml.transform.TransformerException;

public class ShutdownThread extends Thread {

    public void start() {

        try {
            SaveUtils.writeXML();
        } catch (TransformerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        BotMain.jda.shutdown();

        System.out.println("Successfully shutdown from termination");
    }
}
