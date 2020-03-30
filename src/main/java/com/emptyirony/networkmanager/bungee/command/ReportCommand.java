package com.emptyirony.networkmanager.bungee.command;

import com.emptyirony.networkmanager.bungee.BungeeNetwork;
import com.emptyirony.networkmanager.bungee.data.ReportsData;
import com.emptyirony.networkmanager.bungee.data.sub.ReportData;
import com.emptyirony.networkmanager.bungee.util.CC;
import com.emptyirony.networkmanager.bungee.util.ChatComponentBuilder;
import com.emptyirony.networkmanager.bungee.util.Cooldown;
import com.emptyirony.networkmanager.bungee.util.UUIDUtil;
import com.emptyirony.networkmanager.util.NetworkMessageUtil;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

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

                String id = UUIDUtil.generateShortUuid();

                ReportData reportData = new ReportData(id, player.getUniqueId().toString(), target.getUniqueId().toString(), builder.toString());
                ReportsData.get().getActiveReports().add(reportData);
                ReportsData.get().save();

                cooldownMap.put(player.getUniqueId(), new Cooldown(1000 * 30));
                player.sendMessage(CC.translate("&a我们已经接到了您对&e" + target.getName() + "&a的举报，原因是&e" + builder.toString()));
                player.sendMessage(CC.translate("&a您的举报已经提交给所有的在线工作人员和反作弊，我们知道您一样不喜欢破坏规则的玩家，我们将在第一时间为您处理"));
                player.sendMessage(CC.translate("&a您的举报回执码为: &e" + id + "&a,您可以使用回执码进行举报进度查询"));
                player.sendMessage(CC.translate("&a关于聊天举报，我们推荐您使用&e/chatreport 玩家名"));

                TextComponent chat = new TextComponent(CC.translate("&7举报编号: &9" + id));
                chat.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("&9点击复制举报编号").create()));
                chat.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, id));

                TextComponent chatComponentBuilder = new TextComponent(CC.translate("&9[REPORT] &7" + player.getName() + "&9 举报了 &7" + target.getName() + "&7,理由: &9" + builder.toString()));
                chatComponentBuilder.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("&9点击传送到目标服务器").create()));
                chatComponentBuilder.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/server " + target.getServer().getInfo().getName()));

                TextComponent result1 = new TextComponent(CC.translate("&7处理结果反馈: "));
                TextComponent result2 = new TextComponent(CC.translate("&9[&c&l确认违规&9] "));
                TextComponent result3 = new TextComponent(CC.translate("&9[&7&l无法确认&9] "));
                TextComponent result4 = new TextComponent(CC.translate("&9[&a&l未违规&9] "));

                result2.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/reportsystem sure " + id));
                result3.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/reportsystem notsure " + id));
                result4.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/reportsystem not " + id));

                result1.addExtra(result2);
                result1.addExtra(result3);
                result1.addExtra(result4);

                ChatComponentBuilder componentBuilder = new ChatComponentBuilder("").append(result1);

                NetworkMessageUtil.sendMessageWithPermission("panshi.mod", componentBuilder.create());
            } else {
                player.sendMessage(CC.translate("&c举报还在冷却中.."));
            }
        }
    }
}
