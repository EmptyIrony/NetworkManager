package com.emptyirony.networkmanager.bungee.listener;

import com.emptyirony.networkmanager.bungee.BungeeNetwork;
import com.emptyirony.networkmanager.bungee.data.LoginData;
import com.emptyirony.networkmanager.packet.PacketStaffSwitchServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.protocol.packet.PluginMessage;

import java.util.concurrent.TimeUnit;

/**
 * 2 * @Author: EmptyIrony
 * 3 * @Date: 2020/2/26 10:00
 * 4
 */
public class PlayerListener implements Listener {

    @EventHandler
    public void onQuit(PlayerDisconnectEvent event) {
        if (event.getPlayer().hasPermission("panshi.mod")) {
            ProxiedPlayer player = event.getPlayer();
            PacketStaffSwitchServer packet = new PacketStaffSwitchServer(player.getDisplayName(), event.getPlayer().getServer().getInfo().getName(), "", 0, System.currentTimeMillis());
            BungeeNetwork.getInstance().getPidgin().sendPacket(packet);
        }
        LoginData.getByUuid(event.getPlayer().getUniqueId()).save(true);
    }

    @EventHandler
    public void onJoin(PostLoginEvent event) {
        ProxiedPlayer player = event.getPlayer();
        new LoginData(player.getUniqueId()).load();

        BungeeNetwork.getInstance().getProxy().getScheduler().schedule(BungeeNetwork.getInstance(), () -> {
            if (player.hasPermission("panshi.mod")) {
                PacketStaffSwitchServer packet = new PacketStaffSwitchServer(player.getDisplayName(), "", event.getPlayer().getServer().getInfo().getName(), 1, System.currentTimeMillis());
                BungeeNetwork.getInstance().getPidgin().sendPacket(packet);
            }

            sendFmlPacket(player, (byte) -2, (byte) 0);
            sendFmlPacket(player, (byte) 0, (byte) 2, (byte) 0, (byte) 0, (byte) 0, (byte) 0);
            sendFmlPacket(player, (byte) 2, (byte) 0, (byte) 0, (byte) 0, (byte) 0);

        }, 3, TimeUnit.SECONDS);
    }

    private void sendFmlPacket(ProxiedPlayer player, byte... data) {
        player.unsafe().sendPacket(new PluginMessage("FML|HS", data, false));
    }

    @EventHandler
    public void onSwitchServer(ServerSwitchEvent event) {
        ProxiedPlayer player = event.getPlayer();
        if (player.hasPermission("panshi.mod")) {
            String from = player.getServer().getInfo().getName();
            String[] split = from.split("_");
            if (split.length >= 3) {
                String type = split[0];
                String name = split[1];

                LoginData data = LoginData.getByUuid(player.getUniqueId());
                if (type.equalsIgnoreCase("hub")) {
                    data.setLastLobby(from);
                } else if (type.equalsIgnoreCase("games")) {
                    data.setLastGame(from);
                }
                data.save(false);
            }
            BungeeNetwork.getInstance().getProxy().getScheduler().schedule(BungeeNetwork.getInstance(), new Runnable() {
                @Override
                public void run() {
                    PacketStaffSwitchServer packet = new PacketStaffSwitchServer(player.getDisplayName(), from, player.getServer().getInfo().getName(), 2, System.currentTimeMillis());
                    BungeeNetwork.getInstance().getPidgin().sendPacket(packet);
                }
            }, 50, TimeUnit.MILLISECONDS);
        }
    }
}
