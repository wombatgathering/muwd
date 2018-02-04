package com.wgg.muwd.command;

import com.wgg.muwd.client.PlayerCharacter;
import com.wgg.muwd.world.World;

import java.util.Optional;

public class SayCommand extends Command {

    @Override
    public String getCommandValue() {
        return "say";
    }

    @Override
    public Optional<String> getResponse(String[] input, World world, PlayerCharacter client) {
        return null; //todo implement this
    }

    @Override
    public String getHelpText() {
        return "Say something that only people in the same room will hear.";
    }

}
