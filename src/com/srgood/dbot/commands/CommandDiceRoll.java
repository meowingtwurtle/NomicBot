package com.srgood.dbot.commands;

import java.util.Random;

import com.srgood.dbot.BotMain;
import com.srgood.dbot.utils.Permissions;

import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;

public class CommandDiceRoll implements Command {
	
	String help = "Rolls a dice (or die) and prints the results, and prints the result use:'" + BotMain.prefix + "roll <# die>'";
	
	@Override
	public boolean called(String[] args, GuildMessageReceivedEvent event) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void action(String[] args, GuildMessageReceivedEvent event) {
		// TODO Auto-generated method stub
		int numRolls = 0;
		StringBuilder stringBuilder = new StringBuilder();
		Random r = new Random();
		
		if (args.length > 0) {
			if (Integer.parseInt(args[0]) > 10) {
				event.getChannel().sendMessage("Woah there, Im not going to roll " + args[0] + " die, how about 10 instead?" );
				numRolls = 10;
			} else numRolls = Integer.parseInt(args[0]);
			
			for (int roll = 0; roll < numRolls; roll++) {
				int temp = r.nextInt(6) + 1;
				if (roll < numRolls - 1) {
					stringBuilder.append(temp + ", ");
				} else {
					stringBuilder.append(temp + ".");
				}
				
			}
			
			event.getChannel().sendMessage(stringBuilder.toString());
		} else {
			event.getChannel().sendMessage(r.nextInt(6) + 1 + ".");
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

	}

	@Override
	public Permissions permissionLevel(Guild guild) {
		// TODO Auto-generated method stub
		return Command.getPermissionXML(guild, this);
	}

	@Override
	public Permissions defaultPermissionLevel() {
		// TODO Auto-generated method stub
		return Permissions.STANDARD;
	}

}
