package com.srgood.reasons.commands;




import com.srgood.reasons.utils.MethodInvocationUtils;
import com.srgood.reasons.utils.RuntimeCompiler;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.RateLimitedException;



/**
 * Created by srgood on 1/17/2017.
 */
public class CommandExecute implements Command {

    /*
    import java.*;
    import net.dv8tion.*;
    public class RuntimeClass {
        public static void myMethod() {

        }
    }
     */

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) throws RateLimitedException {

        String pre = "public class RuntimeClass {" + "\n" +
                "    public static void exampleMethod() {" + "\n";

        String post = "\n    }" + "\n" +
                "}" + "\n";

        RuntimeCompiler r = new RuntimeCompiler();
        String code ="import com.srgood.reasons.*;\nimport net.dv8tion.*;\n\n" + pre + "        " + event.getMessage().getContent().split("```")[1] + post;

        System.out.println(code);
        r.addClass("RuntimeClass",code);
        r.compile();

        MethodInvocationUtils.invokeStaticMethod(r.getCompiledClass("RuntimeClass"),"exampleMethod");
    }

    @Override
    public String help() {
        return null;
    }

    @Override
    public PermissionLevels defaultPermissionLevel() {
        return PermissionLevels.DEVELOPER;
    }




}
