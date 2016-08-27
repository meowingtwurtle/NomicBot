package com.srgood.dbot.commands;

import com.meowingtwurtle.math.api.IMathGroup;
import com.meowingtwurtle.math.api.IMathHandler;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class CommandEval implements Command {

    private final static NumberFormat RESULT_FORMATTER = new DecimalFormat("#0.0###");

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
            String exp = join(args);

            if (!exp.matches("[()\\d\\w\\s.+\\-*/^]+")) {
                throw new com.meowingtwurtle.math.api.MathExpressionParseException("Expression contains invalid characters");
            }

            IMathGroup group = IMathHandler.getMathHandler().parse(exp);
            event.getChannel().sendMessage("`MATH:` " + RESULT_FORMATTER.format(group.eval()));
        } catch (Exception e) {
            e.printStackTrace();
            event.getChannel().sendMessage("`MATH:` An error occurred during parsing.");
        }
    }

    private String join(Object[] arr) {
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
    }

    @Override
    public com.srgood.dbot.PermissionLevels permissionLevel(Guild guild) {
        // TODO Auto-generated method stub
        return com.srgood.dbot.utils.XMLUtils.getCommandPermissionXML(guild, this);
    }

    @Override
    public com.srgood.dbot.PermissionLevels defaultPermissionLevel() {
        return com.srgood.dbot.PermissionLevels.STANDARD;
    }

}
