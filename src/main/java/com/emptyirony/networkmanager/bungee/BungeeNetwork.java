package com.emptyirony.networkmanager.bungee;

import com.emptyirony.networkmanager.bungee.command.*;
import com.emptyirony.networkmanager.bungee.data.ReportsData;
import com.emptyirony.networkmanager.bungee.database.MongoDB;
import com.emptyirony.networkmanager.bungee.listener.MessageListener;
import com.emptyirony.networkmanager.bungee.listener.PlayerListener;
import com.emptyirony.networkmanager.packet.*;
import com.emptyirony.networkmanager.pidgin.Pidgin;
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

        this.mongoDB = new MongoDB();
        ReportsData reportsData = new ReportsData();
        reportsData.load();

        this.pidgin = new Pidgin("network", "127.0.0.1", 6379, "");

        Arrays.asList(
                PacketHeartBeat.class,
                PacketStaffSwitchServer.class,
                PacketServerShutdown.class,
                PacketPlayerJoinOrQuit.class,
                PacketAlert.class
        ).forEach(pidgin::registerPacket);

        this.getProxy().getPluginManager().registerCommand(this, new RejoinCommand());
        this.getProxy().getPluginManager().registerCommand(this, new StaffCommand());
        this.getProxy().getPluginManager().registerCommand(this, new ReportCommand());
        this.getProxy().getPluginManager().registerCommand(this, new ModsCommand());
        this.getProxy().getPluginManager().registerListener(this, new PlayerListener());
        this.getProxy().getPluginManager().registerListener(this, new MessageListener());
        this.getProxy().getPluginManager().registerCommand(this, new ReportSystemCommand());
    }
}
