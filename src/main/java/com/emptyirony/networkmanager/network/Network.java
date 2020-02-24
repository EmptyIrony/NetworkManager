package com.emptyirony.networkmanager.network;

import com.emptyirony.networkmanager.NetworkManager;
import com.emptyirony.networkmanager.network.command.NetworkCommand;
import com.emptyirony.networkmanager.network.heartbeat.HeartBeatRunnable;
import com.emptyirony.networkmanager.network.listener.NetworkListener;
import com.emptyirony.networkmanager.network.packet.PacketHeartBeat;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import strafe.games.core.Stone;

import static java.lang.Thread.sleep;

/**
 * 2 * @Author: EmptyIrony
 * 3 * @Date: 2020/2/24 0:42
 * 4
 */
public class Network {
    public static String SERVER_NAME;

    @SneakyThrows
    public Network() {
        NetworkManager plugin = NetworkManager.getInstance();
        plugin.saveDefaultConfig();
        SERVER_NAME = plugin.getConfig().getString("server_name");
        if (SERVER_NAME.equalsIgnoreCase("noname")) {
            System.out.println("你必须改变服务器名字才能使用本插件!");
            sleep(1000 * 5);
            Bukkit.shutdown();
        }


        System.out.println("已注册NetWork数据包！");

        plugin.getPidgin().registerListener(new NetworkListener(plugin));
        System.out.println("已注册NetWork数据包监听器！");
        plugin.getPidgin().registerPacket(PacketHeartBeat.class);

        Stone.get().getHoncho().registerCommand(new NetworkCommand());
        System.out.println("已注册NetWork指令！");

        new HeartBeatRunnable(SERVER_NAME).runTaskTimerAsynchronously(plugin, 20, 20);
        System.out.println("心跳活动开始");
    }
}
