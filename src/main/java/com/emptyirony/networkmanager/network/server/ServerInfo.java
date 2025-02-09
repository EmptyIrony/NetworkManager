package com.emptyirony.networkmanager.network.server;

import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 2 * @Author: EmptyIrony
 * 3 * @Date: 2020/2/24 0:58
 * 4
 */
@Data
public class ServerInfo {
    private static Map<String, ServerInfo> cache = new HashMap<>();

    private String serverName;
    private List<String> players;
    private String motd;
    private long lastHeartBeat;
    private double tps;

    public ServerInfo(String serverName, List<String> players, String motd, long time, double tps) {
        this.serverName = serverName;
        this.players = players;
        this.motd = motd;
        this.lastHeartBeat = time;
        this.tps = tps;
        cache.put(serverName, this);
    }

    public static ServerInfo getServerByName(String name) {
        return cache.get(name);
    }

    public static boolean isPlayerOnline(String name) {
        for (String s : cache.keySet()) {
            for (String player : cache.get(s).getPlayers()) {
                if (player.equalsIgnoreCase(name)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static Map<String, ServerInfo> getCache() {
        return cache;
    }

    public static String getPlayerServer(String player) {
        for (String s : cache.keySet()) {
            for (String p : cache.get(s).getPlayers()) {
                if (p.equalsIgnoreCase(player)) {
                    return s;
                }
            }
        }
        return null;
    }
}
