package com.emptyirony.networkmanager.network.listener;

import com.emptyirony.networkmanager.NetworkManager;
import net.minecraft.server.v1_8_R3.PacketPlayOutExplosion;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import net.minecraft.server.v1_8_R3.Vec3D;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.inventivetalent.packetlistener.handler.PacketHandler;
import org.inventivetalent.packetlistener.handler.ReceivedPacket;
import org.inventivetalent.packetlistener.handler.SentPacket;

import java.util.ArrayList;

/**
 * 2 * @Author: EmptyIrony
 * 3 * @Date: 2020/3/11 15:44
 * 4
 */
public class CrashListener extends PacketHandler {
    @Override
    public void onSend(SentPacket sentPacket) {

    }

    @Override
    public void onReceive(ReceivedPacket packet) {
        if (packet.getPacketName().equals("PacketPlayInCustomPayload")) {
            final Object packetDataSerializer = packet.getPacketValue("b");
            try {
                final int size = (int) packetDataSerializer.getClass().getMethod("readableBytes", new Class[0]).invoke(packetDataSerializer, new Object[0]);
                if (size > 30000) {
                    packet.setCancelled(true);
                    final Player player = Bukkit.getPlayerExact(packet.getPlayername());
                    if (player != null) {
                        new BukkitRunnable() {
                            public void run() {
                                player.getWorld().strikeLightningEffect(player.getLocation());
                                Location location = player.getLocation();
                                PacketPlayOutExplosion crashPacket = new PacketPlayOutExplosion(location.getX(), location.getY(), location.getZ(), Float.MAX_VALUE, new ArrayList<>(), new Vec3D(Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE));
                                PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "ban " + player.getName() + " 1y Crashed Server -s");

                                for (int i = 0; i < 10; i++) {
                                    connection.sendPacket(crashPacket);
                                }
                            }
                        }.runTask(NetworkManager.getInstance());
                    }
                }
            } catch (Exception ignored) {
            }
        }
    }
}
