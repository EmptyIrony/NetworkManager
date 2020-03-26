package com.emptyirony.networkmanager.packet;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.minexd.pidgin.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.md_5.bungee.api.chat.TextComponent;
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

    static {
        serializer = new TextComponentSerializer();
    }

    private TextComponent component;
    private String sender;

    @Override
    public int id() {
        return 80;
    }

    @Override
    public JsonObject serialize() {
        JsonObject json = new JsonObject();
        JsonElement element = serializer.serialize(component, json.getClass(), null);
        json.add("msg", element);
        json.addProperty("sender", sender);

        return json;
    }

    @Override
    public void deserialize(JsonObject json) {
        JsonElement text = json.get("msg");

        this.component = serializer.deserialize(text, json.getClass(), null);

        this.sender = json.get("sender").getAsString();
    }
}
