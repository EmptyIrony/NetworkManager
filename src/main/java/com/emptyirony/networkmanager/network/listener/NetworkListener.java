package com.emptyirony.networkmanager.network.listener;

import com.emptyirony.networkmanager.NetworkManager;
import com.emptyirony.networkmanager.network.Network;
import com.emptyirony.networkmanager.network.server.ServerInfo;
import com.emptyirony.networkmanager.packet.PacketAlert;
import com.emptyirony.networkmanager.packet.PacketHeartBeat;
import com.emptyirony.networkmanager.packet.PacketServerShutdown;
import com.emptyirony.networkmanager.packet.PacketStaffSwitchServer;
import com.minexd.pidgin.packet.handler.IncomingPacketHandler;
import com.minexd.pidgin.packet.listener.PacketListener;
import me.allen.chen.user.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import strafe.games.core.util.CC;

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
                User user = User.getByUUID(player.getUniqueId());
                if (user.getStaffSettings().isStaffChatNotify() && player.hasPermission("panshi.mod")) {
                    player.sendMessage(CC.translate("&e[员工频道] &c" + "CONSOLE" + ": &f" + packet.getName() + " 服务器上线了" + "&e(" + "ServerOnline" + ")"));
                }
            }
        }

        new ServerInfo(packet.getName(), packet.getOnlinePlayers(), packet.getMotd(), packet.getTime(), packet.getTps());
    }

    @IncomingPacketHandler
    public void onAlert(PacketAlert packet) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.spigot().sendMessage(packet.getComponent());
        }
    }

    @IncomingPacketHandler
    public void onSwitchServer(PacketStaffSwitchServer packet) {
        StringBuilder builder = new StringBuilder();
        builder.append(CC.translate("&e[员工频道] &cCONSOLE: &3"));
        if (packet.getType() == 0) {
            builder.append(CC.translate(packet.getPlayer() + "&e 从 &3" + packet.getFrom() + "&e 离线了&e(员工追踪)"));
        } else if (packet.getType() == 1) {
            builder.append(CC.translate(packet.getPlayer() + "&e 从 &3" + packet.getTo() + "&e 上线了&e(员工追踪)"));
        }
        Bukkit.getOnlinePlayers().stream().filter(player -> player.hasPermission("panshi.mod") && User.getByUUID(player.getUniqueId()).getStaffSettings().isStaffChatNotify())
                .forEach(player -> player.sendMessage(builder.toString()));
    }

    @IncomingPacketHandler
    public void onShutdownRequest(PacketServerShutdown packet) {
        if (Network.SERVER_NAME.equalsIgnoreCase(packet.getServer())) {
            if (!NetworkManager.getInstance().getNetwork().isShutDowning()) {
                NetworkManager.getInstance().getNetwork().setShutDowning(true);
                Bukkit.broadcastMessage(CC.translate("&6&m---------------------------------------------"));
                Bukkit.broadcastMessage(CC.translate("&c本服将在 &e" + packet.getDelay() + "&c 秒后重启"));
                Bukkit.broadcastMessage(CC.translate("&c执行管理: &e" + packet.getPlayer()));
                Bukkit.broadcastMessage(CC.translate("&c执行原因: &e" + packet.getReason()));
                Bukkit.broadcastMessage(CC.translate("&6&m---------------------------------------------"));
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        Bukkit.shutdown();
                    }
                }.runTaskLater(NetworkManager.getInstance(), 20 * packet.getDelay());
            }
        }
    }
}
