package com.srgood.dbot.commands.audio;

import com.srgood.dbot.MusicPlayer;
import com.srgood.dbot.commands.Command;

import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.managers.AudioManager;

public class CommandAudioRepeat implements AudioCommand {

	@Override
	public boolean called(String[] args, MessageReceivedEvent event) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void action(String[] args, MessageReceivedEvent event) {
		AudioManager manager = event.getGuild().getAudioManager();
		MusicPlayer player = AudioCommand.initAndGetPlayer(manager);
		
		if (player.isRepeat())
        {
            player.setRepeat(false);
            event.getChannel().sendMessage("The player has been set to **not** repeat.");
        }
        else
        {
            player.setRepeat(true);
            event.getChannel().sendMessage("The player been set to repeat.");
        }
	}

	@Override
	public String help() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void executed(boolean success, MessageReceivedEvent event) {
		// TODO Auto-generated method stub
		return;
	}

}
