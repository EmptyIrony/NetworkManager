package com.emptyirony.networkmanager.util;

import com.emptyirony.networkmanager.NetworkManager;
import com.emptyirony.networkmanager.packet.PacketAlert;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.plugin.java.JavaPlugin;
import strafe.games.core.util.ChatComponentBuilder;

/**
 * 2 * @Author: EmptyIrony
 * 3 * @Date: 2020/3/28 0:12
 * 4
 */
public class NetworkMessageUtil {
    public static void sendMessageToPlayer(JavaPlugin plugin, String name, String msg) {
        sendMessageToPlayer(plugin, name, new ChatComponentBuilder(msg).create());
    }

    public static void sendMessageToPlayer(JavaPlugin plugin, String name, BaseComponent[] msg) {
        PacketAlert packet = new PacketAlert(msg, plugin.getName(), name);
        NetworkManager.getInstance()
                .getPidgin()
                .sendPacket(packet);
    }

    public static void sendMessageAlert(JavaPlugin plugin, String msg) {
        sendMessageAlert(plugin, new ChatComponentBuilder(msg).create());
    }

    public static void sendMessageAlert(JavaPlugin plugin, BaseComponent[] msg) {
        PacketAlert packet = new PacketAlert(msg, plugin.getName(), null);
        NetworkManager.getInstance()
                .getPidgin()
                .sendPacket(packet);
    }
}
