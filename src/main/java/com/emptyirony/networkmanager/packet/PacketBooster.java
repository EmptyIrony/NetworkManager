package com.emptyirony.networkmanager.packet;

import com.google.gson.JsonObject;
import com.minexd.pidgin.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * 2 * @Author: EmptyIrony
 * 3 * @Date: 2020/3/12 17:27
 * 4
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PacketBooster implements Packet {
    private UUID uuid;
    private String name;
    private String game;

    @Override
    public int id() {
        return 55;
    }

    @Override
    public JsonObject serialize() {
        JsonObject json = new JsonObject();
        json.addProperty("uuid", uuid.toString());
        json.addProperty("name", name);
        json.addProperty("game", game);
        return json;
    }

    @Override
    public void deserialize(JsonObject object) {
        this.uuid = UUID.fromString(object.get("uuid").getAsString());
        this.name = object.get("name").getAsString();
        this.game = object.get("game").getAsString();
    }
}
