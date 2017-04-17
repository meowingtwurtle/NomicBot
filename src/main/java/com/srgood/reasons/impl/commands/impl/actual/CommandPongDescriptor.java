package com.srgood.reasons.impl.commands.impl.actual;

import com.srgood.reasons.impl.commands.impl.base.descriptor.SimpleTextCommandDescriptor;

public class CommandPongDescriptor extends SimpleTextCommandDescriptor {
    public CommandPongDescriptor() {
        super("pong", "Ping!");
    }
}
