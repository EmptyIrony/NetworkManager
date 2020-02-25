package com.emptyirony.networkmanager.chat.command;

import cn.panshi.spigot.util.CC;
import com.emptyirony.networkmanager.data.PlayerData;
import com.emptyirony.networkmanager.network.server.ServerInfo;
import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.entity.Player;
import strafe.games.core.profile.Profile;
import strafe.games.core.util.BungeeUtil;

/**
 * 2 * @Author: EmptyIrony
 * 3 * @Date: 2020/2/24 10:02
 * 4
 */
@CommandMeta(label = {"msg", "w", "t", "tell"}, async = true)
public class MsgCommand {
    public void execute(Player player, String target, String msg) {
        PlayerData data = new PlayerData(Profile.getByUsername(target).getUuid()).load();
        boolean online = ServerInfo.isPlayerOnline(target);
        if (!online) {
            player.sendMessage(CC.translate("&c那名玩家不在线！"));
            return;
        }
        if (data.getIgnored().contains(player.getName().toLowerCase())) {
            player.sendMessage(CC.translate("&c那名玩家把你拉黑了"));
            return;
        }
        player.sendMessage(CC.translate("&d " + player.getDisplayName() + "➦&7: " + msg));
        BungeeUtil.sendMessage(player, target, "&d" + player.getDisplayName() + "➥&7: " + msg);
    }
}
