package com.srgood.dbot.commands.audio;

import com.srgood.dbot.MusicPlayer;
import com.srgood.dbot.commands.Command;

import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.managers.AudioManager;

public class CommandRepeat implements Command {

	@Override
	public boolean called(String[] args, MessageReceivedEvent event) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void action(String[] args, MessageReceivedEvent event) {
		// TODO Auto-generated method stub
		
		float defvol = 0.35f;

		 event.getMessage().deleteMessage();
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
