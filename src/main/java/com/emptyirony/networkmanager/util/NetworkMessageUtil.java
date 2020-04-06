package com.emptyirony.networkmanager.util;

import com.emptyirony.networkmanager.NetworkManager;
import com.emptyirony.networkmanager.packet.PacketAlert;
import net.md_5.bungee.api.chat.BaseComponent;
import strafe.games.core.util.ChatComponentBuilder;

/**
 * 2 * @Author: EmptyIrony
 * 3 * @Date: 2020/3/28 0:12
 * 4
 */
public class NetworkMessageUtil {
    public static void sendMessageToPlayer(String name, String msg) {
        sendMessageToPlayer(name, new ChatComponentBuilder(msg).create());
    }

    public static void sendMessageToPlayer(String name, BaseComponent[] msg) {
        PacketAlert packet = new PacketAlert(msg, name, null);
        NetworkManager.getInstance()
                .getPidgin()
                .sendPacket(packet);
    }

    public static void sendMessageAlert(String msg) {
        sendMessageAlert(new ChatComponentBuilder(msg).create());
    }

    public static void sendMessageAlert(BaseComponent[] msg) {
        PacketAlert packet = new PacketAlert(msg, null, null);
        NetworkManager.getInstance()
                .getPidgin()
                .sendPacket(packet);
    }

    public static void sendMessageWithPermission(String permission, String msg) {
        sendMessageWithPermission(permission, new ChatComponentBuilder(msg).create());

    }

    public static void sendMessageWithPermission(String permission, BaseComponent[] msg) {
        PacketAlert packet = new PacketAlert(msg, null, permission);
        NetworkManager.getInstance()
                .getPidgin()
                .sendPacket(packet);
    }
}
