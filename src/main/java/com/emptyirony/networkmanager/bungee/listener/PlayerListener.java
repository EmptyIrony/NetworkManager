package com.emptyirony.networkmanager.bungee.listener;

import com.emptyirony.networkmanager.bungee.BungeeNetwork;
import com.emptyirony.networkmanager.bungee.data.LoginData;
import com.emptyirony.networkmanager.bungee.util.CC;
import com.emptyirony.networkmanager.packet.PacketPlayerJoinOrQuit;
import com.emptyirony.networkmanager.packet.PacketStaffSwitchServer;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ServerConnectedEvent;
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
            if (event.getPlayer().getServer() == null) {
                return;
            }
            PacketStaffSwitchServer packet = new PacketStaffSwitchServer(player.getDisplayName(), event.getPlayer().getServer().getInfo().getName(), "", 0, System.currentTimeMillis());
            BungeeNetwork.getInstance().getPidgin().sendPacket(packet);
        }
        LoginData.getByUuid(event.getPlayer().getUniqueId()).save(true);

        PacketPlayerJoinOrQuit packet = new PacketPlayerJoinOrQuit(event.getPlayer().getName(), false);
        BungeeNetwork.getInstance().getPidgin().sendPacket(packet);
    }

    @EventHandler
    public void onJoin(PostLoginEvent event) {
        ProxiedPlayer player = event.getPlayer();
        new LoginData(player.getUniqueId(), player.getName()).load();

        BungeeNetwork.getInstance().getProxy().getScheduler().schedule(BungeeNetwork.getInstance(), () -> {
            sendFmlPacket(player, (byte) -2, (byte) 0);
            sendFmlPacket(player, (byte) 0, (byte) 2, (byte) 0, (byte) 0, (byte) 0, (byte) 0);
            sendFmlPacket(player, (byte) 2, (byte) 0, (byte) 0, (byte) 0, (byte) 0);
        }, 5, TimeUnit.SECONDS);

        PacketPlayerJoinOrQuit packet = new PacketPlayerJoinOrQuit(event.getPlayer().getName(), true);
        BungeeNetwork.getInstance().getPidgin().sendPacket(packet);
    }

    private void sendFmlPacket(ProxiedPlayer player, byte... data) {
        player.unsafe().sendPacket(new PluginMessage("FML|HS", data, false));
    }

    @EventHandler
    public void onDisconnected(PlayerDisconnectEvent event) {
        ProxiedPlayer player = event.getPlayer();
        if (player.getServer() == null) {
            return;
        }
        String from = player.getServer().getInfo().getName();
        String[] split = from.split("_");
        if (split.length >= 3) {
            String type = split[0];

            LoginData data = LoginData.getByUuid(player.getUniqueId());
            if (type == null) {
                return;
            }
            if (type.equalsIgnoreCase("hub")) {
                data.setLastLobby(from);
            } else if (type.equalsIgnoreCase("games")) {
                data.setLastGame(from);
            }
            data.save(true);
        }
    }

    @EventHandler
    public void onConnect(final ServerConnectedEvent e) {
        final ProxiedPlayer p = e.getPlayer();
        if (!p.hasPermission("panshi.helper"))
            return;
        for (final ProxiedPlayer to : ProxyServer.getInstance().getPlayers()) {
            if (to.hasPermission("panshi.helper") && p.getServer() != null) {
                to.sendMessage(CC.translate(p.getName() + "&e 从 &3" + p.getServer().getInfo().getName() + "&e 更变到&3 " + e.getServer().getInfo().getName() + " &e(员工追踪)"));
            }
        }
    }

    @EventHandler
    public void onSwitchServer(ServerSwitchEvent event) {
        ProxiedPlayer player = event.getPlayer();
        String from = player.getServer().getInfo().getName();
        String[] split = from.split("_");
        if (split.length >= 3) {
            String type = split[0];

            LoginData data = LoginData.getByUuid(player.getUniqueId());
                if (type.equalsIgnoreCase("hub")) {
                    data.setLastLobby(from);
                } else if (type.equalsIgnoreCase("games")) {
                    data.setLastGame(from);
                }
                data.save(false);
            }
    }
}
