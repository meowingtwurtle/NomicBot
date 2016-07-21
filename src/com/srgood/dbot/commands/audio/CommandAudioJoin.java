package com.srgood.dbot.commands.audio;

import com.srgood.dbot.BotMain;
import com.srgood.dbot.MusicPlayer;

import net.dv8tion.jda.entities.VoiceChannel;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.managers.AudioManager;
import net.dv8tion.jda.utils.SimpleLog;

public class CommandAudioJoin implements AudioCommand {

	
	private final String help = "Adds Reasons to a voice channel Use: " + BotMain.prefix + "join [channel name]";
			
	@Override
	public boolean called(String[] args, MessageReceivedEvent event) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void action(String[] args, MessageReceivedEvent event) {		
		AudioManager manager = event.getGuild().getAudioManager();
		MusicPlayer player = AudioCommand.initAndGetPlayer(manager);
        
		VoiceChannel channel = null;
        
        StringBuilder chanName = new StringBuilder();
        
        for (int i = 0; i < args.length; i++) {
        	
        	chanName.append(args[i]);
        	
        	if (i < args.length -1) {
        		chanName.append(" ");
        	}
        	
        }
        
        if (chanName.toString().equals("")) {
        	for (VoiceChannel i : event.getGuild().getVoiceChannels()) {
        		if (i.getUsers().contains(event.getAuthor())) {
        			channel = i;
        		}
        	}
        } else {
        	channel = event.getGuild().getVoiceChannels().stream().filter(vChan -> vChan.getName().equalsIgnoreCase(chanName.toString())).findFirst().orElse(null);
        }
        
        
        
        if (channel == null)
        {
            event.getChannel().sendMessage("I couldn't find a channel called: '" + chanName.toString() + "'");
            SimpleLog.getLog("Reasons").debug("Joined: " + chanName.toString());
            return;
        }
        manager.openAudioConnection(channel);
        
		
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
