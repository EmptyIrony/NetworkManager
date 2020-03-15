package com.emptyirony.networkmanager.network.listener;

import com.emptyirony.networkmanager.data.PlayerData;
import com.emptyirony.networkmanager.util.NickUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import strafe.games.core.profile.Profile;

/**
 * 2 * @Author: EmptyIrony
 * 3 * @Date: 2020/3/9 10:47
 * 4
 */
public class PlayerListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onQuit(PlayerQuitEvent event) {
        PlayerData data = PlayerData.getByUuid(event.getPlayer().getUniqueId());
        Profile profile = Profile.getByUuid(event.getPlayer().getUniqueId());
        if (data.isNick()) {
            NickUtil.nick(event.getPlayer(), profile.getName(), "&7");
        }

    }
}
