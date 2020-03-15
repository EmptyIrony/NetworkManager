package com.emptyirony.networkmanager.network;

import com.emptyirony.networkmanager.NetworkManager;
import com.emptyirony.networkmanager.friend.Friend;
import com.emptyirony.networkmanager.network.command.*;
import com.emptyirony.networkmanager.network.heartbeat.HeartBeatRunnable;
import com.emptyirony.networkmanager.network.listener.CrashListener;
import com.emptyirony.networkmanager.network.listener.NetworkListener;
import com.emptyirony.networkmanager.network.listener.PlayerListener;
import com.emptyirony.networkmanager.packet.*;
import lombok.Data;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.inventivetalent.packetlistener.PacketListenerAPI;
import strafe.games.core.Stone;
import strafe.games.core.util.RefUtil;

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


        System.out.println("已注册NetWork数据包！");
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), plugin);
        plugin.getPidgin().registerListener(new NetworkListener(plugin));
        System.out.println("已注册NetWork数据包监听器！");
        plugin.getPidgin().registerPacket(PacketHeartBeat.class);
        plugin.getPidgin().registerPacket(PacketStaffMsg.class);
        plugin.getPidgin().registerPacket(PacketFriendRequest.class);
        plugin.getPidgin().registerPacket(PacketStaffSwitchServer.class);
        plugin.getPidgin().registerPacket(PacketServerShutdown.class);
        plugin.getPidgin().registerPacket(PacketPlayerJoinOrQuit.class);

        Stone.get().getHoncho().registerCommand(new NetworkCommand());
        Stone.get().getHoncho().registerCommand(new StaffCommand());
        Stone.get().getHoncho().registerCommand(new StaffChat());
        Stone.get().getHoncho().registerCommand(new NickCommand());
        if (RefUtil.getVersion().startsWith("v1_8")) {
            Stone.get().getHoncho().registerCommand(new CrashCommand());
            PacketListenerAPI.addPacketHandler(new CrashListener());
        }

        Stone.get().getHoncho().registerCommand(new ShutdownCommand());
        System.out.println("已注册NetWork指令！");

        HeartBeatRunnable heartBeatRunnable = new HeartBeatRunnable(SERVER_NAME);
        Thread thread = new Thread(heartBeatRunnable);
        thread.start();
        System.out.println("心跳活动开始");

        System.out.println("好友系统开始初始化...");
        this.friend = new Friend();
        System.out.println("好友系统初始化完成！");
    }
}
