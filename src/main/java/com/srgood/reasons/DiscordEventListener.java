package com.srgood.reasons;

import com.srgood.reasons.commands.upcoming.CommandManager;
import com.srgood.reasons.utils.CensorUtils;
import com.srgood.reasons.commands.upcoming.CommandUtils;
import com.srgood.reasons.utils.GreetingUtils;
import com.srgood.reasons.utils.GuildUtils;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.guild.GuildJoinEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.events.message.guild.GuildMessageUpdateEvent;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.utils.SimpleLog;

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

    @Override
    public void onReady(ReadyEvent event) {

    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {

        try {
            GuildUtils.doPreMessageGuildCheck(event.getGuild());
        } catch (RateLimitedException e) {
            e.printStackTrace();
        }

        if (event.getMessage().getContent().equals(com.srgood.reasons.Reference.Strings.TABLE_FLIP)) {
            event.getChannel().sendMessage(com.srgood.reasons.Reference.Strings.TABLE_UNFLIP_JOKE).queue();
        }

        if (CommandUtils.isCommandMessage(event.getMessage())) {
            CommandManager.handleCommand(event.getMessage());
            SimpleLog.getLog("Reasons").info("Got command message: " + event.getMessage().getContent());
        }

        CensorUtils.checkCensor(event.getMessage());
    }

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        GreetingUtils.tryGreeting(event.getMember());
    }
    @Override
    public void onGuildMemberLeave(GuildMemberLeaveEvent event) {
        GreetingUtils.tryGoodbye(event.getMember());
    }


    @Override
    public void onGuildJoin(GuildJoinEvent event) {
//          initGuild(event.getGuild());
    }

    @Override
    public void onGuildMessageUpdate(GuildMessageUpdateEvent event) {
        GuildMessageReceivedEvent e = new GuildMessageReceivedEvent(event.getJDA(),event.getResponseNumber(),event.getMessage());
        this.onGuildMessageReceived(e);
    }

    @Override
    public void onGuildVoiceLeave(GuildVoiceLeaveEvent event) {
        if (event.getChannelLeft().getMembers().size() == 1) {
            // TODO FIX AUDIO
//            AudioManager manager = event.getGuild();
//
//            manager.closeAudioConnection();
        }
    }

}