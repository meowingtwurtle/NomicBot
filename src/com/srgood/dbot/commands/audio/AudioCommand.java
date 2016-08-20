package com.srgood.dbot.commands.audio;

import com.srgood.dbot.MusicPlayer;
import com.srgood.dbot.commands.Command;

import net.dv8tion.jda.managers.AudioManager;

interface AudioCommand extends Command {
	float DEFAULT_VOLUME = 0.35f;
	
	static MusicPlayer initAndGetPlayer(AudioManager manager) {
        MusicPlayer player;
        if (manager.getSendingHandler() == null)
        {
            player = new MusicPlayer();
            player.setVolume(DEFAULT_VOLUME);
            manager.setSendingHandler(player);
        }
        else
        {
            player = (MusicPlayer) manager.getSendingHandler();
        }
        return player;
	}
}
