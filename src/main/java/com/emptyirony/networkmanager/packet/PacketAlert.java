package com.emptyirony.networkmanager.packet;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.minexd.pidgin.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import net.md_5.bungee.chat.TextComponentSerializer;

/**
 * 2 * @Author: EmptyIrony
 * 3 * @Date: 2020/3/26 15:37
 * 4
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PacketAlert implements Packet {
    private static TextComponentSerializer serializer;
    private static Gson gson;

    static {
        serializer = new TextComponentSerializer();
    }

    private BaseComponent[] component;
    private String player;
    private String permission;

    @Override
    public int id() {
        return 80;
    }

    @Override
    public JsonObject serialize() {
        JsonObject json = new JsonObject();
        String string = ComponentSerializer.toString(component);
        json.addProperty("msg", string);
        if (player != null) {
            json.addProperty("player", player);
        }
        if (permission != null) {
            json.addProperty("permission", permission);
        }

        return json;
    }

    @Override
    public void deserialize(JsonObject json) {
        String msg = json.get("msg").getAsString();
        this.component = ComponentSerializer.parse(msg);
        this.player = json.get("player") == null ? null : json.get("player").getAsString();
        this.permission = json.get("permission") == null ? null : json.get("permission").getAsString();
    }
}
