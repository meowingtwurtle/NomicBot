package com.srgood.dbot;

import com.srgood.dbot.commands.Command;
import com.srgood.dbot.commands.CommandParser;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.dv8tion.jda.JDA;
import net.dv8tion.jda.JDABuilder;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.utils.SimpleLog;
import org.reflections.Reflections;
import org.w3c.dom.Document;

import javax.imageio.ImageIO;
import javax.security.auth.login.LoginException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.time.Instant;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

public class BotMain extends Application {

    public static JDA jda;

    //Global
    public static final Instant startInstant = Instant.now();
    // prefix and shutdown override key
    public static String prefix;
    public static String overrideKey = com.srgood.dbot.utils.SecureOverrideKeyGenerator.nextOverrideKey();
    public static PrintStream outPS,errOutPS;
    public static ByteArrayOutputStream out,errOut;

    public static final CommandParser parser = new CommandParser();
    public static Map<String, Command> commands = new TreeMap<>();

    //XML variables
    public static DocumentBuilderFactory DomFactory;
    public static DocumentBuilder DomInput;
    public static Document PInputFile;

    private static com.srgood.dbot.app.Controller controller = null;

    private boolean firstTime;
    private TrayIcon trayIcon;

    @Override public void init() {
        out = new ByteArrayOutputStream();
        errOut = new ByteArrayOutputStream();

        outPS = new PrintStream(out) {
            @Override
            public void println(Object o) {
                super.println(o);
                if (controller != null)
                    controller.updateConsole();
            }

            @Override
            public void println(String s) {
                super.println(s);
                if (controller != null)
                    controller.updateConsole();
            }
        };

        errOutPS = new PrintStream(errOut) {
            @Override
            public void println(Object o) {
                super.println(o);
                if (controller != null)
                    controller.updateErrConsole();
            }

            @Override
            public void println(String s) {
                super.println(s);
                if (controller != null)
                    controller.updateErrConsole();
            }

        };



        System.setOut(outPS);
        System.setErr(errOutPS);

        //catch exceptions when building JDA
        //invite temp: https://discordapp.com/oauth2/authorize?client_id=XXXX&scope=bot&permissions=0x33525237


        try {
            //create a JDA with one Event listener
            jda = new JDABuilder().addListener(new BotListener()).setBotToken(Reference.Strings.BOT_TOKEN_REASONS_DEV_2).buildBlocking();
            jda.setAutoReconnect(true);
            jda.getAccountManager().setGame("type '@Reasons help'");
        } catch (LoginException e) {
            SimpleLog.getLog("Reasons").fatal("**COULD NOT LOG IN**");
        } catch (InterruptedException e) {
            SimpleLog.getLog("JDA").fatal("**AN UNKNOWN ERROR OCCURRED DURING LOGIN**");
            e.printStackTrace();
        }

        //load global parameters
        try {
            com.srgood.dbot.utils.XMLUtils.initStorage();
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        //TODO make the null checks modular and in the LoadParams method, not here

        SimpleLog.getLog("Reasons").info("Session override Key: " + overrideKey);

        //catch null pointer exceptions when creating commands
        try {
            String[] packages = { "com.srgood.dbot", "com.srgood.dbot.audio" };

            for (String pack : packages) {
                Reflections mReflect = new Reflections(pack);
                for (Class<? extends Command> cmdClass : mReflect.getSubTypesOf(Command.class)) {
                    if (!cmdClass.isInterface()) {
                        commands.put(cmdClass.getSimpleName().toLowerCase().replace("command", "").replace("audio", ""), cmdClass.newInstance());
                    }
                }
            }
        }  catch (Exception e) {
            SimpleLog.getLog("Reasons").warn("One or more of the commands failed to map");
            e.printStackTrace();
        }

    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/app.fxml"));

        Parent root = loader.load();

        controller = loader.getController();

        controller.updateConsole();

        primaryStage.setTitle("Reasons Console");
        primaryStage.setScene(new Scene(root));

        addToTray(primaryStage);
        firstTime = true;
        Platform.setImplicitExit(false);

        primaryStage.show();
    }

    public void stop () {
        jda.shutdown();
    }


    public static void main(String[] args) {
        launch(args);
    }


    public void addToTray(Stage stage) {
        if (SystemTray.isSupported()) {
            SystemTray systemTray = SystemTray.getSystemTray();
            //load image here
            Image image = null;
            try {
                image = ImageIO.read(getClass().getResource("/Nicholas.gif"));
            } catch (IOException e) {
                e.printStackTrace();
            }

            stage.setOnCloseRequest(event -> hide(stage));

            final ActionListener closeListener = e -> {
                System.exit(0);
            };

            final ActionListener showListener = e -> Platform.runLater(stage::show);

            PopupMenu popup = new PopupMenu();

            MenuItem showItem = new MenuItem("Show");
            showItem.addActionListener(showListener);
            popup.add(showItem);

            MenuItem closeItem = new MenuItem("Close");
            closeItem.addActionListener(closeListener);
            popup.add(closeItem);

            trayIcon = new TrayIcon(image,"Reasons",popup);

            trayIcon.addActionListener(showListener);

            try {
                systemTray.add(trayIcon);
                trayIcon.setImageAutoSize(true); // <- Sets the image size properly
            } catch (AWTException e) {
                e.printStackTrace();
            }
        }
    }

    public void showPrgmIsMinimizedMsg() {
        if (firstTime) {
            trayIcon.displayMessage("Alert","Reasons has been minimised to tray",TrayIcon.MessageType.INFO);
            firstTime = false;

        }
    }

    private void hide(final Stage stage) {
        Platform.runLater(() -> {
            if (SystemTray.isSupported()) {
                stage.hide();
                showPrgmIsMinimizedMsg();
            } else {
                System.exit(0);
            }
        });
    }


    public static String generateDirtyXML() throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        // Beautify XML
        // Set do indents
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        // Set indent amount
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        DOMSource source = new DOMSource(PInputFile);
        StringWriter stringWriter = new StringWriter();
        StreamResult result = new StreamResult(stringWriter);
        transformer.transform(source, result);
        return stringWriter.toString();
    }

