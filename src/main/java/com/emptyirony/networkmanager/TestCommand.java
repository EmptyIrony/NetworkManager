package com.emptyirony.networkmanager;

import com.emptyirony.networkmanager.bungee.util.CC;
import com.emptyirony.networkmanager.packet.PacketAlert;
import com.qrakn.honcho.command.CommandMeta;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

/**
 * 2 * @Author: EmptyIrony
 * 3 * @Date: 2020/3/26 15:55
 * 4
 */
@CommandMeta(label = "alert-redis")
public class TestCommand {
    public void execute(Player player, String text) {
        PacketAlert alert = new PacketAlert(new TextComponent(CC.translate(text)), player.getDisplayName());
        NetworkManager.getInstance().getPidgin().sendPacket(alert);
    }
}
