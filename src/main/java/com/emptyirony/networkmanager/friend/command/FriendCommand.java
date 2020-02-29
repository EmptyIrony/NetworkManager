package com.emptyirony.networkmanager.friend.command;

import com.emptyirony.networkmanager.NetworkManager;
import com.emptyirony.networkmanager.data.PlayerData;
import com.emptyirony.networkmanager.friend.data.FriendData;
import com.emptyirony.networkmanager.network.server.ServerInfo;
import com.emptyirony.networkmanager.packet.PacketFriendRequest;
import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.entity.Player;
import strafe.games.core.profile.Profile;
import strafe.games.core.util.BungeeUtil;
import strafe.games.core.util.CC;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

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
        if (msg.equalsIgnoreCase("list")) {
            PlayerData data = PlayerData.getByUuid(player.getUniqueId());
            List<UUID> f = data.getFriends().stream().map(UUID::fromString).collect(Collectors.toList());
            if (f.isEmpty()) {
                player.sendMessage(CC.translate("&c你没有好友，使用/friend指令去结交更多朋友吧！"));
                return;
            }
            checkFriendList(player, f, 0);
        } else {
            requestFriend(player, msg);
        }
    }

    public void execute(Player player, String option, String target) {
        if (option.equalsIgnoreCase("accept")) {
            FriendData friendData = FriendData.getDataByTargetName(player.getName());
            if (friendData == null) {
                player.sendMessage(CC.translate("&c你没有收到那名玩家发送的好友请求，或者请求已过期"));
                return;
            }
            PlayerData data = PlayerData.getByUuid(player.getUniqueId());
            Profile profile = Profile.getByUsername(target);
            PlayerData targetData = PlayerData.getByUuid(profile.getUuid());

            data.getFriends().add(profile.getUuid().toString());
            targetData.getFriends().add(player.getUniqueId().toString());

            data.save(false);
            targetData.save(false);
            FriendData.getCache().remove(friendData);
            player.sendMessage(CC.translate("&a你现在与 " + target + " &a成为了好友"));
            if (ServerInfo.isPlayerOnline(target)) {
                BungeeUtil.sendMessage(player, target, CC.translate("&a你现在与 " + player.getName() + " &a成为了好友"));
            }
        } else {
            if (option.equalsIgnoreCase("add")) {
                this.requestFriend(player, target);
            } else if (option.equalsIgnoreCase("deny")) {
                FriendData friendData = FriendData.getDataByTargetName(player.getName());
                if (friendData == null) {
                    player.sendMessage(CC.translate("&c你没有收到那名玩家发送的好友请求，或者请求已过期"));
                    return;
                }
                PlayerData data = PlayerData.getByUuid(player.getUniqueId());
                Profile profile = Profile.getByUsername(target);
                PlayerData targetData = PlayerData.getByUuid(profile.getUuid());

                data.getFriends().add(profile.getUuid().toString());
                targetData.getFriends().add(player.getUniqueId().toString());
                FriendData.getCache().remove(friendData);

                player.sendMessage(CC.translate("&c你拒绝了&6" + target + "&c的好友请求"));
                boolean online = ServerInfo.isPlayerOnline(target);
                if (online) {
                    BungeeUtil.sendMessage(player, target, CC.translate("&6" + player.getName() + "&e 拒绝了您的好友请求"));
                }
            } else if (option.equalsIgnoreCase("remove")) {
                PlayerData data = PlayerData.getByUuid(player.getUniqueId());
                Profile profile = Profile.getByUsername(target);
                if (profile == null) {
                    player.sendMessage(CC.translate("&c你和他还不是好友"));
                    return;
                }

                if (!data.getFriends().contains(profile.getUuid().toString())) {
                    player.sendMessage(CC.translate("&c你和他还不是好友"));
                    return;
                }
                PlayerData targetData = PlayerData.getByUuid(profile.getUuid());
                data.getFriends().remove(profile.getUuid().toString());
                targetData.getFriends().remove(profile.getUuid().toString());

                data.save(false);
                targetData.save(false);
                player.sendMessage(CC.translate("&9&m---------------------------------------------"));
                player.sendMessage(CC.translate("&e你从好友列表删除了 &a" + profile.getName()));
                player.sendMessage(CC.translate("&9&m---------------------------------------------"));

                if (ServerInfo.isPlayerOnline(profile.getName())) {
                    BungeeUtil.sendMessage(player, profile.getName(), CC.translate("&9&m---------------------------------------------"));
                    BungeeUtil.sendMessage(player, profile.getName(), CC.translate("&a" + player.getName() + "&e 从好友列表删除了你"));
                    BungeeUtil.sendMessage(player, profile.getName(), CC.translate("&9&m---------------------------------------------"));
                }
            } else if (option.equalsIgnoreCase("list")) {
                int page;
                try {
                    page = Integer.parseInt(target);
                    if (page < 0) {
                        throw new Exception();
                    }
                } catch (Exception ignored) {
                    player.sendMessage(CC.translate("&c页数必须是正整数！"));
                    return;
                }
                PlayerData data = PlayerData.getByUuid(player.getUniqueId());
                if (data.getFriends().size() / 8 > page - 1) {
                    player.sendMessage(CC.translate("&c你输入的数字超过你的好友列表页数！"));
                    return;
                }

                checkFriendList(player, data.getFriends().stream().map(UUID::fromString).collect(Collectors.toList()), page - 1);
            }
        }
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
        player.sendMessage(CC.translate("&9&m---------------------------------------------"));
    }

    private void requestFriend(Player player, String target) {
        boolean online = ServerInfo.isPlayerOnline(target);
        if (!online) {
            player.sendMessage(CC.translate("&c那名玩家不在线！"));
            return;
        }
        PlayerData data = PlayerData.getByUuid(player.getUniqueId());
        Profile profile = Profile.getByUsername(target);
        if (profile == null) {
            player.sendMessage(CC.translate("&c无法读取目标玩家的档案"));
            return;
        }
        if (player.getName().equalsIgnoreCase(target)) {
            player.sendMessage(CC.translate("&c你无法加你自己为好友！"));
            return;
        }
        PlayerData targetData = PlayerData.getByUuid(profile.getUuid());
        if (targetData == null) {
            player.sendMessage(CC.translate("&c无法读取目标玩家的档案"));
            return;
        }
        if (targetData.getIgnored().contains(player.getName())) {
            player.sendMessage(CC.translate("&c那名玩家拉黑了你"));
            return;
        }
        if (data.getFriends().contains(profile.getUuid().toString())) {
            player.sendMessage(CC.translate("&c你和" + target + "已经是好友了"));
            return;
        }

        PacketFriendRequest packet = new PacketFriendRequest(player.getName(), target, false);
        NetworkManager.getInstance().getPidgin().sendPacket(packet);
        System.out.println("sent a packet");
    }

    private void checkFriendList(Player player, List<UUID> friendsList, int page) {
        Map<String, String> friends = new HashMap<>();
        for (int i = page * 8; i < 8 + page * 8; i++) {
            if (friendsList.size() <= i) {
                break;
            }
            String name = Profile.getByUuid(friendsList.get(i)).getName();
            String server = ServerInfo.getPlayerServer(name);
            if (server == null) {
                friends.put(name, "离线");
            } else {
                String[] split = server.split("_");
                String s = split[0];
                String gameName = split[1];
                friends.put(name, s + "_" + gameName);
            }
        }

        player.sendMessage(CC.translate("&9&m---------------------------------------------"));
        player.sendMessage(CC.translate(" &6当前第" + (page + 1) + "页，总共" + ((friendsList.size() / 8) + 1) + "页"));
        friends.forEach((name, server) -> {
            String[] split = server.split("_");
            player.sendMessage(CC.translate("&e" + name + "&e" + (server.equals("离线") ? "&c当前已离线" : split[0].equalsIgnoreCase("hub") ? "正在" + split[1] + "大厅" : "在" + split[1] + "游戏中")));
        });
        player.sendMessage(CC.translate(" "));
        player.sendMessage(CC.translate("&9&m---------------------------------------------"));
    }
}
