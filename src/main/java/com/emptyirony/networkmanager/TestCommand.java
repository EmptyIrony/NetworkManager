package com.emptyirony.networkmanager;

import com.emptyirony.networkmanager.bungee.util.CC;
import com.emptyirony.networkmanager.packet.PacketAlert;
import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.entity.Player;
import strafe.games.core.util.ChatComponentBuilder;

/**
 * 2 * @Author: EmptyIrony
 * 3 * @Date: 2020/3/26 15:55
 * 4
 */
@CommandMeta(label = "alert")
public class TestCommand {
    public void execute(Player player, String text) {
        PacketAlert alert = new PacketAlert(new ChatComponentBuilder(CC.translate("&7[&4全服公告&7]" + text)).create(), player.getDisplayName(), null);
        NetworkManager.getInstance().getPidgin().sendPacket(alert);
    }
}
