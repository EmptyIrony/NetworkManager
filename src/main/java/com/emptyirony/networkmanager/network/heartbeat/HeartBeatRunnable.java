package com.emptyirony.networkmanager.network.heartbeat;

import com.emptyirony.networkmanager.NetworkManager;
import com.emptyirony.networkmanager.network.server.ServerInfo;
import com.emptyirony.networkmanager.packet.PacketHeartBeat;
import com.emptyirony.networkmanager.util.CC;
import com.emptyirony.networkmanager.util.ReflectUtils;
import lombok.SneakyThrows;
import me.allen.chen.user.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

/**
 * 2 * @Author: EmptyIrony
 * 3 * @Date: 2020/2/24 0:54
 * 4
 */
public class HeartBeatRunnable implements Runnable {
    private String name;
    private String motd;
    private Field tpsField;
    private Object minecraftServer;
    private Method getMotd;
    private DecimalFormat df = new DecimalFormat("######0.00");

    @SneakyThrows
    public HeartBeatRunnable(String name) {
        this.name = name;
        Class<?> clazz = Class.forName("net.minecraft.server." + ReflectUtils.getVersion() + ".MinecraftServer");
        Method getServer = clazz.getMethod("getServer");
        Object server = getServer.invoke(null);
        this.getMotd = server.getClass().getMethod("getMotd");
        String motd = (String) getMotd.invoke(server);
        this.motd = motd;
        this.minecraftServer = server;

        Class<?> serverClazz = Class.forName("net.minecraft.server." + ReflectUtils.getVersion() + ".MinecraftServer");
        Method server1 = serverClazz.getMethod("getServer");
        this.tpsField = server1.invoke(null).getClass().getField("recentTps");

    }


    @SneakyThrows
    @Override
    public void run() {
        while (true) {
            try {
                List<String> players = new ArrayList<>();

                this.motd = (String) this.getMotd.invoke(minecraftServer);

                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    players.add(onlinePlayer.getName());
                }
                double[] tps = (double[]) tpsField.get(minecraftServer);

                PacketHeartBeat packet = new PacketHeartBeat(name, players, motd == null ? "null" : motd, Double.parseDouble(df.format(tps[0])));
                NetworkManager.getInstance().getPidgin().sendPacket(packet);

                List<String> dead = new ArrayList<>();
                ServerInfo.getCache().forEach((s, serverInfo) -> {
                    if (System.currentTimeMillis() - serverInfo.getLastHeartBeat() > 5_000L) {
                        dead.add(s);
                    }
                });

                dead.forEach(s -> {
                    ServerInfo.getCache().remove(s);
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        User user = User.getByUUID(player.getUniqueId());
                        if (user.getStaffSettings().isStaffChatNotify() && player.hasPermission("panshi.mod")) {
                            player.sendMessage(CC.translate("&e[员工频道] &c" + "CONSOLE" + ": &f" + s + " 服务器离线了" + "&e(" + "ServerOffline" + ")"));
                        }
                    }
                });
            } catch (Exception ignored) {
            } finally {
                sleep(1000);
            }
        }
    }
}
