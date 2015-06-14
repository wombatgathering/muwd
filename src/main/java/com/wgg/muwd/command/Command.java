package com.wgg.muwd.command;

import com.wgg.muwd.command.service.CommandRegistry;
import com.wgg.muwd.websocket.ClientRegistry;

import java.util.Collections;
import java.util.List;

abstract public class Command {

    abstract public String getCommandValue();

    public List<String> getAliases() {
        return Collections.emptyList();
    }

    abstract public String getResponse(String[] input, CommandRegistry commandRegistry, ClientRegistry clientRegistry);

    abstract public String getHelpText();

    @Override
    public String toString() {
        return getCommandValue();
    }
}
