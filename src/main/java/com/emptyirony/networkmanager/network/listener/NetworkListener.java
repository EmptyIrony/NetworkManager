package com.emptyirony.networkmanager.network.listener;

import cn.panshi.spigot.util.CC;
import com.emptyirony.networkmanager.NetworkManager;
import com.emptyirony.networkmanager.data.PlayerData;
import com.emptyirony.networkmanager.network.packet.PacketHeartBeat;
import com.emptyirony.networkmanager.network.packet.PacketStaffMsg;
import com.emptyirony.networkmanager.network.server.ServerInfo;
import com.minexd.pidgin.packet.handler.IncomingPacketHandler;
import com.minexd.pidgin.packet.listener.PacketListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

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
            player.sendMessage(CC.translate("&e[" + (packet.getType() == 0 ? "员工" : "管理") + "频道] &c" + packet.getName() + ": &f" + packet.getMsg()));
        });
    }


}
