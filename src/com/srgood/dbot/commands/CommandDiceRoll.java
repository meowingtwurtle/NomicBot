package com.srgood.dbot.commands;

import com.srgood.dbot.BotMain;
import com.srgood.dbot.utils.Permissions;
import com.srgood.dbot.utils.XMLHandler;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;

import java.util.Random;

public class CommandDiceRoll implements Command {

    private final String help = "Rolls a dice (or die) and prints the results, and prints the result use:'" + BotMain.prefix + "roll <# die>'";

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
                event.getChannel().sendMessage("Whoa there, Im not going to roll " + args[0] + " dice, how about 10 instead?");
                numRolls = 10;
            } else numRolls = Integer.parseInt(args[0]);

            for (int roll = 0; roll < numRolls; roll++) {
                int randNum = r.nextInt(6) + 1;
                if (roll < numRolls - 1) {
                    stringBuilder.append(randNum).append(", ");
                } else {
                    stringBuilder.append(randNum).append(".");
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
        return XMLHandler.getCommandPermissionXML(guild, this);
    }

    @Override
    public Permissions defaultPermissionLevel() {
        // TODO Auto-generated method stub
        return Permissions.STANDARD;
    }

}
