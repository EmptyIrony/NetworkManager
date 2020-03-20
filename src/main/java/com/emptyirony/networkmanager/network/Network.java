package com.emptyirony.networkmanager.network;

import com.emptyirony.networkmanager.NetworkManager;
import com.emptyirony.networkmanager.friend.Friend;
import com.emptyirony.networkmanager.network.command.*;
import com.emptyirony.networkmanager.network.heartbeat.HeartBeatRunnable;
import com.emptyirony.networkmanager.network.listener.CheckListener;
import com.emptyirony.networkmanager.network.listener.NetworkListener;
import com.emptyirony.networkmanager.network.listener.PlayerListener;
import com.emptyirony.networkmanager.packet.*;
import lombok.Data;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.inventivetalent.packetlistener.PacketListenerAPI;
import strafe.games.core.Stone;
import strafe.games.core.util.RefUtil;

import java.util.Arrays;

import static java.lang.Thread.sleep;

/**
 * 2 * @Author: EmptyIrony
 * 3 * @Date: 2020/2/24 0:42
 * 4
 */
@Data
public class Network {
    public static String SERVER_NAME;
    private boolean shutDowning;
    private Friend friend;

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
        NetworkManager.getInstance().setServerType(SERVER_NAME.split("_")[0].equalsIgnoreCase("hub") ? 0 : 1);


        System.out.println("Registered redis packets listener！");
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), plugin);

        plugin.getPidgin().registerListener(new NetworkListener(plugin));
        Arrays.asList(
                PacketHeartBeat.class,
                PacketStaffMsg.class,
                PacketFriendRequest.class,
                PacketStaffSwitchServer.class,
                PacketServerShutdown.class,
                PacketPlayerJoinOrQuit.class
        ).forEach(packet -> {
            plugin.getPidgin().registerPacket(packet);
        });
        System.out.println("Registered redis packets！");

        Arrays.asList(
                new NetworkCommand(),
                new StaffCommand(),
                new StaffChat(),
                new NickCommand()
        ).forEach(command -> {
            Stone.get().getHoncho().registerCommand(command);
        });


        if (RefUtil.getVersion().startsWith("v1_8")) {
            Stone.get().getHoncho().registerCommand(new CheckCommand());
            PacketListenerAPI.addPacketHandler(new CheckListener());
        }

        Stone.get().getHoncho().registerCommand(new ShutdownCommand());
        System.out.println("Registered command！");

        HeartBeatRunnable heartBeatRunnable = new HeartBeatRunnable(SERVER_NAME);
        Thread thread = new Thread(heartBeatRunnable);
        thread.start();
        System.out.println("Heart Beat Started");

        System.out.println("Friend system initialling...");
        this.friend = new Friend();
        System.out.println("Friend system init successfully");
    }
}
