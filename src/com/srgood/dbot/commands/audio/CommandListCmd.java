package com.srgood.dbot.commands.audio;

import java.util.List;

import com.srgood.dbot.Main;
import com.srgood.dbot.MusicPlayer;
import com.srgood.dbot.commands.Command;
import com.srgood.dbot.source.AudioInfo;
import com.srgood.dbot.source.AudioSource;
import com.srgood.dbot.source.AudioTimestamp;

import net.dv8tion.jda.MessageBuilder;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.managers.AudioManager;

public class CommandListCmd implements Command {

	private final String help = "Lists the current Audio queue Use: " + Main.prefix + "list";
			
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
        
		List<AudioSource> queue = player.getAudioQueue();
        if (queue.isEmpty())
        {
            event.getChannel().sendMessage("The queue is currently empty!");
            return;
        }


        MessageBuilder builder = new MessageBuilder();
        builder.appendString("__Current Queue.  Entries: " + queue.size() + "__\n");
        for (int i = 0; i < queue.size() && i < 10; i++)
        {
            AudioInfo info = queue.get(i).getInfo();
//            builder.appendString("**(" + (i + 1) + ")** ");
            if (info == null)
                builder.appendString("*Could not get info for this song.*");
            else
            {
                AudioTimestamp duration = info.getDuration();
                builder.appendString("`[");
                if (duration == null)
                    builder.appendString("N/A");
                else
                    builder.appendString(duration.getTimestamp());
                builder.appendString("]` " + info.getTitle() + "\n");
            }
        }

        boolean error = false;
        int totalSeconds = 0;
        for (AudioSource source : queue)
        {
            AudioInfo info = source.getInfo();
            if (info == null || info.getDuration() == null)
            {
                error = true;
                continue;
            }
            totalSeconds += info.getDuration().getTotalSeconds();
        }

        builder.appendString("\nTotal Queue Time Length: " + AudioTimestamp.fromSeconds(totalSeconds).getTimestamp());
        if (error)
            builder.appendString("`An error occured calculating total time. Might not be completely valid.");
        event.getChannel().sendMessage(builder.build());
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
