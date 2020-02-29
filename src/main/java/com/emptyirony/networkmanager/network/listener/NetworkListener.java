package com.emptyirony.networkmanager.network.listener;

import com.emptyirony.networkmanager.NetworkManager;
import com.emptyirony.networkmanager.data.PlayerData;
import com.emptyirony.networkmanager.friend.data.FriendData;
import com.emptyirony.networkmanager.network.server.ServerInfo;
import com.emptyirony.networkmanager.packet.PacketFriendRequest;
import com.emptyirony.networkmanager.packet.PacketHeartBeat;
import com.emptyirony.networkmanager.packet.PacketStaffMsg;
import com.emptyirony.networkmanager.packet.PacketStaffSwitchServer;
import com.minexd.pidgin.packet.handler.IncomingPacketHandler;
import com.minexd.pidgin.packet.listener.PacketListener;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import strafe.games.core.util.BungeeUtil;
import strafe.games.core.util.CC;
import strafe.games.core.util.ChatComponentBuilder;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 2 * @Author: EmptyIrony
 * 3 * @Date: 2020/2/24 0:43
 * 4
 */
public class NetworkListener implements PacketListener {
    private NetworkManager plugin;

    public NetworkListener(NetworkManager plugin) {
        this.plugin = plugin;
    }

    @IncomingPacketHandler
    public void onHeartBear(PacketHeartBeat packet) {
        if (ServerInfo.getCache().get(packet.getName()) == null) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                PlayerData data = new PlayerData(player.getUniqueId()).load();
                if (data.getStaffOption().isNotify() && player.hasPermission("panshi.mod")) {
                    player.sendMessage(CC.translate("&e[员工频道] &c" + "CONSOLE" + ": &f" + packet.getName() + " 服务器上线了" + "&e(" + "ServerOnline" + ")"));
                }
            }
        }

        new ServerInfo(packet.getName(), packet.getOnlinePlayers(), packet.getMotd(), packet.getTime());
    }

    @IncomingPacketHandler
    public void onStaffChat(PacketStaffMsg packet) {
        Bukkit.getOnlinePlayers().stream().filter(player -> {
            PlayerData data = PlayerData.getByUuid(player.getUniqueId());
            if (packet.getType() == 0) {
                return data.getStaffOption().isNotify() && player.hasPermission("panshi.mod");
            } else if (packet.getType() == 1) {
                return data.getStaffOption().isNotify() && player.hasPermission("panshi.admin");
            }
            return false;
        }).collect(Collectors.toList()).forEach(player -> {
            player.sendMessage(CC.translate("&d[" + (packet.getType() == 0 ? "员工" : "管理") + "频道] &c" + packet.getName() + ": &f" + packet.getMsg()));
        });
    }

    @IncomingPacketHandler
    public void onSwitchServer(PacketStaffSwitchServer packet) {
        StringBuilder builder = new StringBuilder();
        builder.append(CC.translate("&e[员工频道] &cCONSOLE: &3"));
        if (packet.getType() == 0) {
            builder.append(CC.translate(packet.getPlayer() + "&e 从 &3" + packet.getFrom() + "&e 离线了&e(员工追踪)"));
        } else if (packet.getType() == 1) {
            builder.append(CC.translate(packet.getPlayer() + "&e 从 &3" + packet.getTo() + "&e 上线了&e(员工追踪)"));
        } else if (packet.getType() == 2) {
            builder.append(CC.translate(packet.getPlayer() + "&e 从 &3" + packet.getFrom() + "&e 更变到&3 " + packet.getTo() + " &e(员工追踪)"));
        }
        List<? extends Player> players = Bukkit.getOnlinePlayers().stream().filter(player -> player.hasPermission("panshi.modd") && PlayerData.getByUuid(player.getUniqueId()).getStaffOption().isNotify()).collect(Collectors.toList());
        players.forEach(player -> {
            player.sendMessage(builder.toString());
        });
    }

    @IncomingPacketHandler
    public void onFriendRequest(PacketFriendRequest packet) {
        //todo a packet
        Player player = Bukkit.getPlayer(packet.getTarget());
        boolean allow = new FriendData(packet.getName(), packet.getTarget(), packet.getTime()).add();

        if (player == null) {
            return;
        }
        if (!allow) {
            BungeeUtil.sendMessage(player, packet.getName(), CC.translate("&c你已经向 " + packet.getTarget() + " 发送过请求了，请耐心等待"));
            return;
        }
        BungeeUtil.sendMessage(player, packet.getName(), CC.translate("&9&m---------------------------------------------"));
        BungeeUtil.sendMessage(player, packet.getName(), CC.translate("&e成功发送好友请求，那名玩家需要在5分钟之内同意"));
        BungeeUtil.sendMessage(player, packet.getName(), CC.translate("&9&m---------------------------------------------"));

        ChatComponentBuilder accept = new ChatComponentBuilder(CC.translate("&a&l[同意] "))
                .setCurrentHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(CC.translate("&a点击同意 " + packet.getName() + " &a的好友请求")).create()))
                .setCurrentClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/friend accept " + packet.getName()));

        ChatComponentBuilder deny = new ChatComponentBuilder(CC.translate("&c&l[拒绝] "))
                .setCurrentHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(CC.translate("&c点击拒绝 " + packet.getName() + " &a的好友请求")).create()))
                .setCurrentClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/friend deny " + packet.getName()));

        ChatComponentBuilder ignore = new ChatComponentBuilder(CC.translate("&7&l[忽略]"))
                .setCurrentHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(CC.translate("&c点击屏蔽 " + packet.getName() + " &a的好友请求")).create()))
                .setCurrentClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/friend deny " + packet.getName()));

        BaseComponent[] clickMsg = new ChatComponentBuilder("").append(accept.create()).append(deny.create()).append(ignore.create()).create();
        player.sendMessage(CC.translate("&9&m---------------------------------------------"));
        player.sendMessage(CC.translate("&e" + packet.getName() + " 申请加您为好友"));
        player.spigot().sendMessage(clickMsg);
        player.sendMessage(CC.translate("&9&m---------------------------------------------"));
    }
}
