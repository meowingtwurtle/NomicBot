package com.srgood.dbot.utils;

import javax.xml.transform.TransformerException;

import com.srgood.dbot.BotMain;

public class ShutdownThread extends Thread {
	
	public void start() {
		
	try {
		BotMain.writeXML();
	} catch (TransformerException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	BotMain.jda.shutdown();
	
	}
}
