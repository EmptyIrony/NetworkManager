package com.emptyirony.networkmanager.network.command;

import com.emptyirony.networkmanager.NetworkManager;
import com.emptyirony.networkmanager.network.server.ServerInfo;
import com.emptyirony.networkmanager.packet.PacketServerShutdown;
import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.entity.Player;
import strafe.games.core.util.CC;

/**
 * 2 * @Author: EmptyIrony
 * 3 * @Date: 2020/3/9 22:43
 * 4
 */

@CommandMeta(label = "shutdown", permission = "panshimc.admin", async = true)
public class ShutdownCommand {

    public void execute(Player player, String server, String delayString, String reason) {
        ServerInfo targetServer = ServerInfo.getServerByName(server);
        if (targetServer == null) {
            player.sendMessage(CC.translate("&c目标服务器不存在或者已离线"));
            return;
        }
        int delay;
        try {
            delay = Integer.parseInt(delayString);
        } catch (Exception ignored) {
            player.sendMessage(CC.translate("&c延迟必须为正整数"));
            return;
        }
        if (delay < 10) {
            player.sendMessage(CC.translate("&c执行重启指令必须延迟10秒以上"));
            return;
        }

        PacketServerShutdown packet = new PacketServerShutdown(server, player.getName(), delay, CC.translate(reason));
        NetworkManager.getInstance().getPidgin().sendPacket(packet);


    }
}
