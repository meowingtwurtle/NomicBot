package com.srgood.reasons.commands.old;

import com.srgood.reasons.utils.MethodInvocationUtils;
import com.srgood.reasons.utils.RuntimeCompiler;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.RateLimitedException;

public class CommandExecute implements Command {

    private static final String[] IMPORTS = { "import com.srgood.reasons.*", "import net.dv8tion.jda.core.*", "import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent" };

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) throws RateLimitedException {

        String classDeclaration = "public class RuntimeClass {" + "public static void run(GuildMessageReceivedEvent event) {";

        String closingBraces = "}" + "}";

        RuntimeCompiler r = new RuntimeCompiler();

        String importDeclarations = String.join(";\n", IMPORTS) + ";";

        String rawCode = event.getMessage().getContent().split("```")[1];

        String code =  importDeclarations + classDeclaration + rawCode + closingBraces;

        System.out.println(code);
        r.addClass("RuntimeClass", code);
        r.compile();

        MethodInvocationUtils.invokeStaticMethod(r.getCompiledClass("RuntimeClass"), "run", event);
    }

    @Override
    public String help() {
        return null;
    }
}
