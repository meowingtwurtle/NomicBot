package com.srgood.dbot.commands;

import com.srgood.dbot.BotMain;
import com.srgood.dbot.utils.GuildUtils;
import com.srgood.dbot.utils.ImageUtils;
import com.srgood.dbot.utils.config.ConfigUtils;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

public class CommandDebug implements Command {
    private static final String HELP = "Used internally for debugging. Use: '" + BotMain.prefix + "debug [debug arg]'";
    private static final boolean ALLOW_DEBUG = true;

    @Override
    public boolean called(String[] args, GuildMessageReceivedEvent event) {
        // TODO Auto-generated method stub
        return ALLOW_DEBUG;
    }

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) {
        // TODO Auto-generated method stub

        if (args.length > 0) {
            switch (args[0].toLowerCase()) {
                case "flushandinitguild":
                    GuildUtils.deleteGuild(event.getGuild());
                    com.srgood.dbot.utils.GuildUtils.initGuild(event.getGuild());
                    event.getChannel().sendMessage("Done");
                    break;
                case "deleteguild":
                    GuildUtils.deleteGuild(event.getGuild());
                    break;
                case "verifyxml":
                    event.getChannel().sendMessage("" + ConfigUtils.verifyConfig());
                    break;

                case "getuptime":
                    long seconds = Duration.between(BotMain.startInstant, Instant.now()).getSeconds();
                    long absSeconds = Math.abs(seconds);
                    String positive = String.format("%d:%02d:%02d", absSeconds / 3600, (absSeconds % 3600) / 60, absSeconds % 60);
                    String x = seconds < 0 ? "-" + positive : positive;
                    event.getChannel().sendMessage(x);
                    break;
                case "throw":
                    new Exception("Test Exception").printStackTrace();
                    break;
                case "audiotest":
                    //whatever url you want to pley...
                    String url = "https://www.youtube.com/watch?v=IxijStMHzyA";


                    break;
                case "rendertest":
                    int[] ops = {1,2,3,2,4};

                    try {
                        event.getChannel().sendFile(ImageUtils.renderVote("Test", new String[] {"A","B","C","D","E"},ops),null);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    event.getChannel().sendMessage("Invalid argument");
                    break;
            }
        } else {
            event.getChannel().sendMessage("Author Name: " + event.getAuthor().getUsername() + "\n" + "Author Nick: " + event.getAuthorNick() + "\n" + "id: " + event.getAuthor().getId() + "\n" + event.getAuthor().getAsMention() + "\n" + "Picture url: " + event.getAuthor().getAvatarUrl() + "\n" + "BotMain.jda.getSelfInfo().getAsMention().length()" + BotMain.jda.getSelfInfo().getAsMention().length() + "\n" + "BotMain.jda.getSelfInfo().getAsMention()" + BotMain.jda.getSelfInfo().getAsMention() + "\n" + "ConfigUtils.verifyConfig() = " + ConfigUtils.verifyConfig());
        }

    }

    @Override
    public String help() {
        // TODO Auto-generated method stub
        return HELP;
    }

    @Override
    public void executed(boolean success, GuildMessageReceivedEvent event) {
        // TODO Auto-generated method stub
    }

    @Override
    public com.srgood.dbot.PermissionLevels permissionLevel(Guild guild) {
        // TODO Auto-generated method stub
        return ConfigUtils.getCommandPermission(guild, this);
    }

    @Override
    public com.srgood.dbot.PermissionLevels defaultPermissionLevel() {
        // TODO Auto-generated method stub
        return com.srgood.dbot.PermissionLevels.STANDARD;
    }

    @Override
    public String[] names() {
        return new String[] {"debug"};
    }

}