package com.emptyirony.networkmanager.guild;

import com.emptyirony.networkmanager.guild.command.GuildCommand;
import strafe.games.core.Stone;

/**
 * 2 * @Author: EmptyIrony
 * 3 * @Date: 2020/2/27 9:39
 * 4
 */
public class Guild {
    public Guild() {
        System.out.println("初始化公会系统...");
        Stone.get().getHoncho().registerCommand(new GuildCommand());
    }
}
