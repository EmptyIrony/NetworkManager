package com.emptyirony.networkmanager.friend;

import com.emptyirony.networkmanager.friend.command.FriendCommand;
import strafe.games.core.Stone;

/**
 * 2 * @Author: EmptyIrony
 * 3 * @Date: 2020/2/25 15:57
 * 4
 */
public class Friend {

    public Friend() {
        System.out.println("初始化好友系统...");
        Stone.get().getHoncho().registerCommand(new FriendCommand());

    }
}
