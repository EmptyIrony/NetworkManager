package com.emptyirony.networkmanager.util;

import com.emptyirony.networkmanager.NetworkManager;
import com.emptyirony.networkmanager.packet.PacketAlert;
import net.md_5.bungee.api.chat.BaseComponent;
import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;

/**
 * 2 * @Author: EmptyIrony
 * 3 * @Date: 2020/3/28 0:12
 * 4
 */
public class NetworkMessageUtil {
    public static void sendMessageToPlayer(String name, String msg) {
        sendMessageToPlayer(name, IChatBaseComponent.ChatSerializer.a(new ChatComponentText(msg).f()));
    }

    public static void sendMessageToPlayer(String name, BaseComponent[] msg) {
        PacketAlert packet = new PacketAlert(msg, name, null);
        NetworkManager.getInstance()
                .getPidgin()
                .sendPacket(packet);
    }

    public static void sendMessageAlert(String msg) {
        sendMessageAlert(IChatBaseComponent.ChatSerializer.a(new ChatComponentText(msg).f()));
    }

    public static void sendMessageAlert(BaseComponent[] msg) {
        PacketAlert packet = new PacketAlert(msg, null, null);
        NetworkManager.getInstance()
                .getPidgin()
                .sendPacket(packet);
    }

    public static void sendMessageWithPermission(String permission, String msg) {
        sendMessageWithPermission(permission, IChatBaseComponent.ChatSerializer.a(new ChatComponentText(msg).f()));
    }

    public static void sendMessageWithPermission(String permission, BaseComponent[] msg) {
        PacketAlert packet = new PacketAlert(msg, null, permission);
        NetworkManager.getInstance()
                .getPidgin()
                .sendPacket(packet);
    }
}
