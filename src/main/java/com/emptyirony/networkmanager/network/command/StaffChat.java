package com.emptyirony.networkmanager.network.command;

import com.emptyirony.networkmanager.NetworkManager;
import com.emptyirony.networkmanager.data.PlayerData;
import com.emptyirony.networkmanager.packet.PacketStaffMsg;
import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.entity.Player;
import strafe.games.core.util.CC;

/**
 * 2 * @Author: EmptyIrony
 * 3 * @Date: 2020/2/25 11:09
 * 4
 */
@CommandMeta(label = {"staffchat", "sc"}, permission = "panshi.mod")
public class StaffChat {
    public void execute(Player player) {
        PlayerData data = PlayerData.getByUuid(player.getUniqueId());
        int staffChat = data.getStaffOption().getStaffChat();
        if (staffChat == 1 || staffChat == 2) {
            data.getStaffOption().setStaffChat(0);
            player.sendMessage(CC.translate("&6[&c员工频道&6] &c您退出了员工频道"));
        } else {
            data.getStaffOption().setStaffChat(1);
            player.sendMessage(CC.translate("&6[&c员工频道&6] &c您进入了员工频道"));
        }
        data.save(false);
    }

    public void execute(Player player, String msg) {
        PlayerData data = PlayerData.getByUuid(player.getUniqueId());
        if (msg.equalsIgnoreCase("admin") && player.hasPermission("panshi.admin")) {
            data.getStaffOption().setStaffChat(2);
            player.sendMessage(CC.translate("&6[&c员工频道&6] &c您进入了Admin频道"));
            data.save(false);
            return;
        }
        NetworkManager.getInstance().getPidgin().sendPacket(new PacketStaffMsg(player.getDisplayName(), msg, 0));
    }
}
