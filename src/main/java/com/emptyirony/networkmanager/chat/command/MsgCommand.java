package com.emptyirony.networkmanager.chat.command;

import com.emptyirony.networkmanager.data.PlayerData;
import com.emptyirony.networkmanager.network.server.ServerInfo;
import com.qrakn.honcho.command.CommandMeta;
import lombok.Getter;
import org.bukkit.entity.Player;
import strafe.games.core.profile.Profile;
import strafe.games.core.util.BungeeUtil;
import strafe.games.core.util.CC;
import strafe.games.core.util.Cooldown;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 2 * @Author: EmptyIrony
 * 3 * @Date: 2020/2/24 10:02
 * 4
 */
@CommandMeta(label = {"msg", "w", "t", "tell"}, async = true)
public class MsgCommand {
    @Getter
    private static Map<UUID, Cooldown> cooldownMap = new HashMap<>();

    public void execute(Player player, String target, String msg) {
        boolean online = ServerInfo.isPlayerOnline(target);
        if (!online) {
            player.sendMessage(CC.translate("&c那名玩家不在线！"));
            return;
        }
        Profile profile = Profile.getByUsername(target);
        PlayerData data = PlayerData.getByUuid(profile.getUuid());
        if (data.getIgnored().contains(player.getName().toLowerCase())) {
            player.sendMessage(CC.translate("&c那名玩家把你拉黑了"));
            return;
        }
        if (cooldownMap.containsKey(player.getUniqueId()) && !cooldownMap.get(player.getUniqueId()).hasExpired()) {
            player.sendMessage(CC.translate("&c请不要使用私聊刷屏"));
            return;
        }

        if (data.getPlayerOption().isStream() && !player.hasPermission("panshi.admin")) {
            player.sendMessage(CC.translate("&c那名玩家开启了直播模式，请使用其他方式私聊告知"));
            BungeeUtil.sendMessage(player, target, CC.translate("&d玩家 &b" + player.getName() + "&d 私聊了你，但是为了防止直播事故，我们已将其屏蔽"));
            return;
        }
        data.setLastMsg(player.getName());
        cooldownMap.put(player.getUniqueId(), new Cooldown(1000));
        data.isFriend(player.getName(), target, isFriend -> {
            if (isFriend) {
                player.sendMessage(CC.translate("&d" + target + "➦&7: " + msg));
                BungeeUtil.sendMessage(player, target, CC.translate("&d" + player.getDisplayName() + "➥&7: " + msg));
            } else {
                player.sendMessage(CC.translate("&c你需要先添加对方为好友才可以私聊！"));
            }
        });
        data.save(false);
    }
}
