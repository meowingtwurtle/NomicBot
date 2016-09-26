package com.srgood.dbot.commands.audio;

import com.srgood.dbot.BotMain;
import com.srgood.dbot.audio.MusicPlayer;
import com.srgood.dbot.utils.config.ConfigUtils;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.managers.AudioManager;

public class CommandAudioVolume implements AudioCommand {

    private static final String HELP = "Used to get or set the audio volume on this server. No argument will get, one argument will set. Use: '" + BotMain.prefix + "volume [0-100]'";

    @Override
    public boolean called(String[] args, GuildMessageReceivedEvent event) {
        return true;
    }

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) {
        AudioManager manager = event.getGuild().getAudioManager();
        MusicPlayer player = AudioCommand.initAndGetPlayer(manager);

        try {
            float volume = Float.parseFloat(args[0]);
            volume = Math.min(100F, Math.max(0F, volume));
            player.setVolume(volume / 100);
            event.getChannel().sendMessage("Volume was changed to: " + volume);
        } catch (Exception e) {
            event.getChannel().sendMessage("Volume is: " + player.getVolume() * 100);
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
    public com.srgood.dbot.PermissionLevels permissionLevel(Guild guild) {
        // TODO Auto-generated method stub
        return ConfigUtils.getCommandPermission(guild, this);
    }

    @Override
    public com.srgood.dbot.PermissionLevels defaultPermissionLevel() {
        // TODO Auto-generated method stub
        return com.srgood.dbot.PermissionLevels.STANDARD;
    }

    @Override
    public String[] names() {
        return new String[] {"volume"};
    }

}
