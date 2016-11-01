package com.srgood.reasons.commands.audio;

import com.srgood.reasons.BotMain;
import com.srgood.reasons.audio.MusicPlayer;
import com.srgood.reasons.utils.config.ConfigUtils;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.managers.AudioManager;

public class CommandAudioRepeat implements AudioCommand {
    private static final String HELP = "Used to repeat the audio that is playing on this server. Use: '" + BotMain.prefix + "pause'";

    @Override
    public boolean called(String[] args, GuildMessageReceivedEvent event) {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) {

        AudioManager manager = event.getGuild().getAudioManager();
        MusicPlayer player = AudioCommand.initAndGetPlayer(manager);

        if (player.isRepeat()) {
            player.setRepeat(false);
            event.getChannel().sendMessage("The player has been set to **not** repeat.");
        } else {
            player.setRepeat(true);
            event.getChannel().sendMessage("The player been set to repeat.");
        }
    }

    @Override
    public String help() {
        // TODO Auto-generated method stub
        return HELP;
    }

    @Override
    public void executed(boolean success, GuildMessageReceivedEvent event) {
        // TODO Auto-generated method stub
    }

    @Override
    public com.srgood.reasons.PermissionLevels permissionLevel(Guild guild) {
        // TODO Auto-generated method stub
        return ConfigUtils.getCommandPermission(guild, this);
    }

    @Override
    public com.srgood.reasons.PermissionLevels defaultPermissionLevel() {
        // TODO Auto-generated method stub
        return com.srgood.reasons.PermissionLevels.STANDARD;
    }



}
