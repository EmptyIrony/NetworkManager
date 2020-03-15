package com.emptyirony.networkmanager.bungee.command;

import com.emptyirony.networkmanager.bungee.BungeeNetwork;
import com.emptyirony.networkmanager.bungee.util.CC;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 2 * @Author: EmptyIrony
 * 3 * @Date: 2020/3/15 2:37
 * 4
 */
public class StaffCommand extends Command {
    public StaffCommand() {
        super("staff");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender.hasPermission("panshi.helper")) {
            List<ProxiedPlayer> staffs = new ArrayList<>();
            for (ProxiedPlayer player : BungeeNetwork.getInstance().getProxy().getPlayers()) {
                if (player.hasPermission("panshi.helper")) {
                    if (player.getServer() != null && player.getServer().getInfo() != null) {
                        staffs.add(player);
                    }
                }
            }

            List<ProxiedPlayer> players = staffs.stream().sorted(Comparator.comparing(proxiedPlayer -> {
                if (proxiedPlayer.hasPermission("panshi.admin")) {
                    return 0;
                } else if (proxiedPlayer.hasPermission("panshi.mod")) {
                    return 1;
                } else if (proxiedPlayer.hasPermission("panshi.helper")) {
                    return 2;
                } else {
                    return 3;
                }
            })).collect(Collectors.toList());

            sender.sendMessage(CC.translate("&e在线的员工&7(&b" + players.size() + "&7)"));
            for (ProxiedPlayer player : players) {
                TextComponent textComponent = new TextComponent(CC.translate("&e" + player.getName() + "&7 - &e所在服务器: &b" + player.getServer().getInfo().getName()));
                textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/server " + player.getServer().getInfo().getName()));
                textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(CC.translate("&6点击传送到该玩家的服务器")).create()));
                sender.sendMessage(textComponent);
            }
        }
    }
}
