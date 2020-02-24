package com.emptyirony.networkmanager.network.heartbeat;

import com.emptyirony.networkmanager.NetworkManager;
import com.emptyirony.networkmanager.network.packet.PacketHeartBeat;
import com.emptyirony.networkmanager.network.packet.PacketStaffMsg;
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
            PacketStaffMsg packetStaffMsg = new PacketStaffMsg("console", s + " 服务器离线了", "ServerOffline");
            NetworkManager.getInstance().getPidgin().sendPacket(packetStaffMsg);
        });
    }
}
