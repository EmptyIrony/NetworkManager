package com.emptyirony.networkmanager.network.command;

import com.emptyirony.networkmanager.network.server.ServerInfo;
import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.entity.Player;
import strafe.games.core.util.CC;

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
                player.sendMessage(CC.translate("&e" + s + "&7 - &e在线人数: &b" + serverInfo.getPlayers().size() + "  &eMOTD状态: &b" + serverInfo.getMotd()));
            });
        } else {
            player.sendMessage(CC.translate("&c通讯错误，没有找到在线的服务器..."));
        }
    }
}
