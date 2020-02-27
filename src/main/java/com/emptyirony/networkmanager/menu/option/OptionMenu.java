package com.emptyirony.networkmanager.menu.option;

import org.bukkit.entity.Player;
import strafe.games.core.util.menu.Button;
import strafe.games.core.util.menu.Menu;

import java.util.HashMap;
import java.util.Map;

/**
 * 2 * @Author: EmptyIrony
 * 3 * @Date: 2020/2/26 20:23
 * 4
 */
public class OptionMenu extends Menu {
    @Override
    public String getTitle(Player player) {
        return "&a玩家设置";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> button = new HashMap<>();


        return button;
    }
}
