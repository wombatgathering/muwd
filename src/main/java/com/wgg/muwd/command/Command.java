package com.wgg.muwd.command;

import com.wgg.muwd.websocket.Client;
import com.wgg.muwd.world.World;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

abstract public class Command {

    abstract public String getCommandValue();

    public List<String> getAliases() {
        return Collections.emptyList();
    }

    abstract public Optional<String> getResponse(String[] input,
                                                 World world,
                                                 Client client);

    abstract public String getHelpText();

    @Override
    public String toString() {
        return getCommandValue();
    }
}
