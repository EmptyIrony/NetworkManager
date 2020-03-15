package com.emptyirony.networkmanager.chat.command;

import com.emptyirony.networkmanager.data.PlayerData;
import com.emptyirony.networkmanager.network.server.ServerInfo;
import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.entity.Player;
import strafe.games.core.profile.Profile;
import strafe.games.core.util.BungeeUtil;
import strafe.games.core.util.CC;

/**
 * 2 * @Author: EmptyIrony
 * 3 * @Date: 2020/2/26 21:22
 * 4
 */

@CommandMeta(label = {"reply", "r"}, async = true)
public class ReplyCommand {
    public void execute(Player player, String msg) {
        PlayerData data = PlayerData.getByUuid(player.getUniqueId());
        if (data.getLastMsg() == null) {
            player.sendMessage(CC.translate("&c你没有玩家可以回复"));
            return;
        }

        String target = data.getLastMsg();
        boolean online = ServerInfo.isPlayerOnline(target);
        if (!online) {
            player.sendMessage(CC.translate("&c对方不在线！"));
            return;
        }
        if (MsgCommand.getCooldownMap().containsKey(player.getUniqueId()) && !MsgCommand.getCooldownMap().get(player.getUniqueId()).hasExpired()) {
            player.sendMessage(CC.translate("&c请不要使用私聊刷屏"));
            return;
        }

        Profile profile = Profile.getByUsername(target);
        PlayerData targetData = PlayerData.getByUuid(profile.getUuid());
        if (targetData.getIgnored().contains(player.getName().toLowerCase())) {
            player.sendMessage(CC.translate("&c那名玩家把你拉黑了"));
            return;
        }

        if (data.getPlayerOption().isStream() && !player.hasPermission("panshi.admin")) {
            player.sendMessage(CC.translate("&c那名玩家开启了直播模式，请使用其他方式私聊告知"));
            BungeeUtil.sendMessage(player, target, CC.translate("&d玩家 &b" + player.getName() + "&d 私聊了你，但是为了防止直播事故，我们已将其屏蔽"));
            return;
        }

        targetData.setLastMsg(player.getName());
        targetData.isFriend(player.getName(), target, isFriend -> {
            if (isFriend) {
                player.sendMessage(CC.translate("&d" + target + "➦&7: " + msg));
                BungeeUtil.sendMessage(player, target, CC.translate("&d" + player.getDisplayName() + "➥&7: " + msg));
            } else {
                player.sendMessage(CC.translate("&c你需要先添加对方为好友才可以私聊！"));
            }
        });
        targetData.save(false);
    }
}
