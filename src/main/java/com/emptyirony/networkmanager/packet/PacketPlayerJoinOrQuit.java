package com.emptyirony.networkmanager.packet;

import com.google.gson.JsonObject;
import com.emptyirony.networkmanager.pidgin.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 2 * @Author: EmptyIrony
 * 3 * @Date: 2020/3/15 2:27
 * 4
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PacketPlayerJoinOrQuit implements Packet {
    private String name;
    private boolean join;

    @Override
    public int id() {
        return 56;
    }

    @Override
    public JsonObject serialize() {
        JsonObject json = new JsonObject();
        json.addProperty("name", name);
        json.addProperty("isJoin", join);

        return json;
    }

    @Override
    public void deserialize(JsonObject json) {
        this.name = json.get("name").getAsString();
        this.join = json.get("isJoin").getAsBoolean();
    }
}
