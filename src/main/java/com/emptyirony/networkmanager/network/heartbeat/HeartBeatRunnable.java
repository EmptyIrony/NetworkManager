package com.emptyirony.networkmanager.network.heartbeat;

import cn.panshi.spigot.util.CC;
import com.emptyirony.networkmanager.NetworkManager;
import com.emptyirony.networkmanager.data.PlayerData;
import com.emptyirony.networkmanager.network.packet.PacketHeartBeat;
import com.emptyirony.networkmanager.network.server.ServerInfo;
import lombok.AllArgsConstructor;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

/**
 * 2 * @Author: EmptyIrony
 * 3 * @Date: 2020/2/24 0:54
 * 4
 */
@AllArgsConstructor
public class HeartBeatRunnable extends BukkitRunnable {
    private String name;

    @Override
    public void run() {
        List<String> players = new ArrayList<>();

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            players.add(onlinePlayer.getName());
        }
        PacketHeartBeat packet = new PacketHeartBeat(name, players, MinecraftServer.getServer().getMotd());
        NetworkManager.getInstance().getPidgin().sendPacket(packet);

        List<String> dead = new ArrayList<>();
        ServerInfo.getCache().forEach((s, serverInfo) -> {
            if (System.currentTimeMillis() - serverInfo.getLastHeartBeat() > 5 * 1000) {
                dead.add(s);
            }
        });

        dead.forEach(s -> {
            ServerInfo.getCache().remove(s);
            for (Player player : Bukkit.getOnlinePlayers()) {
                PlayerData data = new PlayerData(player.getUniqueId()).load();
                if (data.getStaffOption().isNotify() && player.hasPermission("panshi.mod")) {
                    player.sendMessage(CC.translate("&e[员工频道] &c" + "CONSOLE" + ": &f" + s + " 服务器离线了" + "&e(" + "ServerOffline" + ")"));
                }
            }
        });
    }
}
