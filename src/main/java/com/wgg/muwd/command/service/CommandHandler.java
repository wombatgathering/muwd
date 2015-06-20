package com.wgg.muwd.command.service;

import com.wgg.muwd.command.Command;
import com.wgg.muwd.controller.model.CommandWrapper;
import com.wgg.muwd.controller.model.ResponseWrapper;
import com.wgg.muwd.websocket.ClientRegistry;
import com.wgg.muwd.world.service.WorldBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Service
public class CommandHandler {

    private CommandRegistry commandRegistry;

    private ClientRegistry clientRegistry;

    private WorldBuilder worldBuilder;

    @Autowired
    public CommandHandler(CommandRegistry commandRegistry, ClientRegistry clientRegistry, WorldBuilder worldBuilder) {
        this.commandRegistry = commandRegistry;
        this.clientRegistry = clientRegistry;
        this.worldBuilder = worldBuilder;
    }

    public String handleCommandInput(CommandWrapper commandWrapper) {
        String inputText = commandWrapper.getCommand();
        String[] inputTextSplit = getInputTextSplitBySpaces(inputText);

        String commandValue = inputTextSplit[0];
        Optional<Command> commandOptional = commandRegistry.getCommandByValue(commandValue);

        String response;
        if(isValidCommand(commandOptional)) {
            Command command = commandOptional.get();
            response = command.getResponse(inputTextSplit, commandRegistry, clientRegistry);
        } else {
            response = "Unrecognized command: '" + inputText + "'";
        }

        return response;
    }

    private boolean isValidCommand(Optional<Command> commandOptional) {
        List<String> listOfEnabledCommands = worldBuilder.getListOfEnabledCommands();
        boolean commandIsPresentInSystem = commandOptional.isPresent();
        boolean commandIsEnabledInWorld = commandIsPresentInSystem ? listOfEnabledCommands.contains(commandOptional.get().getCommandValue()) : false;

        return commandIsPresentInSystem && commandIsEnabledInWorld;
    }

    private String[] getInputTextSplitBySpaces(String inputText) {
        String[] inputTextSplit = StringUtils.split(inputText, " ");
        if (null == inputTextSplit) {
            inputTextSplit = new String[]{inputText};
        }
        return inputTextSplit;
    }
}
