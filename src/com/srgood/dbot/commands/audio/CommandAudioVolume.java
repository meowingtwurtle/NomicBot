package com.srgood.dbot.commands.audio;

import com.srgood.dbot.BotMain;
import com.srgood.dbot.MusicPlayer;

import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.managers.AudioManager;

public class CommandAudioVolume implements AudioCommand {
	
	private final String help = "Used to set the audio volume Use: '" + BotMain.prefix + "volume <0-1>'";
	
	@Override
	public boolean called(String[] args, GuildMessageReceivedEvent event) {
		return true;
		// TODO Auto-generated method stubn 
	}

	@Override
	public void action(String[] args, GuildMessageReceivedEvent event) {
		AudioManager manager = event.getGuild().getAudioManager();
		MusicPlayer player = AudioCommand.initAndGetPlayer(manager);

		try {
			float volume = Float.parseFloat(args[0]);
	        volume = Math.min(100F, Math.max(0F, volume));
	        player.setVolume(volume/100);
	        event.getChannel().sendMessage("Volume was changed to: " + volume);
		} catch (Exception e) {
			event.getChannel().sendMessage("Volume is: " + player.getVolume()*100);
		}
	}

	@Override
	public String help() {
		// TODO Auto-generated method stub
		return help;
	}

	@Override
	public void executed(boolean success, GuildMessageReceivedEvent event) {
		// TODO Auto-generated method stub
		return;
	}

}
