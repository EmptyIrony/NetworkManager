package com.emptyirony.networkmanager.network.packet;

import com.google.gson.JsonObject;
import com.minexd.pidgin.packet.Packet;
import lombok.Getter;

/**
 * 2 * @Author: EmptyIrony
 * 3 * @Date: 2020/2/24 19:49
 * 4
 */
@Getter
public class PacketStaffMsg implements Packet {
    private String name;
    private String msg;
    private int type;

    public PacketStaffMsg(String name, String msg, int type) {
        this.name = name;
        this.msg = msg;
        this.type = type;
    }

    public PacketStaffMsg() {
    }


    @Override
    public int id() {
        return 51;
    }

    @Override
    public JsonObject serialize() {
        JsonObject json = new JsonObject();
        json.addProperty("name", name);
        json.addProperty("msg", msg);
        json.addProperty("type", type);

        return json;
    }

    @Override
    public void deserialize(JsonObject object) {
        this.name = object.get("name").getAsString();
        this.msg = object.get("msg").getAsString();
        this.type = object.get("type").getAsInt();
    }
}
