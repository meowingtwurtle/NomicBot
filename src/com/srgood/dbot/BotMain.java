
package com.srgood.dbot; 

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.time.Instant;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

import javax.security.auth.login.LoginException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.reflections.Reflections;
import org.w3c.dom.Document;

import com.srgood.dbot.commands.Command;
import com.srgood.dbot.ref.RefStrings;
import com.srgood.dbot.utils.CommandParser;
import com.srgood.dbot.utils.PermissionOps;
import com.srgood.dbot.utils.ShutdownThread;
import com.srgood.dbot.utils.XMLHandler;

import net.dv8tion.jda.JDA;
import net.dv8tion.jda.JDABuilder;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.utils.SimpleLog;

public class BotMain {
	
	public static JDA jda;
	
	//Global
	public static Instant startInstant = Instant.now();
    // prefix and shutdown override key
	public static String prefix;
	public static String Okey;
	
	public static final CommandParser parser = new CommandParser();
	public static Map<String, Command> commands = new TreeMap<String, Command>(); 

	//XML variables
	public static DocumentBuilderFactory DomFactory;
	public static DocumentBuilder DomInput;
	public static Document PInputFile;
	
	
	public static void main(String[] args) {
	    
		//catch exceptions when building JDA
		//invite temp: https://discordapp.com/oauth2/authorize?client_id=XXXX&scope=bot&permissions=0x33525237
		
		Runtime.getRuntime().addShutdownHook(new ShutdownThread());
		
		try  {
			jda = new JDABuilder().addListener(new BotListener()).setBotToken(RefStrings.BOT_TOKEN_REASONS).buildBlocking();
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
			XMLHandler.initStorage();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//TODO make the null checks modular and in the LoadParams method, not here
		
		SimpleLog.getLog("Reasons").info("Session override Key: " + Okey);
		
		//catch null pointer exceptions when creating commands
		try {
		    String[] packages = {"com.srgood.dbot", "com.srgood.dbot.audio"};

            for (String pack : packages) {
                Reflections mReflect = new Reflections(pack);
                for (Class<? extends Command> cmdClass : mReflect.getSubTypesOf(Command.class)) {
                    if (!cmdClass.isInterface()) {
                        commands.put(cmdClass.getSimpleName().replaceAll("Command", "").toLowerCase(), cmdClass.newInstance());
                    }
                }
            }
		} catch (Exception e) {
			SimpleLog.getLog("Reasons").warn("One or more of the commands failed to map");
			e.printStackTrace();
		}
		
		
	
	}
	
	
	//TODO fix the exceptions here
	public static void writeXML() throws TransformerException{
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
		
		cleanFile();
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
	
	public static void storeMessage (GuildMessageReceivedEvent event){
		
		String truePath = "messages/guilds/" + cleanFileName(event.getGuild().getName()) +"/" + cleanFileName(event.getChannel().getName()) + "/all/";
		try {
			
			FileOutputStream fout = new FileOutputStream(truePath + event.getMessage().getId() + ".ser");
			ObjectOutputStream oos = new ObjectOutputStream(fout);   
			oos.writeObject(event.getMessage().getContent());
			
			oos.close();
			Boolean mentioned = false;
			if (!event.getMessage().getMentionedUsers().isEmpty()) {
				if ( event.getJDA().getSelfInfo().getAsMention().equals(event.getMessage().getMentionedUsers().get(0).getAsMention())) {
					mentioned = true;
				}
			}
			if (event.getAuthor().isBot() | event.getMessage().getContent().startsWith(XMLHandler.getGuildPrefix(event.getGuild())) | mentioned) {
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
			
			storeMessage(event);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		//TODO Rank based delete
	}
    public static void handleCommand(CommandParser.CommandContainer cmd) {
        // checks if the typed command is in the lis i
        if (commands.containsKey(cmd.invoke)) {
            
            XMLHandler.initCommandIfNotExists(cmd);
            
            if (XMLHandler.commandIsEnabled(
                    cmd.event.getGuild(),
                    BotMain.commands.get(cmd.invoke))) {
            	if (PermissionOps
                        .getHighestPermission(PermissionOps.getPermissions(cmd.event.getGuild(), cmd.event.getAuthor()),
                                cmd.event.getGuild())
                        .getLevel() >= commands.get(cmd.invoke).permissionLevel(cmd.event.getGuild()).getLevel()) {
                    boolean safe = commands.get(cmd.invoke).called(cmd.args, cmd.event);
                    if (safe) {
                        commands.get(cmd.invoke).action(cmd.args, cmd.event);
                        commands.get(cmd.invoke).executed(safe, cmd.event);
                    } else {
                        commands.get(cmd.invoke).executed(safe, cmd.event);
                    }
                } else {
                	cmd.event.getChannel().sendMessage("You lack the required permission to preform this action");
                }
            } else {
            	cmd.event.getChannel().sendMessage("This command is disabled");
            }
        } 
    }
    
    public static void cleanFile() {
        
        try (FileReader fr = new FileReader("servers.xml"); FileWriter fw = new FileWriter("temp.xml"); ) {
            BufferedReader br = new BufferedReader(fr); 
            String line;

            while((line = br.readLine()) != null)
            { 
                if (!line.trim().equals("")) // don't write out blank lines
                {
                    line = line.replace("\n", "").replace("\f", "").replace("\r", "");
                    fw.write(line + "\n", 0, line.length() + 1);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        try {
            Files.deleteIfExists(new File("servers.xml").toPath());
            Files.move(new File("temp.xml").toPath(), new File("servers.xml").toPath());
            Files.deleteIfExists(new File("temp.xml").toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Command getCommandByName(String name) {
        return commands.get(name);
    }
    
    public static String getNameFromCommand(Command cmd) {
        for (String s : commands.keySet()) {
            if (commands.get(s) == cmd) {
                return s;
            }
        }
        return null;
    }
}
