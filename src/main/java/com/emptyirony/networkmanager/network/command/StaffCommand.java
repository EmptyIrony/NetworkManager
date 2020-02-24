package com.emptyirony.networkmanager.network.command;

import cn.panshi.spigot.util.CC;
import com.emptyirony.networkmanager.data.PlayerData;
import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.entity.Player;

/**
 * 2 * @Author: EmptyIrony
 * 3 * @Date: 2020/2/25 1:16
 * 4
 */
@CommandMeta(label = "staffmode", permission = "panshi.mod", async = true)
public class StaffCommand {
    public void execute(Player player) {
        PlayerData data = new PlayerData(player.getUniqueId());
        if (data.isNotify()) {
            data.setNotify(false);
            player.sendMessage(CC.translate("&6员工详细信息已&c关闭"));
        } else {
            data.setNotify(true);
            player.sendMessage(CC.translate("&6员工详细信息已&a开启"));
        }
        data.save(false);
    }
}