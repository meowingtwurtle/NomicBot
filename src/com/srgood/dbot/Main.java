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
	public static Map<String, Map<String, String>> servers = new HashMap<String, Map<String, String>>();
	
	//.properties streams
	public static Properties prop = new Properties();
	public static OutputStream out = null;
	public static InputStream input = null;
	
	//hash streams
	public static OutputStream HashFOS = null;
	public static InputStream HashFIS = null;
	public static ObjectOutputStream HashOOS = null;
	public static ObjectInputStream HashOIS = null;
	
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
		boolean loaded = LoadParams();
		
		//TODO make the null checks modular and in the LoadParams method, not here
		if (loaded & Okey != null & prefix 	!= null) {
			SimpleLog.getLog("Reasons").info("Paramaters loaded successfully!");
		} else {
			SimpleLog.getLog("Reasons").warn("Paramaters not loaded! Setting to defaults!");
			prefix = "#!";
		}
		SimpleLog.getLog("Reasons").info("Override Key: " + Okey);
		
		//catch null pointer exceptions when creating commands
		try {
			commands.put("ping", new Ping());
			commands.put("pong", new Pong());
			commands.put("shutdown", new Shutdown());
			commands.put("setprefix", new SetPrefix());
			commands.put("debug",  new debug());
			commands.put("volume", new Volume());
			commands.put("list", new ListCmd());
			commands.put("now-playing", new NowPlaying());
			commands.put("join", new Join());
			commands.put("leave", new Leave());
			commands.put("play", new Play());
			commands.put("skip", new  Skip());
			commands.put("stop", new Stop());
			commands.put("pause", new Pause());
			commands.put("help", new Help());
			commands.put("repeat", new Repeat());
			commands.put("getprefix", new GetPrefix());
			
		} catch (Exception e) {
			SimpleLog.getLog("Reasons").warn("One or more of the commands failed to map");
			e.printStackTrace();
		}
		
		
	
	}
	
	//TODO fix the exceptions here
	public static void SaveParams() {
		try {
			out = new FileOutputStream("config.properties");
			HashFOS = new FileOutputStream("servers.ser");
			HashOOS = new ObjectOutputStream(HashFOS);
			// set the properties value 
			Okey = SecureOkeyGen.nextSessionId();
			prop.setProperty("GlobalPrefix", prefix);
			prop.setProperty("Override", Okey);
			prop.store(out, null);
			HashOOS.writeObject(servers);
			HashFOS.close();
			HashOOS.close();

		} catch (IOException io) {
			SimpleLog.getLog("Reasons").warn("Error when saving paramaters, no file exists");
				try {
					@SuppressWarnings("unused")
					File file = new File("config.properties");
					prop.setProperty("GlobalPrefix", prefix);
					prop.setProperty("Override", Okey);
					//prop.setProperty("server-settings", servers);
					prop.store(out, null);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	
	//TODO fix the exceptions here, too
	public static Boolean LoadParams() {
		try {
			input = new FileInputStream("config.properties");
			HashFIS = new FileInputStream("servers.ser");
			HashOIS = new ObjectInputStream(HashFIS);
			servers = (HashMap)HashOIS.readObject();
			
			if (servers == null) {
				servers = new HashMap<String, Map<String, String>>();
			}
			
			
			// load a properties file
			prop.load(input);
			// get the property value and print it out
			String preprefix = prop.getProperty("GlobalPrefix");
			if (preprefix != null) {
				prefix = preprefix;
				}
			Okey = prop.getProperty("Override");

		} catch (IOException ex) {
			ex.printStackTrace();
			return false;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
					return false;
				}
			} else {
				return true;
			}
		}
		return true;
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
