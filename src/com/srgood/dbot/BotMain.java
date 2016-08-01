package com.srgood.dbot;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;

import javax.security.auth.login.LoginException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.srgood.dbot.commands.*;
import com.srgood.dbot.commands.audio.*;
import com.srgood.dbot.ref.RefStrings;
import com.srgood.dbot.utils.CommandParser;
import com.srgood.dbot.utils.PermissionOps;

import net.dv8tion.jda.JDA;
import net.dv8tion.jda.JDABuilder;
import net.dv8tion.jda.entities.Role;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.utils.SimpleLog;

public class BotMain {
	
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
	public static Document PInputFile;
	
	
	public static void main(String[] args) {
		//catch exceptions when building JDA
		//invite temp: https://discordapp.com/oauth2/authorize?client_id=XXXX&scope=bot&permissions=0x33525237
		try  {
			jda = new JDABuilder().addListener(new BotListener()).setBotToken(RefStrings.BOT_TOKEN_REASONS_DEV_1).buildBlocking();
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
			commands.put("delete", new CommandDelete());
			commands.put("version", new CommandVersion());
			commands.put("invite", new CommandInvite());
			
			
		} catch (Exception e) {
			SimpleLog.getLog("Reasons").warn("One or more of the commands failed to map");
			e.printStackTrace();
		}
		
		
	
	}
	
	
	//TODO fix the exceptions here
	public static void WriteXML() throws TransformerException{
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		// Beautify XML
		// Set do indents
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		// Set indent amount
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		transformer.setOutputProperty(OutputKeys.METHOD, "html");
		DOMSource source = new DOMSource(PInputFile);
		StreamResult result = new StreamResult(new File("servers.xml"));
		transformer.transform(source, result);
	}
	
	
	// TODO fix the exceptions here, too
	public static void PutNodes() throws Exception {
	    try {
		DomFactory = DocumentBuilderFactory.newInstance();
		DomInput = DomFactory.newDocumentBuilder();

		File InputFile = new File("servers.xml");

		PInputFile = DomInput.parse(InputFile);
		PInputFile.getDocumentElement().normalize();
		SimpleLog.getLog("Reasons.").info(PInputFile.getDocumentElement().getNodeName());

		// <config> element
		Element rootElem = PInputFile.getDocumentElement();
		// <server> element list
		NodeList ServerNodes = rootElem.getElementsByTagName("server");
		for (int i = 0; i < ServerNodes.getLength(); i++) {
			Node ServerNode = ServerNodes.item(i);
			Element ServerNodeElement = (Element) ServerNode;

			servers.put(ServerNodeElement.getAttribute("id"), ServerNode);
		}
		
		Element globalElem = (Element) rootElem.getElementsByTagName("global").item(0);
		
		prefix = globalElem.getElementsByTagName("prefix").item(0).getTextContent();
	    } catch (Exception e) {
	        e.printStackTrace();
	        throw e;
	    }
	}
	
	final static int[] illegalChars = {34, 60, 62, 124, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 58, 42, 63, 92,46, 47};
	static {
	    Arrays.sort(illegalChars);
	}
	public static String cleanFileName(String badFileName) {
	    StringBuilder cleanName = new StringBuilder();
	    for (int i = 0; i < badFileName.length(); i++) {
	        int c = (int)badFileName.charAt(i);
	        if (Arrays.binarySearch(illegalChars, c) < 0) {
	            cleanName.append((char)c);
	        }
	    }
	    return cleanName.toString();
	}
	
	public static void StoreMessage (GuildMessageReceivedEvent event,Node node){
		
		String truePath = "messages/guilds/" + cleanFileName(event.getGuild().getName()) +"/" + cleanFileName(event.getChannel().getName()) + "/all/";
		try {
			
			FileOutputStream fout = new FileOutputStream(truePath + event.getMessage().getId() + ".ser");
			ObjectOutputStream oos = new ObjectOutputStream(fout);   
			oos.writeObject(event.getMessage().getContent());
			
			oos.close();
			Boolean mentioned = false;
			Element NodeElement = (Element)node;
			if (!event.getMessage().getMentionedUsers().isEmpty()) {
				if ( event.getJDA().getSelfInfo().getAsMention().equals(event.getMessage().getMentionedUsers().get(0).getAsMention())) {
					mentioned = true;
				}
			}
			if (event.getAuthor().isBot() | event.getMessage().getContent().startsWith(NodeElement.getElementsByTagName("prefix").item(0).getTextContent()) | mentioned) {
				FileOutputStream fout2 = new FileOutputStream(truePath.replace("/all/", "/bot/") + event.getMessage().getId() + ".ser");
				ObjectOutputStream oos2 = new ObjectOutputStream(fout2);   
				oos2.writeObject(event.getMessage().getContent());
				
				oos2.close();
			}
		} catch(FileNotFoundException e) {
			
			File file = new File(truePath);
			File file2 = new File(truePath.replace("/all/", "/bot/"));
			
			file.setWritable(true);
			file2.setWritable(true);
			file.mkdirs();
			file2.mkdirs();
			
			StoreMessage(event,node);
			

			

			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		//TODO Rank based delete
	}
	
	public static void handleCommand (CommandParser.CommandContainer cmd) {
		//checks if the typed command is in the list
		

		
		if (commands.containsKey(cmd.invoke)){
			
			if (PermissionOps.getHighestPermission(PermissionOps.getPermissions(cmd.event.getGuild(), cmd.event.getAuthor()), cmd.event.getGuild()).getLevel() >= commands.get(cmd.invoke).permissionLevel().getLevel()) {
				boolean safe = commands.get(cmd.invoke).called(cmd.args,cmd.event);
				if (safe) {
					commands.get(cmd.invoke).action(cmd.args,cmd.event);
					commands.get(cmd.invoke).executed(safe,cmd.event);
				} else {
					commands.get(cmd.invoke).executed(safe,cmd.event);
				}
			}
			
			cmd.event.getChannel().sendMessage("" + PermissionOps.getHighestPermission(PermissionOps.getPermissions(cmd.event.getGuild(), cmd.event.getAuthor()), cmd.event.getGuild()).getLevel());
		}
	}
}
