package com.srgood.dbot.commands.audio;

import com.srgood.dbot.BotMain;
import com.srgood.dbot.MusicPlayer;
import com.srgood.dbot.commands.Command;

import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.managers.AudioManager;

public class CommandAudioStop implements AudioCommand {

	private final String help = "Used to stop the current audio Use: " + BotMain.prefix + "stop";
	
	@Override
	public boolean called(String[] args, MessageReceivedEvent event) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void action(String[] args, MessageReceivedEvent event) {
		AudioManager manager = event.getGuild().getAudioManager();
		MusicPlayer player = AudioCommand.initAndGetPlayer(manager);
		
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
