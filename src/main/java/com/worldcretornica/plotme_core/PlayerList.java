package com.worldcretornica.plotme_core;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

public class PlayerList {

    private PlotMe_Core api;
    private HashMap<String, UUID> playerlist;

    public PlayerList(PlotMe_Core api) {
        this.api = api;
    }

    public PlayerList() {
        playerlist = new HashMap<>();
    }

    public PlayerList(HashMap<String, UUID> players) {
        playerlist = players;
    }

    public void put(String name) {
        put(name, null);
    }

    public void put(String name, UUID uuid) {
        playerlist.put(name, uuid);
    }

    public String put(UUID uuid) {
        String name = api.getServerBridge().getOfflinePlayer(uuid).getName();
        playerlist.put(name, uuid);
        return name;
    }

    public UUID remove(String name) {
        String found = "";
        for (String key : playerlist.keySet()) {
            if (key.equalsIgnoreCase(name)) {
                found = key;
            }
        }
        UUID uuid = null;
        if (!found.isEmpty()) {
            uuid = playerlist.get(found);
            playerlist.remove(found);
        }
        return uuid;
    }

    public String remove(UUID uuid) {
        for (String name : playerlist.keySet()) {
            if (playerlist.get(name).equals(uuid)) {
                playerlist.remove(name);
                return name;
            }
        }
        return "";
    }

    public Set<String> getPlayers() {
        return playerlist.keySet();
    }

    public String getPlayerList() {
        StringBuilder list = new StringBuilder();

        for (String s : playerlist.keySet()) {
            list = list.append(s + ", ");
        }
        if (list.length() > 1) {
            list = list.delete(list.length() - 2, list.length());
        }
        return list.toString();
    }

    public boolean contains(String name) {
        for (String key : playerlist.keySet()) {
            if (key.equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    public boolean contains(UUID uuid) {
        return playerlist.values().contains(uuid);
    }

    public HashMap<String, UUID> getAllPlayers() {
        return playerlist;
    }

    public void clear() {
        playerlist.clear();
    }

    public int size() {
        return playerlist.size();
    }

    public void replace(UUID uuid, String newname) {
        if (uuid != null && playerlist != null) {
            if (contains(uuid)) {
                Iterator<String> it = playerlist.keySet().iterator();
                while (it.hasNext()) {
                    String name = it.next();

                    if (playerlist.get(name) != null && playerlist.get(name).equals(uuid)) {
                        playerlist.remove(name);
                        playerlist.put(newname, uuid);
                        return;
                    }
                }
            }
        }
    }

    public void replace(String name, UUID newuuid) {
        if (newuuid != null && playerlist != null) {
            if (contains(name)) {
                Iterator<String> it = playerlist.keySet().iterator();
                while (it.hasNext()) {
                    String key = it.next();

                    if (key.equalsIgnoreCase(name)) {
                        playerlist.remove(key);
                        playerlist.put(name, newuuid);
                        return;
                    }
                }
            }
        }
    }
}