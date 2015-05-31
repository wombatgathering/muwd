package com.wgg.muwd.commands;

import com.wgg.muwd.service.CommandRegistry;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;

@Component
public class HelpCommand extends Command {

    @Override
    public String getCommandValue() {
        return "help";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("commands", "?");
    }

    @Override
    public String getResponse(String[] input, CommandRegistry commandRegistry) {
        if (input.length > 1) {
            Command command = commandRegistry.getCommandByValue(input[1]);
            if (null != command) {
                return generateHelpTextForCommand(command);
            }
        }

        return getCommaDelimitedListOfAvailableCommands(commandRegistry);
    }

    @Override
    public String getHelpText() {
        return "Provides a list of commands as well as more specific help information.";
    }

    private String getCommaDelimitedListOfAvailableCommands(CommandRegistry commandRegistry) {
        StringBuilder response = new StringBuilder();

        response.append("List of available commands: <span style='color:red;'>");

        List<Command> allCommands = commandRegistry.getAllCommands();
        String commaDelimitedStringOfAllCommands = StringUtils.collectionToDelimitedString(allCommands, ", ");
        response.append(commaDelimitedStringOfAllCommands);

        response.append("</span>");

        return response.toString();
    }

    private String generateHelpTextForCommand(Command command) {
        StringBuilder stringBuilder = new StringBuilder();

        List<String> aliasesForThisCommand = command.getAliases();
        String commaDelimitedListOfAliases = StringUtils.collectionToDelimitedString(aliasesForThisCommand, ", ");

        stringBuilder.append("Help for command: '<span style='color:green;'>" + command.getCommandValue() + "</span>'<br/><br/>");
        stringBuilder.append(command.getHelpText() + "<br/><br/>");
        stringBuilder.append("Other aliases for this command: <span style='color:red;'>" + commaDelimitedListOfAliases + "</span><br/>");

        return stringBuilder.toString();
    }
}
