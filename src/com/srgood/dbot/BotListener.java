package com.srgood.dbot;

import com.srgood.dbot.utils.XMLHandler;
import net.dv8tion.jda.events.ReadyEvent;
import net.dv8tion.jda.events.guild.GuildJoinEvent;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.hooks.ListenerAdapter;
import net.dv8tion.jda.utils.SimpleLog;

/**
 * <h1>Bot Listener</h1>
 * <p>
 * Bot Listener deals with MessageReceived events, excluding its own.
 *
 * @author srgood
 * @version 0.8
 * @since 7/12/16
 */

public class BotListener extends ListenerAdapter {

    @Override
    public void onReady(ReadyEvent event) {

    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {

        String localPrefix = XMLHandler.getGuildPrefix(event.getGuild());

        if (event.getMessage().getContent().equals(com.srgood.dbot.Reference.Strings.TABLE_FLIP)) {
            event.getChannel().sendMessage(com.srgood.dbot.Reference.Strings.TABLE_UNFLIP_JOKE);
        }

        BotMain.storeMessage(event);

        if (event.getMessage().getContent().startsWith(localPrefix) && !java.util.Objects.equals(event.getMessage().getAuthor().getId(), event.getJDA().getSelfInfo().getId())) {
            BotMain.handleCommand(BotMain.parser.parse(event.getMessage().getContent().toLowerCase(), event, localPrefix));
            SimpleLog.getLog("Reasons").info("Got prefixed input: " + event.getMessage().getContent());
        } else {
            try {

                if (event.getJDA().getSelfInfo().getAsMention().equals(event.getMessage().getMentionedUsers().get(0).getAsMention())) {

                    SimpleLog.getLog("Reasons").info("Got prefixed input (mention): " + event.getMessage().getContent());
                    BotMain.handleCommand(BotMain.parser.parse(event.getMessage().getContent().toLowerCase(), event, localPrefix));
                }
            } catch (Exception ignored) {

            }
        }

    }


    @Override
    public void onGuildJoin(GuildJoinEvent event) {
//          initGuild(event.getGuild());
    }

}