    public static String generateCleanXML() throws TransformerException  {
        return cleanXMLString(generateDirtyXML());
    }

    //TODO fix the exceptions here
    public static void writeXML() throws TransformerException {
        String cleanXML = generateCleanXML();

        try (FileWriter fileWriter = new FileWriter(new File("servers.xml")); BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            bufferedWriter.write(cleanXML);
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private final static int[] illegalChars = { 34, 60, 62, 124, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 58, 42, 63, 92, 46, 47 };

    static {
        Arrays.sort(illegalChars);
    }

    public static String cleanFileName(String badFileName) {
        StringBuilder cleanName = new StringBuilder();
        for (int i = 0; i < badFileName.length(); i++) {
            int c = (int) badFileName.charAt(i);
            if (Arrays.binarySearch(illegalChars, c) < 0) {
                cleanName.append((char) c);
            }
        }
        return cleanName.toString();
    }

    public static void storeMessage(GuildMessageReceivedEvent event) {
        //creates a path based on a guild's readable name, which has illegal characters removed
        //example guild: name = "ExampleGuild" channel = "ExampleChannel"
        //our path for this example would be: "messages/guilds/ExampleGuild/ExampleChannel/all/
        String truePath = "messages/guilds/" + cleanFileName(event.getGuild().getName()) + "/" + cleanFileName(event.getChannel().getName()) + "/all/";
        try {

            FileOutputStream allFileStream = new FileOutputStream(truePath + event.getMessage().getId() + ".ser");
            ObjectOutputStream allObjectStream = new ObjectOutputStream(allFileStream);
            allObjectStream.writeObject(event.getMessage().getContent());

            allObjectStream.close();
            Boolean mentioned = false;
            //checks if jds is mentioned
            //TODO move this to a more suitable place
            if (!event.getMessage().getMentionedUsers().isEmpty()) {
                if (event.getJDA().getSelfInfo().getAsMention().equals(event.getMessage().getMentionedUsers().get(0).getAsMention())) {
                    mentioned = true;
                }
            }
            //checks if the message is written by a bot, or mentions this bot, and, if it is, puts it in the /bot/ directory also
            if (event.getAuthor().isBot() | event.getMessage().getContent().startsWith(com.srgood.dbot.utils.XMLUtils.getGuildPrefix(event.getGuild())) | mentioned) {
                FileOutputStream botFileStream = new FileOutputStream(truePath.replace("/all/", "/bot/") + event.getMessage().getId() + ".ser");
                ObjectOutputStream botObjectStream = new ObjectOutputStream(botFileStream);
                botObjectStream.writeObject(event.getMessage().getContent());

                botObjectStream.close();
            }
        } catch (FileNotFoundException e) {
            //creates the file chain, if it dosent exist
            //TODO can't we change this to a lambada thingy? you know wherever you do that ->
            File allFile = new File(truePath);
            File botFile = new File(truePath.replace("/all/", "/bot/"));

            allFile.setWritable(true);
            botFile.setWritable(true);
            allFile.mkdirs();
            botFile.mkdirs();

            storeMessage(event);

        } catch (IOException e) {
            e.printStackTrace();
        }


        //TODO Rank based delete
    }

    private static String cleanXMLString(String dirtyXML) {

//        try (FileReader fr = new FileReader("servers.xml"); FileWriter fw = new FileWriter("temp.xml")) {
//            BufferedReader br = new BufferedReader(fr);
//            String line;
//
//            while ((line = br.readLine()) != null) {
//                if (!line.trim().equals("")) // don't write out blank lines
//                {
//                    line = line.replace("\n", "").replace("\f", "").replace("\r", "");
//                    fw.write(line + "\n", 0, line.length() + 1);
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//            return;
//        }
//        try {
//            Files.deleteIfExists(new File("servers.xml").toPath());
//            Files.move(new File("temp.xml").toPath(), new File("servers.xml").toPath());
//            Files.deleteIfExists(new File("temp.xml").toPath());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        String[] lines = dirtyXML.split("\n");
        StringBuilder buffer = new StringBuilder();

        for (String line : lines) {
            if (!line.trim().equals("") /* don't write out blank lines */ ) {
                line = line.replace("\f", "").replace("\r", "").replaceAll("\\s+$", "");
                buffer.append(line).append("\n");
            }
        }

        return buffer.toString();
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
