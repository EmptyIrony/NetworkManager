package com.emptyirony.networkmanager.bungee;

import com.emptyirony.networkmanager.bungee.command.ModsCommand;
import com.emptyirony.networkmanager.bungee.command.RejoinCommand;
import com.emptyirony.networkmanager.bungee.database.MongoDB;
import com.emptyirony.networkmanager.bungee.listener.MessageListener;
import com.emptyirony.networkmanager.bungee.listener.NetworkListener;
import com.emptyirony.networkmanager.bungee.listener.PlayerListener;
import com.emptyirony.networkmanager.packet.PacketFriendRequest;
import com.emptyirony.networkmanager.packet.PacketHeartBeat;
import com.emptyirony.networkmanager.packet.PacketStaffMsg;
import com.emptyirony.networkmanager.packet.PacketStaffSwitchServer;
import com.minexd.pidgin.Pidgin;
import lombok.Getter;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.Arrays;

/**
 * 2 * @Author: EmptyIrony
 * 3 * @Date: 2020/2/26 9:55
 * 4
 */
@Getter
public class BungeeNetwork extends Plugin {
    @Getter
    private static BungeeNetwork instance;
    private Pidgin pidgin;
    private MongoDB mongoDB;

    @Override
    public void onEnable() {
        instance = this;
        pidgin = new Pidgin("network", "127.0.0.1", 6379, null);
        Arrays.asList(
                PacketFriendRequest.class,
                PacketHeartBeat.class,
                PacketStaffMsg.class,
                PacketStaffSwitchServer.class
        ).forEach(pidgin::registerPacket);

        pidgin.registerListener(new NetworkListener());

        mongoDB = new MongoDB();

        this.getProxy().getPluginManager().registerCommand(this, new RejoinCommand());
        this.getProxy().getPluginManager().registerCommand(this, new ModsCommand());
        this.getProxy().getPluginManager().registerListener(this, new PlayerListener());
        this.getProxy().getPluginManager().registerListener(this, new MessageListener());
    }
}
