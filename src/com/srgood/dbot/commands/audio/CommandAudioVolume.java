package com.srgood.dbot.commands.audio;

import com.srgood.dbot.Main;
import com.srgood.dbot.MusicPlayer;
import com.srgood.dbot.commands.Command;

import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.managers.AudioManager;

public class CommandAudioVolume implements AudioCommand {
	
	private final String help = "Used to set the audio volume Use: " + Main.prefix + "volume [0-1]";
	
	@Override
	public boolean called(String[] args, MessageReceivedEvent event) {
		return true;
		// TODO Auto-generated method stubn 
	}

	@Override
	public void action(String[] args, MessageReceivedEvent event) {
		AudioManager manager = event.getGuild().getAudioManager();
		MusicPlayer player = AudioCommand.initAndGetPlayer(manager);

		try {
			float volume = Float.parseFloat(args[0]);
	        volume = Math.min(20F, Math.max(0F, volume));
	        player.setVolume(volume);
	        event.getChannel().sendMessage("Volume was changed to: " + volume);
		} catch (Exception e) {
			event.getChannel().sendMessage("Volume is: " + player.getVolume());
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
