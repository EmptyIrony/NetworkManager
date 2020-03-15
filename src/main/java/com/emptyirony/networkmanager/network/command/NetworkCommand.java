package com.emptyirony.networkmanager.network.command;

import com.emptyirony.networkmanager.network.server.ServerInfo;
import com.qrakn.honcho.command.CommandMeta;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.entity.Player;
import strafe.games.core.util.CC;
import strafe.games.core.util.ChatComponentBuilder;

/**
 * 2 * @Author: EmptyIrony
 * 3 * @Date: 2020/2/24 16:54
 * 4
 */
@CommandMeta(label = "network", permission = "panshi.mod", async = true)
public class NetworkCommand {
    public void execute(Player player) {
        if (ServerInfo.getCache().size() > 0) {
            player.sendMessage(CC.translate("&e在线的服务器&7(&b" + ServerInfo.getCache().size() + "&7)"));
            ServerInfo.getCache().forEach((s, serverInfo) -> {
                ChatComponentBuilder text = new ChatComponentBuilder(CC.translate("&e" + s + "&7 - &e在线人数: &b" + serverInfo.getPlayers().size() + "  &eMOTD状态: &b" + serverInfo.getMotd() + "  &eTPS状态: &b" + serverInfo.getTps()));
                text.setCurrentHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentBuilder(CC.translate("&6Click to teleport the server")).create()));
                text.setCurrentClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/server " + serverInfo.getServerName()));
                player.spigot().sendMessage(text.create());
            });
        } else {
            player.sendMessage(CC.translate("&c通讯错误，没有找到在线的服务器..."));
        }
    }
}
