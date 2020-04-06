package com.emptyirony.networkmanager;

import com.emptyirony.networkmanager.bungee.util.CC;
import com.emptyirony.networkmanager.util.NetworkMessageUtil;
import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.entity.Player;
import strafe.games.core.util.ChatComponentBuilder;

/**
 * 2 * @Author: EmptyIrony
 * 3 * @Date: 2020/3/26 15:55
 * 4
 */
@CommandMeta(label = "alert")
public class TestCommand {
    public void execute(Player player, String text) {
        NetworkMessageUtil.sendMessageAlert(new ChatComponentBuilder(CC.translate("&7[&4&l全服公告&7] &f" + text)).create());
    }
}
