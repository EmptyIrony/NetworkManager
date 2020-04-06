package com.emptyirony.networkmanager.packet;

import com.google.gson.JsonObject;
import com.emptyirony.networkmanager.pidgin.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 2 * @Author: EmptyIrony
 * 3 * @Date: 2020/2/26 9:52
 * 4
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PacketStaffSwitchServer implements Packet {
    private String player;
    private String from;
    private String to;
    private int type;
    private long time;


    @Override
    public int id() {
        return 53;
    }

    @Override
    public JsonObject serialize() {
        JsonObject json = new JsonObject();
        json.addProperty("player", player);
        json.addProperty("from", from);
        json.addProperty("to", to);
        json.addProperty("type", type);
        json.addProperty("time", time);

        return json;
    }

    @Override
    public void deserialize(JsonObject object) {
        this.player = object.get("player").getAsString();
        this.from = object.get("from").getAsString();
        this.to = object.get("to").getAsString();
        this.type = object.get("type").getAsInt();
        this.time = object.get("time").getAsLong();
    }
}
