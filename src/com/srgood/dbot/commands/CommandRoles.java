package com.srgood.dbot.commands;

import com.srgood.dbot.BotMain;
import com.srgood.dbot.PermissionLevels;
import com.srgood.dbot.utils.ConfigUtils;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.Role;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;
import java.util.stream.Collectors;

public class CommandRoles implements Command {

    private final String help = "Lists all roles and their ID's in the guild. Use: " + BotMain.prefix + "roles";

    @Override
    public boolean called(String[] args, GuildMessageReceivedEvent event) {
        return true;
    }

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) {
        Guild guild = event.getGuild();
        List<Role> roles = guild.getRoles();
        System.out.println(roles.stream().map(Role::getName).collect(Collectors.toList()));
        List<String> roleStrings = roles.stream().filter(role -> role.compareTo(event.getGuild().getPublicRole()) != 0).map(role -> role.getName() + ": " + role.getId() + "\n").collect(Collectors.toList());

        StringBuilder stringBuilder = new StringBuilder();
        roleStrings.forEach(stringBuilder::append);
        event.getChannel().sendMessage(stringBuilder.toString());
    }

    @Override
    public String help() {
        return help;
    }

    @Override
    public void executed(boolean success, GuildMessageReceivedEvent event) {

    }

    @Override
    public com.srgood.dbot.PermissionLevels defaultPermissionLevel() {
        return PermissionLevels.STANDARD;
    }

    @Override
    public com.srgood.dbot.PermissionLevels permissionLevel(Guild guild) {
        return ConfigUtils.getCommandPermissionXML(guild, this);
    }

    @Override
    public String[] names() {
        return new String[] {"roles", "listroles", "list-roles", "getroles", "get-roles"};
    }
}
