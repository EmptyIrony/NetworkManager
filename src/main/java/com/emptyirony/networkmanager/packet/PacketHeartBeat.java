package com.emptyirony.networkmanager.packet;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.minexd.pidgin.packet.Packet;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * 2 * @Author: EmptyIrony
 * 3 * @Date: 2020/2/24 0:45
 * 4
 */
@Getter
public class PacketHeartBeat implements Packet {
    private String name;
    private List<String> onlinePlayers;
    private String motd;
    private double tps;
    private long time;

    public PacketHeartBeat(String name, List<String> onlinePlayers, String motd, double tps) {
        this.name = name;
        this.onlinePlayers = onlinePlayers;
        if (motd.equalsIgnoreCase("a minecraft server")) {
            this.motd = "默认";
        } else {
            this.motd = motd;
        }
        this.time = System.currentTimeMillis();
        this.tps = tps;
    }


    public PacketHeartBeat() {
    }

    @Override
    public int id() {
        return 50;
    }

    @Override
    public JsonObject serialize() {
        JsonObject json = new JsonObject();
        json.addProperty("motd", motd);
        json.addProperty("name", name);
        json.addProperty("time", time);
        json.addProperty("tps", tps);
        JsonArray array = new JsonArray();
        for (String player : onlinePlayers) {
            array.add(new JsonPrimitive(player));
        }
        json.add("online", array);
        return json;
    }

    @Override
    public void deserialize(JsonObject object) {
        this.motd = object.get("motd").getAsString();
        this.name = object.get("name").getAsString();
        this.time = object.get("time").getAsLong();
        this.tps = object.get("tps").getAsDouble();
        this.onlinePlayers = new ArrayList<>();
        if (!object.getAsJsonArray("online").isJsonNull()) {
            for (JsonElement element : object.get("online").getAsJsonArray()) {
                this.onlinePlayers.add(element.getAsString());
            }
        }

    }
}
