package com.wgg.muwd.command.service;

import com.wgg.muwd.command.Command;
import com.wgg.muwd.world.World;
import org.reflections.Reflections;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Scope("singleton")
public class CommandRegistry {

    private Map<String, Command> registeredCommands;

    public CommandRegistry() {
        loadCommands();
    }

    private void loadCommands() {
        initCommandMap();
        Set<Class<? extends Command>> allCommandClasses = getAllCommandClasses();
        registerAllCommands(allCommandClasses);
    }

    private void initCommandMap() {
        if(null == registeredCommands) {
            registeredCommands = new HashMap<>();
        } else {
            registeredCommands.clear();
        }
    }

    private Set<Class<? extends Command>> getAllCommandClasses() {
        Reflections reflections = new Reflections("com.wgg.muwd.command");
        Set<Class<? extends Command>> sourcesInPackage = reflections.getSubTypesOf(Command.class);
        return sourcesInPackage;
    }

    private void registerAllCommands(Set<Class<? extends Command>> allCommandClasses) {
        for (Class<? extends Command> commandClass : allCommandClasses) {
            Command command = null;
            try {
                command = commandClass.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                System.out.println("error initializing command -> " + commandClass.getName());
                e.printStackTrace();
            }

            registerCommand(command);
            System.out.println("registered command -> " + command.getCommandValue());
        }
    }

    public void registerCommand(Command command) {
        registeredCommands.put(command.getCommandValue(), command);
        for (String alias : command.getAliases()) {
            registeredCommands.put(alias, command);
        }
    }

    public Optional<Command> getCommandByValue(String value) {
        Command command = registeredCommands.get(value);
        return Optional.ofNullable(command);
    }

    public List<Command> getAllCommands() {
        Collection<Command> allCommands = registeredCommands.values();
        List<Command> allCommandsWithoutDuplicates =
                allCommands.parallelStream().distinct().collect(Collectors.toList());

        return allCommandsWithoutDuplicates;
    }

    public List<Command> getAllCommandsForWorld(World world) {
        List<Command> commands = getAllCommands();
        List<String> enabledCommands = world.getEnabledCommands();

        return commands.stream()
                .filter(command -> enabledCommands.contains(command.getCommandValue()))
                .collect(Collectors.toList());
    }

    public List<String> getAllCommandValues() {
        return registeredCommands.values().stream().map(Object::toString).collect(Collectors.toList());
    }
}
