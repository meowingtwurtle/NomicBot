package com.srgood.dbot.commands.audio;

import com.srgood.dbot.Main;
import com.srgood.dbot.MusicPlayer;
import com.srgood.dbot.commands.Command;
import com.srgood.dbot.source.AudioInfo;
import com.srgood.dbot.source.AudioTimestamp;

import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.managers.AudioManager;

public class CommandNowPlaying implements Command {

	public final String help = "Displays information about the song that is playing Use: " + Main.prefix + "now-playing";
	@Override
	public boolean called(String[] args, MessageReceivedEvent event) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void action(String[] args, MessageReceivedEvent event) {
		// TODO Auto-generated method stub
		
float defvol = 0.35f;
		
		AudioManager manager = event.getGuild().getAudioManager();
        MusicPlayer player;
        if (manager.getSendingHandler() == null)
        {
            player = new MusicPlayer();
            player.setVolume(defvol);
            manager.setSendingHandler(player);
        }
        else
        {
            player = (MusicPlayer) manager.getSendingHandler();
        }

		
		if (player.isPlaying())
        {
            AudioTimestamp currentTime = player.getCurrentTimestamp();
            AudioInfo info = player.getCurrentAudioSource().getInfo();
            if (info.getError() == null)
            {
                event.getChannel().sendMessage(
                        "**Playing:** " + info.getTitle() + "\n" +
                        "**Time:**    [" + currentTime.getTimestamp() + " / " + info.getDuration().getTimestamp() + "]");
            }
            else
            {
                event.getChannel().sendMessage(
                        "**Playing:** Info Error. Known source: " + player.getCurrentAudioSource().getSource() + "\n" +
                        "**Time:**    [" + currentTime.getTimestamp() + " / (N/A)]");
            }
        }
        else
        {
            event.getChannel().sendMessage("The player is not currently playing anything!");
        }
	}

	@Override
	public String help() {
		// TODO Auto-generated method stub
		return help;
	}

	@Override
	public void executed(boolean success, MessageReceivedEvent event) {
		// TODO Auto-generated method stub
		return;
	}

}
