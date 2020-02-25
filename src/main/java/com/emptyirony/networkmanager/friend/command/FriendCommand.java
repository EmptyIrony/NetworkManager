package com.emptyirony.networkmanager.friend.command;

import cn.panshi.spigot.util.CC;
import com.emptyirony.networkmanager.NetworkManager;
import com.emptyirony.networkmanager.data.PlayerData;
import com.emptyirony.networkmanager.network.packet.PacketFriendRequest;
import com.emptyirony.networkmanager.network.server.ServerInfo;
import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.entity.Player;
import strafe.games.core.profile.Profile;

/**
 * 2 * @Author: EmptyIrony
 * 3 * @Date: 2020/2/25 15:58
 * 4
 */
@CommandMeta(label = {"friend", "f"}, async = true)
public class FriendCommand {
    public void execute(Player player) {
        sendHelp(player);
    }

    public void execute(Player player, String msg) {
        boolean online = ServerInfo.isPlayerOnline(msg);
        if (!online) {
            player.sendMessage(CC.translate("&c那名玩家不在线！"));
            return;
        }
        Profile profile = Profile.getByUsername(msg);
        if (profile == null) {
            player.sendMessage(CC.translate("&c无法读取目标玩家的档案"));
            return;
        }
        PlayerData data = PlayerData.getByUuid(profile.getUuid());
        if (data == null) {
            player.sendMessage(CC.translate("&c无法读取目标玩家的档案"));
            return;
        }
        if (data.getIgnored().contains(player.getName())) {
            player.sendMessage(CC.translate("&c那名玩家拉黑了你"));
            return;
        }
        PacketFriendRequest packet = new PacketFriendRequest(player.getName(), msg, false);
        NetworkManager.getInstance().getPidgin().sendPacket(packet);
        player.sendMessage(CC.translate("&9&m---------------------------------------------"));
        player.sendMessage(CC.translate("&e成功发送好友请求，那名玩家需要在5分钟之内同意"));
        player.sendMessage(CC.translate("&9&m---------------------------------------------"));
    }


    private void sendHelp(Player player) {
        player.sendMessage(CC.translate("&9&m---------------------------------------------"));
        player.sendMessage(CC.translate("&e好友指令:"));
        player.sendMessage(CC.translate("&e/friend help &7- &b显示此帮助"));
        player.sendMessage(CC.translate("&e/friend add <玩家> &7- &b添加玩家为好友"));
        player.sendMessage(CC.translate("&e/friend remove <玩家> &7- &b从好友列表中移除玩家"));
        player.sendMessage(CC.translate("&e/friend accept <玩家> &7- &b接受好友请求"));
        player.sendMessage(CC.translate("&e/friend deny <玩家> &7- &b拒绝好友请求"));
        player.sendMessage(CC.translate("&e/friend list &7- &b列出你的好友"));
        player.sendMessage(CC.translate("&7&m----------------------"));
    }
}
