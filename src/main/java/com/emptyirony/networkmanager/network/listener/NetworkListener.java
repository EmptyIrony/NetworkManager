package com.emptyirony.networkmanager.network.listener;

import cn.panshi.spigot.util.CC;
import com.emptyirony.networkmanager.NetworkManager;
import com.emptyirony.networkmanager.data.PlayerData;
import com.emptyirony.networkmanager.network.packet.PacketHeartBeat;
import com.emptyirony.networkmanager.network.server.ServerInfo;
import com.minexd.pidgin.packet.handler.IncomingPacketHandler;
import com.minexd.pidgin.packet.listener.PacketListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

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
                PlayerData data = new PlayerData(player.getUniqueId());
                if (data.isNotify() && player.hasPermission("panshi.mod")) {
                    player.sendMessage(CC.translate("&e[员工频道] &c" + "CONSOLE" + ": &f" + packet.getName() + " 服务器上线了" + "&e(" + "ServerOnline" + ")"));
                }
            }
            return;
        }

        new ServerInfo(packet.getName(), packet.getOnlinePlayers(), packet.getMotd(), packet.getTime());
    }


}
