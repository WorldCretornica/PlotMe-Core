package com.worldcretornica.plotme_core;

import java.util.ArrayList;
import java.util.List;

public class PlayerList {

    private final List<String> playerList;
    private PlotMe_Core api;

    public PlayerList() {
        playerList = new ArrayList<>();
    }

    public PlayerList(ArrayList<String> players) {
        playerList = players;
    }

    public void add(String name) {
        put(name);
    }

    public void put(String name) {
        playerList.add(name);
    }

    public void remove(String name) {
        for (String key : playerList) {
            if (key.equalsIgnoreCase(name)) {
                playerList.remove(name);
            }
        }
    }

    public List<String> getPlayers() {
        return playerList;
    }

    public String getPlayerList() {
        StringBuilder list = new StringBuilder();

        for (String s : playerList) {
            list = list.append(s + ", ");
        }
        if (list.length() > 1) {
            list = list.delete(list.length() - 2, list.length());
        }
        return list.toString();
    }

    public boolean contains(String name) {
        for (String key : playerList) {
            if (key.equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    public List<String> getAllPlayers() {
        return playerList;
    }

    public void clear() {
        playerList.clear();
    }

    public int size() {
        return playerList.size();
    }

    public void replace(String oldname, String newname) {
        if (playerList != null && contains(oldname)) {
            playerList.remove(oldname);
            playerList.add(newname);
        }
    }
}
