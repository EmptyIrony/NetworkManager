package com.emptyirony.networkmanager.bungee.command;

import com.emptyirony.networkmanager.bungee.BungeeNetwork;
import com.emptyirony.networkmanager.bungee.data.LoginData;
import com.emptyirony.networkmanager.bungee.data.ReportsData;
import com.emptyirony.networkmanager.bungee.data.sub.ReportData;
import com.emptyirony.networkmanager.bungee.util.CC;
import com.emptyirony.networkmanager.util.NetworkMessageUtil;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.Optional;
import java.util.UUID;

/**
 * 2 * @Author: EmptyIrony
 * 3 * @Date: 2020/3/20 17:29
 * 4
 */
public class ReportSystemCommand extends Command {
    public ReportSystemCommand() {
        super("reportsystem");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender.hasPermission("panshi.mod") && sender instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer) sender;

            if (args.length == 0) {
                return;
            }

            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("show") || args[0].equalsIgnoreCase("list")) {
                    BungeeNetwork.getInstance().getProxy().getScheduler().runAsync(BungeeNetwork.getInstance(), () -> {
                        sender.sendMessage(CC.translate(CC.translate("&e待处理的举报&7(&b" + ReportsData.get().getActiveReports().size() + "&7)")));
                        for (ReportData activeReport : ReportsData.get().getActiveReports()) {
                            String id = LoginData.getByUuid(UUID.fromString(activeReport.getTarget())).getName();

                            TextComponent chatComponentBuilder = new TextComponent(CC.translate("&9[REPORT] &7" + player.getName() + "&9 举报了 &7" + id + "&7,理由: &9" + activeReport.getReason()));

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

                            player.sendMessage(chatComponentBuilder);
                            player.sendMessage(result1);
                        }
                    });
                }
            } else {
                if (args[0].equalsIgnoreCase("sure")) {
                    if (args.length != 2) {
                        return;
                    }
                    String name = args[1];
                    Optional<ReportData> first = ReportsData.get().getActiveReports().stream().filter(reportData -> reportData.getId().equalsIgnoreCase(name)).findFirst();
                    if (first.isPresent()) {
                        ReportData reportData = first.get();
                        reportData.setReceiver(player.getName());
                        reportData.setResult("BANNED");
                        ReportsData.get().getActiveReports().remove(reportData);
                        ReportsData.get().getAccepted().add(reportData);
                        ReportsData.get().save();
                        NetworkMessageUtil.sendMessageWithPermission("panshi.mod", "&e" + player.getName() + "&c 处理了一个举报\n&cID: &e" + name + "\n&c处理结果: &eBANNED");
                        String reporter = reportData.getReporter();
                        LoginData data = LoginData.getByUuid(UUID.fromString(reporter));
                        if (data != null) {
                            NetworkMessageUtil.sendMessageToPlayer(data.getName(), CC.translate("&c您的一个举报已被处理并给予处罚，感谢您对社区环境的贡献"));
                        }
                    } else {
                        player.sendMessage(CC.translate("&c没有找到相关举报"));
                    }
                }
            }
        }
    }
}
