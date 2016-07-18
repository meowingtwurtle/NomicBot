package com.srgood.dbot;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.security.auth.login.LoginException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.srgood.dbot.commands.*;
import com.srgood.dbot.commands.audio.*;
import com.srgood.dbot.utils.CommandParser;
import com.srgood.dbot.utils.SecureOkeyGen;

import net.dv8tion.jda.JDA;
import net.dv8tion.jda.JDABuilder;
import net.dv8tion.jda.utils.SimpleLog;

public class Main {
	
	public static JDA jda;
	
	//Global prefix and shutdown override key
	public static String prefix;
	public static String Okey;
	
	public static final CommandParser parser = new CommandParser();
	public static HashMap<String, Command> commands = new HashMap<String, Command>(); 
	public static HashMap<String,Node> servers = new HashMap<String,Node>();
	
	//XML variables
	public static DocumentBuilderFactory DomFactory;
	public static DocumentBuilder DomInput;
	
	
	public static void main(String[] args) {
		//catch exceptions when building JDA
		try  {
			jda = new JDABuilder().addListener(new BotListener()).setBotToken("MjAxODEwODM4MDcwMzYyMTEy.CmROfQ.8taYC1Hiv3T-GjRi1bBObAcCXlg").buildBlocking();
			jda.setAutoReconnect(true);
			jda.getAccountManager().setGame("type '@Reasons help'");
		} catch(LoginException e) {
			SimpleLog.getLog("Reasons").fatal("**COULD NOT LOG IN**");
		} catch (InterruptedException e) {
			SimpleLog.getLog("JDA").fatal("**AN UNKNOWWN ERROR OCCURED DURING LOGIN**");
			e.printStackTrace();
		}
		
		//load global paramaters
		try {
			PutNodes();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//TODO make the null checks modular and in the LoadParams method, not here
		
		//SimpleLog.getLog("Reasons").info("Override Key: " + Okey);
		
		//catch null pointer exceptions when creating commands
		try {
			commands.put("ping", new CommandPing());
			commands.put("pong", new CommandPong());
			commands.put("shutdown", new CommandShutdown());
			commands.put("setprefix", new CommandSetPrefix());
			commands.put("getprefix", new CommandGetPrefix());
			commands.put("debug",  new CommandDebug());
			commands.put("volume", new CommandAudioVolume());
			commands.put("list", new CommandAudioList());
			commands.put("now-playing", new CommandAudioNowPlaying());
			commands.put("join", new CommandAudioJoin());
			commands.put("leave", new CommandAudioLeave());
			commands.put("play", new CommandAudioPlay());
			commands.put("skip", new  CommandAudioSkip());
			commands.put("stop", new CommandAudioStop());
			commands.put("pause", new CommandAudioPause());
			commands.put("help", new CommandHelp());
			commands.put("repeat", new CommandAudioRepeat());
			
		} catch (Exception e) {
			SimpleLog.getLog("Reasons").warn("One or more of the commands failed to map");
			e.printStackTrace();
		}
		
		
	
	}
	
	
	//TODO fix the exceptions here
	public static void SaveParams(){
		
		
		
	}
	
	
	//TODO fix the exceptions here, too
	public static void PutNodes() throws Exception  {
		DomFactory = DocumentBuilderFactory.newInstance();
		DomInput = DomFactory.newDocumentBuilder();
		
		File InputFile = new File("servers.xml");
		
		Document PInputFile = DomInput.parse(InputFile);
		PInputFile.getDocumentElement().normalize();
		SimpleLog.getLog("Reasons.").info(PInputFile.getDocumentElement().getNodeName());
		NodeList ServerNodes = PInputFile.getElementsByTagName("server");
			for(int i = 0; i < ServerNodes.getLength(); i++) {
				Node ServerNode = ServerNodes.item(i);
				Element ServerNodeElement = (Element) ServerNode;
				
				servers.put(ServerNodeElement.getAttribute("id"), ServerNode);
			}
		
		
	}
	
	public static void handleCommand (CommandParser.CommandContainer cmd) {
		//checks if the typed command is in the list
		if (commands.containsKey(cmd.invoke)){
			boolean safe = commands.get(cmd.invoke).called(cmd.args,cmd.event);
			if (safe) {
				commands.get(cmd.invoke).action(cmd.args,cmd.event);
				commands.get(cmd.invoke).executed(safe,cmd.event);
			} else {
				commands.get(cmd.invoke).executed(safe,cmd.event);
			}
		}
	}
}
