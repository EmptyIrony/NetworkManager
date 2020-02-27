package com.emptyirony.networkmanager.bungee.command;

import com.emptyirony.networkmanager.bungee.BungeeNetwork;
import com.emptyirony.networkmanager.bungee.data.ModInfo;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.stream.Collectors;

/**
 * 2 * @Author: EmptyIrony
 * 3 * @Date: 2020/2/26 11:13
 * 4
 */
public class ModsCommand extends Command implements TabExecutor {
    public ModsCommand() {
        super("mods");
    }

    @Override
    public String[] getAliases() {
        return new String[]{"mods", "mod", "checkmod"};
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!sender.hasPermission("urbane.mod")) {
            return;
        }

        if (args.length != 1) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c用法：/mods <用户名>"));
            return;
        }

        ProxiedPlayer player = BungeeNetwork.getInstance().getProxy().getPlayer(args[0]);
        if (player == null) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c那名玩家不在线"));
            return;
        }

        ModInfo user = ModInfo.getCache().get(player.getUniqueId());
        if (user == null) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c我们仍在与该名玩家建立FML通讯，请等待5秒再试"));
            return;
        }
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b" + user.getName() + "&e 正在使用的Mod(&6" + user.getMods().size() + "&e)"));

        user.getMods().forEach((modid, version) -> {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7 - &e" + modid + " &f版本：&e" + version));
        });
    }


    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        final String lastArg = (args.length > 0) ? args[args.length - 1].toLowerCase() : "";
        return ProxyServer.getInstance().getPlayers().stream().filter(player -> player.getName().toLowerCase().startsWith(lastArg)).collect(Collectors.toList()).stream().map(player -> player.getName()).collect(Collectors.toList());
    }
}
