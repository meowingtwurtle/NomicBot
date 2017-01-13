package com.srgood.reasons.commands;

import com.srgood.reasons.ReasonsMain;
import com.srgood.reasons.config.ConfigUtils;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class CommandCensor implements Command {

    private static final String HELP = "Modifies the list of censored words.  Use: '" + ReasonsMain.prefix + "censor <add/remove/list> [word]'";

    @Override
    public boolean called(String[] args, GuildMessageReceivedEvent event) { return true; }

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) {
        List<String> censoredWords = new ArrayList<>();
        if(ConfigUtils.getGuildProperty(event.getGuild(), "censorlist") == null) {
            try {
                List<String> strings = new ArrayList<>();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(baos);
                oos.writeObject(strings);
                String serialized = baos.toString("UTF-8");
                ConfigUtils.setGuildProperty(event.getGuild(), "censorlist", serialized);
            } catch(Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                String serialized = ConfigUtils.getGuildProperty(event.getGuild(), "censorlist");
                ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(serialized.getBytes()));
                censoredWords = (ArrayList<String>)ois.readObject();
            } catch(Exception e) {
                e.printStackTrace();
            }
        }

        if(args.length == 1) {
            if(args[0].equals("list")) {
                String out = "__**Censored Words**__ ```\n";
                for(int i = 0; i < censoredWords.size(); i++) {
                    out += "-";
                    out += censoredWords.get(i);
                    out += "\n";
                }
                out += "```";
                if(censoredWords.size() == 0) out = "There are no censored words on this server.";
                event.getAuthor().getPrivateChannel().sendMessage(out).queue();

            }
        } else if(args.length == 2) {
            if(args[0].equals("add")) {
                censoredWords.add(args[1].toLowerCase());
                save(censoredWords, event, args[1], true);
            }
            if(args[0].equals("remove")) {
                if(censoredWords.remove(args[1].toLowerCase())) {
                    save(censoredWords, event, args[1], false);
                } else {
                    event.getAuthor().getPrivateChannel().sendMessage("\"" + args[1] + "\" not found in list of censored words.").queue();
                }
            }
        }
    }

    private void save(List<String> strings, GuildMessageReceivedEvent event, String arg, boolean added) {
        String addsub;
        if(added) { addsub = "added to"; } else { addsub = "removed from"; }
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(strings);
            String serialized = baos.toString("UTF-8");
            ConfigUtils.setGuildProperty(event.getGuild(), "censorlist", serialized);
            event.getAuthor().getPrivateChannel().sendMessage("\"" + arg + "\" " + addsub + " list of censored words.").queue();
        } catch(Exception e) {
            e.printStackTrace();
            event.getAuthor().getPrivateChannel().sendMessage("\"" + arg + "\" could not be " + addsub + " list of censored words.").queue();
        }
    }

    @Override
    public String help() {
        return HELP;
    }

    @Override
    public void executed(boolean success, GuildMessageReceivedEvent event) {}

    @Override
    public PermissionLevels permissionLevel(Guild guild) {
        return ConfigUtils.getCommandPermission(guild, this);
    }

    @Override
    public PermissionLevels defaultPermissionLevel() {
        return PermissionLevels.ADMINISTRATOR;
    }

    @Override
    public String[] names() {
        return new String[] {"censor"};
    }

}
