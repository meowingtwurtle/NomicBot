package com.srgood.reasons.impl;

import com.srgood.reasons.BotManager;
import com.srgood.reasons.commands.CommandManager;
import com.srgood.reasons.config.BotConfigManager;
import com.srgood.reasons.impl.commands.CommandManagerImpl;
import com.srgood.reasons.impl.commands.impl.actual.CommandRegistrar;
import com.srgood.reasons.impl.config.BotConfigManagerImpl;
import com.srgood.reasons.impl.config.ConfigFileManager;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.exceptions.RateLimitedException;

import javax.imageio.ImageIO;
import javax.security.auth.login.LoginException;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.*;

public class ReasonsMain implements BotManager {
    public final Instant START_INSTANT = Instant.now();

    private JDA jda;
    private String prefix;

    private final CommandManager commandManager = new CommandManagerImpl(this);
    private final ConfigFileManager configFileManager = new ConfigFileManager("theta.xml");
    private final BotConfigManager configManager = new BotConfigManagerImpl(configFileManager);
    private final Logger logger = Logger.getLogger("Theta");

    {
        Formatter loggerFormatter = new Formatter() {
            @Override
            public String format(LogRecord record) {
                //LocalDateTime instant = LocalDateTime.ofEpochSecond(record.getMillis() / 1000, 0, TimeZone.getDefault());
                ZonedDateTime dateTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(record.getMillis()), ZoneOffset.systemDefault());
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                return String.format("[%s] [%s] [%s]: %s %n", dateFormatter.format(dateTime), record.getLevel(), record.getLoggerName(), record.getMessage());
            }
        };

        logger.setUseParentHandlers(false);

        StreamHandler streamHandler = new StreamHandler(System.out, loggerFormatter) {
            @Override
            public synchronized void publish(LogRecord record) {
                super.publish(record);
                super.flush();
            }
        };
        streamHandler.flush();

        logger.addHandler(streamHandler);

        logger.setLevel(Level.FINEST);
    }

    public JDA getJda() {
        return jda;
    }

    public static void main(String[] args) {
        String token = getToken(args);
        new ReasonsMain().init(token);
    }

    private static String getToken(String[] args) {
        if (args.length < 1) {
            throw new IllegalArgumentException("No token was provided");
        } else if (args.length > 1) {
            throw new IllegalArgumentException("Only one argument should be provided");
        }
        return args[0];
    }

    @Override
    public void init(String token) {
        initJDA(token);
        initCommands();

        addToTray();
    }

    private void initJDA(String token) {
        try {
            jda = new JDABuilder(AccountType.BOT).addEventListener(new DiscordEventListener(this))
                                                 .setToken(token)
                                                 .setGame(Game.of("Type @Theta help"))
                                                 .setAutoReconnect(true)
                                                 .buildBlocking();
        } catch (LoginException | IllegalArgumentException e) {
            getLogger().severe("**COULD NOT LOG IN** An invalid token was provided.");
            throw new RuntimeException(e);
        } catch (RateLimitedException e) {
            getLogger().severe("**We are being ratelimited**");
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            getLogger().severe("InterruptedException");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private void initCommands() {
        CommandRegistrar.registerCommands(getCommandManager());
    }

    private void addToTray() {
        if (SystemTray.isSupported()) {
            SystemTray systemTray = SystemTray.getSystemTray();
            //load image here
            Image image = null;
            try {
                image = ImageIO.read(getClass().getResource("/Nicholas.gif"));
            } catch (IOException e) {
                e.printStackTrace();
            }

            final ActionListener closeListener = e -> System.exit(0);

            PopupMenu popup = new PopupMenu();

            MenuItem closeItem = new MenuItem("Close");
            closeItem.addActionListener(closeListener);
            popup.add(closeItem);

            TrayIcon trayIcon = new TrayIcon(image, "Reasons", popup);

            try {
                systemTray.add(trayIcon);
                trayIcon.setImageAutoSize(true); // <- Sets the image size properly
            } catch (AWTException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void shutdown() {
        configFileManager.save();
        jda.shutdown();
    }

    @Override
    public void shutdown(boolean force) {
        // TODO force shutdown
        shutdown();
    }

    @Override
    public BotConfigManager getConfigManager() {
        return configManager;
    }

    @Override
    public JDA getJDA() {
        return jda;
    }

    @Override
    public String getDefaultPrefix() {
        return prefix;
    }

    @Override
    public void setDefaultPrefix(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public CommandManager getCommandManager() {
        return commandManager;
    }

    @Override
    public Logger getLogger() {
        return logger;
    }

    @Override
    public Instant getStartTime() {
        return START_INSTANT;
    }
}
