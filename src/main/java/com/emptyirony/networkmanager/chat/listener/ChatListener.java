package com.emptyirony.networkmanager.chat.listener;

import cn.panshi.spigot.util.CC;
import com.emptyirony.networkmanager.NetworkManager;
import com.emptyirony.networkmanager.data.PlayerData;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import strafe.games.core.util.ChatComponentBuilder;
import strafe.games.core.util.Cooldown;

import java.util.*;

/**
 * 2 * @Author: EmptyIrony
 * 3 * @Date: 2020/2/24 8:43
 * 4
 */
public class ChatListener implements Listener {
    private static String FORMAT;
    private static String BILIBILI_PATTERN = "av( )?[1-9]([0-9]{0,9})";
    private Map<UUID, Cooldown> cooldownMap;
    private Map<UUID, List<String>> lastChat;

    public ChatListener() {
        FORMAT = NetworkManager.getInstance().getConfig().getString("chat_format");
        this.cooldownMap = new HashMap<>();
        lastChat = new HashMap<>();
    }

    @EventHandler(ignoreCancelled = true)
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        if (!player.hasPermission("panshi.famous")) {
            lastChat.putIfAbsent(player.getUniqueId(), new ArrayList<>());
            List<String> list = lastChat.get(player.getUniqueId());
            String s = event.getMessage().toLowerCase();
            if (list.contains(s)) {
                player.sendMessage(CC.translate("&c请不要刷屏"));
                event.setCancelled(true);
                return;
            }
        }
        if (!player.hasPermission("panshi.vip") && cooldownMap.get(player.getUniqueId()) != null && !cooldownMap.get(player.getUniqueId()).hasExpired()) {
            event.setCancelled(true);
            player.sendMessage(CC.translate("&c聊天还在冷却中！购买&a石&c会员即可取消聊天冷却"));
            return;
        }
        if (player.hasPermission("panshi.vip")) {
            event.setFormat(PlaceholderAPI.setPlaceholders(player, FORMAT) + CC.translate("%1$s&f: %2$s"));
        } else {
            event.setFormat(PlaceholderAPI.setPlaceholders(player, FORMAT) + CC.translate("%1$s&7: %2$s"));
        }

        List<Player> ignored = new ArrayList<>();
        for (Player target : event.getRecipients()) {
            PlayerData data = new PlayerData(target.getUniqueId());
            if (data.getIgnored().contains(player.getName())) {
                ignored.add(target);
            }
        }
        event.getRecipients().removeAll(ignored);

        String message = event.getMessage();
        ChatComponentBuilder builder = new ChatComponentBuilder(message);
        if (message.matches(BILIBILI_PATTERN)) {

        }


        if (!player.hasPermission("panshi.vip")) {
            cooldownMap.put(player.getUniqueId(), new Cooldown(3 * 1000));
        }
        if (!player.hasPermission("panshi.famous")) {
            List<String> list = lastChat.get(player.getUniqueId());
            list.add(event.getMessage().toLowerCase());
            if (list.size() > 3) {
                list.remove(0);
            }
            lastChat.put(player.getUniqueId(), list);
        }
    }
}
