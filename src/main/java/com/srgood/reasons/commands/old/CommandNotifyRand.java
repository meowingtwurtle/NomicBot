package com.srgood.reasons.commands.old;

import com.srgood.reasons.ReasonsMain;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import static com.srgood.reasons.utils.RoleUtils.*;
import static com.srgood.reasons.utils.MemberUtils.*;

/**
 * Created by dmanl on 9/24/2016.
 */
public class CommandNotifyRand implements Command {

    private static final String HELP = "Notifies a random member of a Role. Use: '" + ReasonsMain.prefix + "notifyrand <role ID>'";

    /*
    IMPORTANT NOTE: this class is kindof functional, but, its planned for the new args system with quotes.
    if executed #!notifyRand Bot Developer 2, Developer would be parsed as an integer, and nobody wants that.
    also the feedback needs to be fixed.

     */

    @Override
    public boolean called(String[] args, GuildMessageReceivedEvent event) {
        return args.length < 0;
    }

    @Override

    public void action(String[] args, GuildMessageReceivedEvent event) {
        Random random = new Random();

        int amount = 1;

        if (args.length > 1) {
            amount = Integer.parseInt(args[1]);
        }


        final List<Role> foundRoles = getRolesByName(event.getGuild(),args[0]);

        if (foundRoles.size() < 1) {
            event.getChannel().sendMessage("Found no roles called *" + args[0] + "*").queue();
            return;
        }

        final Role role = foundRoles.get(0);
        List<Member> foundMembers = getMembersWithRole(getOnlineMembers(event.getGuild()),foundRoles.get(0));

        if (foundMembers.size() < 1) {
            event.getChannel().sendMessage("Found no users with role *" + role.getName() + "*").queue();
            return;
        }

        List<User> users = new LinkedList<>();


        for (int i = 0;i < amount;i++) {
            Member m = foundMembers.get(random.nextInt(foundMembers.size() + 1));
            assert m != null;
            foundMembers.remove(m);
            users.add(m.getUser());
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < users.size(); i++) {
            User u = users.get(i);
            u.getPrivateChannel().sendMessage(u.getName() + ", your presence was requested by *" + event.getAuthor() + "* in *" + event.getGuild().getName() + " -> #" + event.getChannel().getName() + "*").queue();

            if (i < users.size() - 3) {
                sb.append(u.getName()).append(", ");
            } else if (i == users.size() - 2) {
                sb.append(u.getName()).append(", and ");
            } else if(i == users.size() - 1) {
                sb.append(u.getName());
            }
        }

        event.getChannel().sendMessage("Notified " + sb.toString());


    }

    @Override
    public String help() {
        return HELP;
    }


}
