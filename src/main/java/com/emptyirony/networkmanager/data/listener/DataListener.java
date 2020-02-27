package com.emptyirony.networkmanager.data.listener;

import cn.panshi.spigot.util.CC;
import com.emptyirony.networkmanager.data.PlayerData;
import me.neznamy.tab.api.TABAPI;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * 2 * @Author: EmptyIrony
 * 3 * @Date: 2020/2/24 9:04
 * 4
 */
public class DataListener implements Listener {

    @EventHandler
    public void onLogin(AsyncPlayerPreLoginEvent event) {
        new PlayerData(event.getUniqueId()).load();
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        new PlayerData(event.getPlayer().getUniqueId()).save(true);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        PlayerData data = new PlayerData(event.getPlayer().getUniqueId()).load();
        if (event.getPlayer().hasPermission("panshi.mod")) {
            if (data.getStaffOption().isNotify()) {
                event.getPlayer().sendMessage(CC.translate("&6你的员工详细日志已&a开启&6，如需关闭请输入/staffmode"));
            }
        }
        if (data.isNick()) {
            nick(event.getPlayer(), data.getNickedName());
        }
    }

    private void nick(Player player, String id) {
        ((CraftPlayer) player).setNickName(id);
        TABAPI.setCustomTabNameTemporarily(player.getUniqueId(), player.getDisplayName());

        PacketPlayOutEntityDestroy destroy = new PacketPlayOutEntityDestroy(player.getEntityId());
        EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        PacketPlayOutPlayerInfo playerInfo = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, entityPlayer);
        PacketPlayOutNamedEntitySpawn spawn = new PacketPlayOutNamedEntitySpawn(entityPlayer);

        for (Player target : Bukkit.getOnlinePlayers()) {
            if (target.equals(player)) {
                ((CraftPlayer) target).getHandle().playerConnection.sendPacket(destroy);
                ((CraftPlayer) target).getHandle().playerConnection.sendPacket(playerInfo);
                playerInfo = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, entityPlayer);
                ((CraftPlayer) target).getHandle().playerConnection.sendPacket(spawn);
                ((CraftPlayer) target).getHandle().playerConnection.sendPacket(playerInfo);
            }
        }
    }
}
