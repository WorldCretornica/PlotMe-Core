package com.worldcretornica.plotme_core.utils;

import com.google.common.collect.ImmutableList;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;

public class NameFetcher implements Callable<Map<UUID, String>> {

    private static final String PROFILE_URL = "https://sessionserver.mojang.com/session/minecraft/profile/";
    private final JSONParser jsonParser = new JSONParser();
    private final List<UUID> uuids;

    public NameFetcher(List<UUID> uuids) {
        this.uuids = ImmutableList.copyOf(uuids);
    }

    public NameFetcher(HashSet<String> denied) {
        uuids = new ArrayList<>();
        for (String uuid : denied) {
            uuids.add(UUID.fromString(uuid));
        }
    }

    public static String getNameOf(String name) {
        return new NameFetcher(Collections.singletonList(UUID.fromString(name))).call().get(UUID.fromString(name));
    }

    public static String getNameOf(UUID id) {
        return new NameFetcher(Collections.singletonList(id)).call().get(id);
    }

    @Override
    public Map<UUID, String> call() {
        Map<UUID, String> uuidStringMap = new HashMap<>();
        for (UUID uuid : uuids) {
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) new URL(PROFILE_URL + uuid.toString().replace("-", "")).openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
            JSONObject response;
            if (connection != null) {
                try (InputStreamReader reader = new InputStreamReader(connection.getInputStream())) {
                    try {
                        response = (JSONObject) jsonParser.parse(reader);
                        String name = (String) response.get("name");
                        if (name == null) {
                            continue;
                        }
                        uuidStringMap.put(uuid, name);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    e.getCause();
                    e.getMessage();
                }
            }
        }
        return uuidStringMap;
    }
}