package com.wgg.muwd.world.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wgg.muwd.websocket.Client;
import com.wgg.muwd.world.Room;
import com.wgg.muwd.world.World;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class WorldManager implements EnvironmentAware {

    static final String DEFAULT_WORLD_FILE = "worlds/shack.world";
    static final String WORLD_FILE_PARAM_KEY = "world.file";
    private World world;

    //can specify param to load a specific world file when the server starts
    //ie: java -jar muwd.jar --world.file=your.world
    @Override
    public void setEnvironment(Environment environment) {
        File worldFile = getWorldFileFromEnvironmentProperty(environment);

        if (!worldFile.exists()) {
            throw new IllegalArgumentException("Could not load world file! " + worldFile.getAbsolutePath());
        }

        loadWorld(worldFile);
    }

    private File getWorldFileFromEnvironmentProperty(Environment environment) {
        String worldFileLocation = environment.getProperty(WORLD_FILE_PARAM_KEY);
        if (StringUtils.isEmpty(worldFileLocation)) {
            worldFileLocation = DEFAULT_WORLD_FILE;
        }
        return new File(worldFileLocation);
    }

    private void loadWorld(File worldFile) {
        log.info("Loading world file: {}", worldFile.getAbsolutePath());

        ObjectMapper mapper = new ObjectMapper();
        try {
            world = mapper.readValue(worldFile, World.class);
        } catch (IOException e) {
            log.error("Failed to load a world!", e);
            throw new RuntimeException("Failed to load a world!");
        }
    }

    public Optional<Room> getRoomById(Long id) {
        return world.getRoomById(id);
    }

    public Room getCurrentRoom(Client client) {
        return getRoomById(client.getCurrentRoom()).get();
    }

    public List<String> getListOfEnabledCommands() {
        List<String> enabledCommands = new ArrayList<>();

        if (null != world) {
            enabledCommands = world.getEnabledCommands();
        }

        return enabledCommands;
    }

    public Optional<World> getCurrentlyLoadedWorld() {
        if (null == world) {
            return Optional.empty();
        } else {
            return Optional.of(world);
        }
    }

    boolean isWorldLoaded() {
        return getCurrentlyLoadedWorld().isPresent();
    }
}
