package com.emptyirony.networkmanager.packet;

import com.google.gson.JsonObject;
import com.minexd.pidgin.packet.Packet;
import lombok.Getter;

/**
 * 2 * @Author: EmptyIrony
 * 3 * @Date: 2020/2/25 23:39
 * 4
 */
@Getter
public class PacketFriendRequest implements Packet {
    private String name;
    private String target;
    private boolean force;
    private long time;

    public PacketFriendRequest(String name, String target, boolean force) {
        this.name = name;
        this.target = target;
        this.force = force;
        this.time = System.currentTimeMillis();
    }

    public PacketFriendRequest() {
    }

    @Override
    public int id() {
        return 52;
    }

    @Override
    public JsonObject serialize() {
        JsonObject json = new JsonObject();
        json.addProperty("name", name);
        json.addProperty("target", target);
        json.addProperty("force", force);
        json.addProperty("time", time);

        return json;
    }

    @Override
    public void deserialize(JsonObject object) {
        this.name = object.get("name").getAsString();
        this.target = object.get("target").getAsString();
        this.force = object.get("force").getAsBoolean();
        this.time = object.get("time").getAsLong();
    }
}
