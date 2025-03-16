package io.greitan.avion.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class JsonBase {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void savePlayerData(String name, String id, ObjectNode node) {
        File dir = new File("./plugins/PlayerCorpses/" + name);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File file = new File(dir, id + ".json");

        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, node);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static UUID generateUUIDv4() {
        return UUID.randomUUID();
    }
}
