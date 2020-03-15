package com.emptyirony.networkmanager.bungee.command;

import com.emptyirony.networkmanager.bungee.BungeeNetwork;
import com.emptyirony.networkmanager.bungee.data.LoginData;
import com.emptyirony.networkmanager.bungee.util.CC;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

/**
 * 2 * @Author: EmptyIrony
 * 3 * @Date: 2020/2/26 10:39
 * 4
 */
public class RejoinCommand extends Command {
    public RejoinCommand() {
        super("rejoin");
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if (!(commandSender instanceof ProxiedPlayer)) {
            commandSender.sendMessage("player only");
            return;
        }

        ProxiedPlayer player = (ProxiedPlayer) commandSender;
        LoginData data = LoginData.getByUuid(player.getUniqueId());
        if (data.getLastGame() != null && !data.getLastGame().equals("UNKNOW")) {
            String server = data.getLastGame();
            if (player.getServer().getInfo().getName().equalsIgnoreCase(data.getLastGame())) {
                player.sendMessage(CC.translate("&c你已经在该服务器上了"));
                return;
            }
            if (player.getServer().getInfo().getName().split("_")[0].equalsIgnoreCase("games")) {
                player.sendMessage(CC.translate("&c你已经在该服务器上了"));
                return;
            }
            player.connect(BungeeNetwork.getInstance().getProxy().getServerInfo(server), (done, throwable) -> {
                if (!done) {
                    player.connect(BungeeNetwork.getInstance().getProxy().getServerInfo(data.getLastLobby()));
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c重新连接时发生了一点意外，请报告给管理员: " + throwable.toString()));
                } else {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a成功重新加入游戏！"));
                }
            });
        } else {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&Hey！我们正在尝试找到你上一场游戏的数据但是...真的找不到了?(好像)"));
        }
    }

    @Override
    public String[] getAliases() {
        return new String[]{"rejoin", "rej", "back"};
    }
}
