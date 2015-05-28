package com.worldcretornica.plotme_core.utils;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Callable;

public class NameFetcher implements Callable<Map<String, String>> {

    private static final String PROFILE_URL = "https://sessionserver.mojang.com/session/minecraft/profile/";
    private final JSONParser jsonParser = new JSONParser();
    private final HashSet<String> uuids = new HashSet<>();

    public NameFetcher(Set<String> denied) {
        uuids.addAll(denied);
    }

    public static String getNameOf(String name) {
        return new NameFetcher(Collections.singleton(name)).call().get(name);
    }

    public static String getNameOf(UUID id) {
        return new NameFetcher(Collections.singleton(id.toString())).call().get(id.toString());
    }

    @Override
    public Map<String, String> call() {
        Map<String, String> uuidStringMap = new HashMap<>();
        for (String uuid : uuids) {
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) new URL(PROFILE_URL + uuid.replace("-", "")).openConnection();
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