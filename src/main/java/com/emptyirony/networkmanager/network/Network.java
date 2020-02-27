package com.emptyirony.networkmanager.network;

import com.emptyirony.networkmanager.NetworkManager;
import com.emptyirony.networkmanager.friend.Friend;
import com.emptyirony.networkmanager.network.command.NetworkCommand;
import com.emptyirony.networkmanager.network.command.NickCommand;
import com.emptyirony.networkmanager.network.command.StaffChat;
import com.emptyirony.networkmanager.network.command.StaffCommand;
import com.emptyirony.networkmanager.network.heartbeat.HeartBeatRunnable;
import com.emptyirony.networkmanager.network.listener.NetworkListener;
import com.emptyirony.networkmanager.packet.PacketFriendRequest;
import com.emptyirony.networkmanager.packet.PacketHeartBeat;
import com.emptyirony.networkmanager.packet.PacketStaffMsg;
import com.emptyirony.networkmanager.packet.PacketStaffSwitchServer;
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

        plugin.getPidgin().registerListener(new NetworkListener(plugin));
        System.out.println("已注册NetWork数据包监听器！");
        plugin.getPidgin().registerPacket(PacketHeartBeat.class);
        plugin.getPidgin().registerPacket(PacketStaffMsg.class);
        plugin.getPidgin().registerPacket(PacketFriendRequest.class);
        plugin.getPidgin().registerPacket(PacketStaffSwitchServer.class);

        Stone.get().getHoncho().registerCommand(new NetworkCommand());
        Stone.get().getHoncho().registerCommand(new StaffCommand());
        Stone.get().getHoncho().registerCommand(new StaffChat());
        Stone.get().getHoncho().registerCommand(new NickCommand());
        System.out.println("已注册NetWork指令！");

        new HeartBeatRunnable(SERVER_NAME).runTaskTimerAsynchronously(plugin, 20, 20);
        System.out.println("心跳活动开始");

        System.out.println("好友系统开始初始化...");
        this.friend = new Friend();
        System.out.println("好友系统初始化完成！");
    }
}
