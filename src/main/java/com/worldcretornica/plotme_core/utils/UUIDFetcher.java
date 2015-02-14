package com.worldcretornica.plotme_core.utils;

import com.google.common.collect.ImmutableList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;

public class UUIDFetcher implements Callable<Map<String, UUID>> {

    public static final double PROFILES_PER_REQUEST = 100;
    private static final String PROFILE_URL = "https://api.mojang.com/profiles/minecraft";
    private static final String PROFILE_AT_TIME_URL = "https://api.mojang.com/users/profiles/minecraft/@USERNAME@?at=1420156800";
    private final JSONParser jsonParser = new JSONParser();
    private final List<String> names;
    private final boolean rateLimiting;

    public UUIDFetcher(List<String> names, boolean rateLimiting) {
        this.names = ImmutableList.copyOf(names);
        this.rateLimiting = rateLimiting;
    }

    public UUIDFetcher(List<String> names) {
        this(names, true);
    }

    private static void writeBody(HttpURLConnection connection, String body) throws Exception {
        OutputStream stream = connection.getOutputStream();
        stream.write(body.getBytes());
        stream.flush();
        stream.close();
    }

    private static HttpURLConnection createConnection() throws Exception {
        URL url = new URL(PROFILE_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setUseCaches(false);
        connection.setDoInput(true);
        connection.setDoOutput(true);
        return connection;
    }

    private static HttpURLConnection createAtTimeConnection(String username) throws Exception {
        URL url = new URL(PROFILE_AT_TIME_URL.replace("@USERNAME@", username));
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setUseCaches(false);
        connection.setDoInput(true);
        connection.setDoOutput(false);
        return connection;
    }

    public static UUID getUUID(String id) {
        return UUID.fromString(id.substring(0, 8) + "-" + id.substring(8, 12) + "-" + id.substring(12, 16) + "-" + id.substring(16, 20) + "-" + id
                .substring(20, 32));
    }

    public static byte[] toBytes(UUID uuid) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[16]);
        byteBuffer.putLong(uuid.getMostSignificantBits());
        byteBuffer.putLong(uuid.getLeastSignificantBits());
        return byteBuffer.array();
    }

    public static UUID fromBytes(byte[] array) {
        if (array.length != 16) {
            throw new IllegalArgumentException("Illegal byte array length: " + array.length);
        }
        ByteBuffer byteBuffer = ByteBuffer.wrap(array);
        long mostSignificant = byteBuffer.getLong();
        long leastSignificant = byteBuffer.getLong();
        return new UUID(mostSignificant, leastSignificant);
    }

    public static UUID getUUIDOf(String name) {
        return new UUIDFetcher(Collections.singletonList(name)).call().get(name.toLowerCase());
    }

    @Override
    public Map<String, UUID> call() {
        Map<String, UUID> uuidMap = new HashMap<>();
        int requests = (int) Math.ceil(names.size() / PROFILES_PER_REQUEST);
        int retries = 0;

        for (int i = 0; i < requests; i++) {

            List<String> missinguuid = new ArrayList<>();

            //First step, get people that didn't change name
            boolean success = false;
            while (!success) {
                try {
                    HttpURLConnection connection = createConnection();
                    List<String> sublist = names.subList(i * 100, Math.min((i + 1) * 100, names.size()));
                    String body = JSONArray.toJSONString(sublist);
                    writeBody(connection, body);
                    JSONArray array = (JSONArray) jsonParser.parse(new InputStreamReader(connection.getInputStream()));
                    for (Object profile : array) {
                        JSONObject jsonProfile = (JSONObject) profile;
                        String id = (String) jsonProfile.get("id");
                        String name = (String) jsonProfile.get("name");
                        UUID uuid = UUIDFetcher.getUUID(id);
                        uuidMap.put(name.toLowerCase(), uuid);
                    }

                    for (String name : sublist) {
                        if (!uuidMap.containsKey(name)) {
                            missinguuid.add(name);
                        }
                    }

                    success = true;
                } catch (Exception ex) {
                    ex.printStackTrace();
                    try {
                        //If we got an exception, retry in 30 seconds
                        Thread.sleep(30000L);
                    } catch (InterruptedException ignored) {
                    }
                    if (retries > 0 && retries % 10 == 0) {
                        //Bukkit.getLogger().warning("The UUID fetcher has been trying for " + retries + " times to get UUIDs.");
                    }
                    retries += 1;
                }
            }

            //Step 2, check people that changed name since Febuary 2nd
            if (!missinguuid.isEmpty()) {
                for (String name : missinguuid) {
                    success = false;
                    while (!success) {
                        HttpURLConnection connection = null;

                        try {
                            connection = createAtTimeConnection(name);
                        } catch (Exception ex) {
                            //ex.printStackTrace();
                            try {
                                //If we got an exception, retry in 30 seconds
                                Thread.sleep(30000L);
                            } catch (InterruptedException ignored) {
                            }
                            if (retries > 0 && retries % 20 == 0) {
                                //Bukkit.getLogger().warning("The UUID fetcher has been trying for " + retries + " times to get UUIDs.");
                            }
                            retries += 1;
                        }

                        if (connection != null) {
                            try {
                                JSONObject jsonProfile = (JSONObject) jsonParser.parse(new InputStreamReader(connection.getInputStream()));
                                String id = (String) jsonProfile.get("id");
                                String newname = (String) jsonProfile.get("name");
                                UUID uuid = UUIDFetcher.getUUID(id);
                                uuidMap.put(name.toLowerCase() + ";" + newname, uuid);
                            } catch (Exception ex) {
                                //Unable to find name at mojang...
                                uuidMap.put(name.toLowerCase() + ";", null);
                            }
                            success = true;
                        } else {
                            //Unable to find name at mojang...
                            uuidMap.put(name.toLowerCase() + ";", null);
                        }
                    }
                }
            }

            if (rateLimiting && i != requests - 1) {
                try {
                    Thread.sleep(1100L);
                } catch (InterruptedException ignored) {
                }
            }

            retries = 0;
        }
        return uuidMap;
    }
}
