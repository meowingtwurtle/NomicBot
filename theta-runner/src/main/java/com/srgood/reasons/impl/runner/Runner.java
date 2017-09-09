package com.srgood.reasons.impl.runner;

import com.srgood.reasons.BotManager;
import com.srgood.reasons.impl.base.BotManagerImpl;
import com.srgood.reasons.impl.base.DiscordEventListener;
import com.srgood.reasons.impl.base.commands.CommandManagerImpl;
import com.srgood.reasons.impl.base.config.BotConfigManagerImpl;
import com.srgood.reasons.impl.base.config.ConfigFileManager;
import com.srgood.reasons.impl.commands.main.CommandRegistrar;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import org.apache.commons.lang3.StringUtils;

import javax.security.auth.login.LoginException;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.logging.*;

public class Runner {
    public static void main(String[] args) {
        String token = getToken(args);
        CompletableFuture<BotManager> botManagerFuture = new CompletableFuture<>();
        BotManager botManager = new BotManagerImpl(() -> createJDA(token, createLogger("ThetaInit"), botManagerFuture), () -> new BotConfigManagerImpl(new ConfigFileManager("theta.xml")), () -> new CommandManagerImpl(botManagerFuture), () -> createLogger("Theta"));
        botManagerFuture.complete(botManager);
        botManager.init();
        CommandRegistrar.registerCommands(botManager.getCommandManager());
    }

    private static String getToken(String[] args) {
        if (args.length < 1) {
            throw new IllegalArgumentException("No token was provided");
        } else if (args.length > 1) {
            throw new IllegalArgumentException("Only one argument should be provided");
        }
        return args[0];
    }

    private static JDA createJDA(String token, Logger logger, Future<BotManager> botManagerFuture) {
        try {
            return new JDABuilder(AccountType.BOT).addEventListener(new DiscordEventListener(botManagerFuture, Collections.emptyList())) // TODO Add messageChecks for eventlistener
                                                      .setToken(token)
                                                      .setGame(Game.of("Type @Theta help"))
                                                      .setAutoReconnect(true)
                                                      .buildBlocking();
        } catch (LoginException | IllegalArgumentException e) {
            logger.severe("**COULD NOT LOG IN** An invalid token was provided.");
            throw new RuntimeException(e);
        } catch (RateLimitedException e) {
            logger.severe("**We are being ratelimited**");
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            logger.severe("InterruptedException");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private static Logger createLogger(String name) {
            Logger logger = Logger.getLogger(name);
            Formatter loggerFormatter = new Formatter() {
                @Override
                public String format(LogRecord record) {
                    ZonedDateTime dateTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(record.getMillis()), ZoneOffset
                            .systemDefault());
                    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                    return String.format("[%s] [%s] [%s]: %s %n", dateFormatter.format(dateTime), StringUtils.capitalize(record
                            .getLevel()
                            .toString()
                            .toLowerCase()), record.getLoggerName(), record.getMessage());
                }
            };

            StreamHandler streamHandler = new StreamHandler(System.out, loggerFormatter) {
                @Override
                public synchronized void publish(LogRecord record) {
                    super.publish(record);
                    super.flush();
                }
            };
            streamHandler.flush();

            logger.setUseParentHandlers(false);
            logger.addHandler(streamHandler);
            logger.setLevel(Level.ALL);
            return logger;
    }
}
