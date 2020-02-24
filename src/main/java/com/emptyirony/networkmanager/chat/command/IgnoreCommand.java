package com.emptyirony.networkmanager.chat.command;

import cn.panshi.spigot.util.CC;
import com.emptyirony.networkmanager.data.PlayerData;
import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * 2 * @Author: EmptyIrony
 * 3 * @Date: 2020/2/24 9:50
 * 4
 */
@CommandMeta(label = "ignore", async = true)
public class IgnoreCommand {

    public void execute(Player player, String target) {
        PlayerData data = new PlayerData(player.getUniqueId());

        if (target.equalsIgnoreCase("list")) {
            List<String> ignored = data.getIgnored();
            if (ignored.isEmpty()) {
                player.sendMessage(CC.translate("&c你的黑名单中没有玩家"));
                return;
            }
            player.sendMessage("&7&m------------------------");
            player.sendMessage("&c黑名单列表: ");
            ignored.forEach(name -> {
                player.sendMessage("&7 - " + name);
            });
            player.sendMessage("&7&m------------------------");
            return;
        }

        if (data.getIgnored().contains(target.toLowerCase())) {
            player.sendMessage(CC.translate("&c那名玩家已经在你的黑名单中了"));
            return;
        }

        data.getIgnored().add(target.toLowerCase());
        player.sendMessage(CC.translate("&c已将 &6" + target + "&c 添加到黑名单中"));
    }

    public void execute(Player player, String option, String target) {
        PlayerData data = new PlayerData(player.getUniqueId());

        option = option.toLowerCase();

        switch (option) {
            case "add":
                if (data.getIgnored().contains(target.toLowerCase())) {
                    player.sendMessage(CC.translate("&c那名玩家已经在你的黑名单中了"));
                    return;
                }

                data.getIgnored().add(target.toLowerCase());
                player.sendMessage(CC.translate("&c已将 &6" + target + "&c 添加到黑名单中"));
                data.save(false);
                break;
            case "remove":
                if (!data.getIgnored().contains(target.toLowerCase())) {
                    player.sendMessage(CC.translate("&c那名玩家不在你的黑名单中"));
                    return;
                }

                data.getIgnored().add(target.toLowerCase());
                player.sendMessage(CC.translate("&c已将 &6" + target + "&c 移除黑名单"));
                data.save(false);
                break;
            case "list":
                List<String> ignored = data.getIgnored();
                if (ignored.isEmpty()) {
                    player.sendMessage(CC.translate("&c你的黑名单中没有玩家"));
                    return;
                }
                player.sendMessage("&7&m------------------------");
                player.sendMessage("&c黑名单列表: ");
                ignored.forEach(name -> {
                    player.sendMessage("&7 - " + name);
                });
                player.sendMessage("&7&m------------------------");
                break;
        }
    }

}
