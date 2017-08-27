package com.srgood.reasons.impl.base;

import com.srgood.reasons.BotManager;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.function.BiPredicate;

/**
 * <h1>DiscordEventListener</h1>
 * <p>
 * DiscordEventListener deals with MessageReceived events, excluding its own.
 *
 * @author srgood
 * @version 0.8
 * @since 7/12/16
 */
public class DiscordEventListener extends ListenerAdapter {

    private BotManager botManager;
    private final Collection<BiPredicate<Message, BotManager>> messageFilters;
    private Future<BotManager> botManagerFuture;

    public DiscordEventListener(Future<BotManager> botManagerFuture, Collection<BiPredicate<Message, BotManager>> messageFilters) {
        this.botManagerFuture = botManagerFuture;
        this.messageFilters = messageFilters;
    }

    @Override
    public void onReady(ReadyEvent event) {
        try {
            botManager = botManagerFuture.get();
            botManagerFuture = null;
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {
        handleMessage(event.getMessage());
    }

    private void handleMessage(Message message) {
        if (message.getAuthor().isBot()) {
            return;
        }

        if (Objects.equals(message.getContent(), BaseConstants.TABLE_FLIP)) {
            message.getChannel().sendMessage(BaseConstants.TABLE_UNFLIP_JOKE).queue();
            return;
        }

        botManager.getLogger().info("Got command message: " + message.getContent());
        botManager.getCommandManager().handleCommandMessage(message);
    }
}