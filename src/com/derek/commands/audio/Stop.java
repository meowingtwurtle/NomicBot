package com.derek.commands.audio;

import com.derek.Command;
import com.derek.Main;
import com.derek.MusicPlayer;

import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.managers.AudioManager;

public class Stop implements Command {

	private final String help = "Used to stop the current audio Use: " + Main.prefix + "stop";
	
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
		
        player.stop();
        event.getChannel().sendMessage("Playback has been completely stopped. Forever. and ever. its never coming back");
        
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
