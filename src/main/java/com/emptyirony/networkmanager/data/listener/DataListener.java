package com.emptyirony.networkmanager.data.listener;

import cn.panshi.spigot.util.CC;
import com.emptyirony.networkmanager.data.PlayerData;
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
        new PlayerData(event.getUniqueId());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        new PlayerData(event.getPlayer().getUniqueId()).save(true);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        PlayerData data = new PlayerData(event.getPlayer().getUniqueId());
        if (event.getPlayer().hasPermission("panshi.mod")) {
            if (data.isNotify()) {
                event.getPlayer().sendMessage(CC.translate("&6你的员工详细日志已&a开启&6，如需关闭请输入/staffmode"));
            }
        }
    }
}
