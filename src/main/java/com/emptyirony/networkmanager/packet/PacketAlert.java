package com.emptyirony.networkmanager.packet;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.minexd.pidgin.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.TextComponentSerializer;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;

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

    private TextComponent component;
    private String sender;

    @Override
    public int id() {
        return 80;
    }

    @Override
    public JsonObject serialize() {
        JsonObject json = new JsonObject();

        json.add("msg", serializer.serialize(component, component.getClass(), null));
        json.addProperty("sender", sender);

        return json;
    }

    @Override
    public void deserialize(JsonObject json) {
        JsonElement text = json.get("msg");
        IChatBaseComponent a = IChatBaseComponent.ChatSerializer.a(text.toString());
        PacketPlayOutChat chat = new PacketPlayOutChat(a, (byte) 0);
        Bukkit.getOnlinePlayers().forEach(player -> {
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(chat);
        });

        this.sender = json.get("sender").getAsString();
    }
}
