package com.srgood.dbot.commands;

import com.meowingtwurtle.math.api.IMathGroup;
import com.meowingtwurtle.math.api.IMathHandler;
import com.srgood.dbot.utils.Permissions;

import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;

public class CommandEval implements Command {

    public CommandEval() {
        
    }

    @Override
    public boolean called(String[] args, GuildMessageReceivedEvent event) {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) {
        try {
            IMathGroup group = IMathHandler.getMathHandler().parse(join(args));
            event.getChannel().sendMessage("`MATH:` " + group.eval().toString());
        } catch (Exception e) {
            e.printStackTrace();
            event.getChannel().sendMessage("`MATH:` An error occurred during parsing.");
        }
    }
    
    public String join(Object[] arr) {
        String ret = "";
        for (Object o : arr) {
            ret += o.toString();
        }
        return ret;
    }

    @Override
    public String help() {
        return "";
    }

    @Override
    public void executed(boolean success, GuildMessageReceivedEvent event) {
        return;
    }

    @Override
    public Permissions permissionLevel(Guild guild) {
        // TODO Auto-generated method stub
        return Command.getPermissionXML(guild, this);
    }

    @Override
    public Permissions defaultPermissionLevel() {
        return Permissions.STANDARD;
    }

}
