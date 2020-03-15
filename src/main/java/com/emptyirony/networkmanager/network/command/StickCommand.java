package com.emptyirony.networkmanager.network.command;

import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import strafe.games.core.util.ItemBuilder;

/**
 * 2 * @Author: EmptyIrony
 * 3 * @Date: 2020/3/5 1:51
 * 4
 */
@CommandMeta(label = "lightingStick", permission = "panshi.admin")
public class StickCommand {

    public void execute(Player player) {
        ItemStack stick = new ItemBuilder(Material.STICK)
                .name("&b闪电棒")
                .shiny()
                .build();

        player.getInventory().addItem(stick);
    }
}
