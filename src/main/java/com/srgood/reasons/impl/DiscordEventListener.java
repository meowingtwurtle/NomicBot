package com.srgood.reasons.impl;

import com.srgood.reasons.BotManager;
import com.srgood.reasons.config.GuildConfigManager;
import com.srgood.reasons.impl.commands.CommandUtils;
import com.srgood.reasons.impl.utils.CensorUtils;
import com.srgood.reasons.impl.utils.GreetingUtils;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.guild.GenericGuildEvent;
import net.dv8tion.jda.core.events.guild.GuildJoinEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.events.message.guild.GuildMessageUpdateEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.Objects;

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

    private final BotManager botManager;

    public DiscordEventListener(BotManager botManager) {
        this.botManager = botManager;
    }

    @Override
    public void onReady(ReadyEvent event) {

    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {

        if (Objects.equals(event.getAuthor(), event.getJDA().getSelfUser())) return;

        if (Objects.equals(event.getMessage().getContent(), Reference.TABLE_FLIP)) {
            event.getChannel().sendMessage(Reference.TABLE_UNFLIP_JOKE).queue();
        }

        GuildConfigManager guildConfigManager = getGuildConfigManager(event.getGuild());
        if (CommandUtils.isCommandMessage(event.getMessage(), guildConfigManager
                                                                        .getPrefix())) {
            botManager.getCommandManager().handleCommandMessage(event.getMessage());
            botManager.getLogger().info("Got command message: " + event.getMessage().getContent());
        }

        CensorUtils.checkCensor(CensorUtils.getGuildCensorList(guildConfigManager), event.getMessage());
    }

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        GreetingUtils.tryGreeting(event.getMember(), getGuildConfigManager(event));
    }
    @Override
    public void onGuildMemberLeave(GuildMemberLeaveEvent event) {
        GreetingUtils.tryGoodbye(event.getMember(), getGuildConfigManager(event));
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

    private GuildConfigManager getGuildConfigManager(GenericGuildEvent guildEvent) {
        return getGuildConfigManager(guildEvent.getGuild());
    }

    private GuildConfigManager getGuildConfigManager(Guild guild) {
        return botManager.getConfigManager().getGuildConfigManager(guild);
    }
}