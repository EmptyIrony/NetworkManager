package com.emptyirony.networkmanager.chat.command;

import com.emptyirony.networkmanager.data.PlayerData;
import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.entity.Player;
import strafe.games.core.util.CC;

/**
 * 2 * @Author: EmptyIrony
 * 3 * @Date: 2020/2/26 21:27
 * 4
 */
@CommandMeta(label = "stream", permission = "panshi.yt", async = true)
public class StreamCommand {
    public void execute(Player player) {
        PlayerData data = PlayerData.getByUuid(player.getUniqueId());
        if (data.getPlayerOption().isStream()) {
            data.getPlayerOption().setStream(false);
            player.sendMessage(CC.translate("&a你已退出直播模式，请注意其他玩家私聊发送给你的&c不良信息&a！"));
        } else {
            data.getPlayerOption().setStream(true);
            player.sendMessage(CC.translate("&a你已进入直播模式，自动屏蔽所有来自玩家的消息，注意安全，朋友！"));
        }
        data.save(false);
    }
}
