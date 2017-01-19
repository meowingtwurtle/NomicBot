package com.srgood.reasons.commands;

import com.srgood.reasons.ReasonsMain;
import com.srgood.reasons.utils.MemberUtils;
import com.srgood.reasons.utils.RoleUtils;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;
import java.util.Random;

/**
 * Created by dmanl on 9/24/2016.
 */
public class CommandNotifyRand implements Command {

    private static final String HELP = "Notifies a random member of a Role. Use: '" + ReasonsMain.prefix + "notifyrand <role ID>'";

    @Override
    public boolean called(String[] args, GuildMessageReceivedEvent event) {
        return args.length < 0;
    }

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) {

    }

    @Override
    public String help() {
        return HELP;
    }


}
