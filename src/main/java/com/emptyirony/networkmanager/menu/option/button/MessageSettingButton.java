package com.emptyirony.networkmanager.menu.option.button;

import com.emptyirony.networkmanager.data.PlayerData;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import strafe.games.core.util.menu.Button;

/**
 * 2 * @Author: EmptyIrony
 * 3 * @Date: 2020/2/26 20:32
 * 4
 */
public class MessageSettingButton extends Button {
    @Override
    public ItemStack getButtonItem(Player player) {
        PlayerData data = PlayerData.getByUuid(player.getUniqueId());
        return null;
    }
}
