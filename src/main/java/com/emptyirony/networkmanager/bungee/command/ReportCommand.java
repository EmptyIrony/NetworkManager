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
import strafe.games.core.util.Cooldown;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 2 * @Author: EmptyIrony
 * 3 * @Date: 2020/3/5 17:10
 * 4
 */
public class ReportCommand extends Command {
    private Map<UUID, Cooldown> cooldownMap = new HashMap<>();

    public ReportCommand() {
        super("report");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer) sender;
            if (args.length < 2) {
                player.sendMessage(CC.translate("&c举报指令用法: /report <玩家ID> <举报玩家>"));
                return;
            }
            ProxiedPlayer target = BungeeNetwork.getInstance().getProxy().getPlayer(args[0]);
            if (target == null) {
                player.sendMessage(CC.translate("&c目标玩家不在线"));
                return;
            }
            if (player.equals(target)) {
                player.sendMessage(CC.translate("&c你不能举报自己！"));
                return;
            }
            StringBuilder builder = new StringBuilder();
            for (int i = 1; i < args.length; i++) {
                builder.append(args[i])
                        .append(" ");
            }
            if (cooldownMap.get(player.getUniqueId()) == null || cooldownMap.get(player.getUniqueId()).hasExpired()) {
                cooldownMap.put(player.getUniqueId(), new Cooldown(1000 * 30));
                player.sendMessage(CC.translate("&a我们已经接到了您对&e" + target.getName() + "&a的举报，原因是&e" + builder.toString()));
                player.sendMessage(CC.translate("&a您的举报已经提交给所有的在线工作人员和反作弊，我们知道您一样不喜欢破坏规则的玩家，我们将在第一时间为您处理"));
                player.sendMessage(CC.translate("&a关于聊天举报，我们推荐您使用&e/chatreport 玩家名"));

                TextComponent chatComponentBuilder = new TextComponent(CC.translate("&9[REPORT] &7" + player.getName() + "&9 举报了 &7" + target.getName() + "&7,理由: &9" + builder.toString()));
                chatComponentBuilder.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("&9CLICK TO TELEPORT THE SERVER").create()));
                chatComponentBuilder.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/server " + target.getServer().getInfo().getName()));

                for (ProxiedPlayer proxiedPlayer : BungeeNetwork.getInstance().getProxy().getPlayers()) {
                    if (proxiedPlayer.hasPermission("panshi.mod")) {
                        proxiedPlayer.sendMessage(chatComponentBuilder);
                    }
                }
            } else {
                player.sendMessage(CC.translate("&c举报还在冷却中.."));
            }
        }
    }
}
