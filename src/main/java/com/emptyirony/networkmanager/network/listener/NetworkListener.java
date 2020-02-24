package com.emptyirony.networkmanager.network.listener;

import com.emptyirony.networkmanager.NetworkManager;
import com.emptyirony.networkmanager.network.packet.PacketHeartBeat;
import com.emptyirony.networkmanager.network.server.ServerInfo;
import com.minexd.pidgin.packet.handler.IncomingPacketHandler;
import com.minexd.pidgin.packet.listener.PacketListener;

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
        new ServerInfo(packet.getName(), packet.getOnlinePlayers(), packet.getMotd());
    }


}
