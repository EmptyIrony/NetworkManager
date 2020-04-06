package com.emptyirony.networkmanager.packet;

import com.google.gson.JsonObject;
import com.emptyirony.networkmanager.pidgin.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 2 * @Author: EmptyIrony
 * 3 * @Date: 2020/3/9 22:45
 * 4
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PacketServerShutdown implements Packet {
    private String server;
    private String player;
    private int delay;
    private String reason;

    @Override
    public int id() {
        return 54;
    }

    @Override
    public JsonObject serialize() {
        JsonObject json = new JsonObject();
        json.addProperty("server", server);
        json.addProperty("player", player);
        json.addProperty("delay", delay);
        json.addProperty("reason", reason);

        return json;
    }

    @Override
    public void deserialize(JsonObject object) {
        this.server = object.get("server").getAsString();
        this.player = object.get("player").getAsString();
        this.delay = object.get("delay").getAsInt();
        this.reason = object.get("reason").getAsString();
    }
}
