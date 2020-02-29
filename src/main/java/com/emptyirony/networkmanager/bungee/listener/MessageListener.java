package com.emptyirony.networkmanager.bungee.listener;

import com.emptyirony.networkmanager.bungee.data.ModInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 2 * @Author: EmptyIrony
 * 3 * @Date: 2020/2/26 11:14
 * 4
 */
public class MessageListener implements Listener {
    @EventHandler
    public void onMessage(PluginMessageEvent event) {
        byte[] bytes = event.getData();
        if (bytes != null) {
            if (bytes[0] == 2) {
                Map<String, String> mods = getModData(bytes);
                ProxiedPlayer player = (ProxiedPlayer) event.getSender();
                new ModInfo(player.getUniqueId(), player.getName(), mods);

//                boolean illegal = false;
//                String bMod = "";
//
//
//                if (illegal){
//                    player.disconnect(new ComponentBuilder("您正在使用黑名单模组："+bMod+"，请删除后再重新尝试加入服务器").create());
//                }
            }
        }
    }

    private Map<String, String> getModData(byte[] data) {
        Map<String, String> mods = new HashMap<>();

        boolean store = false;
        String tempName = null;

        for (int i = 2; i < data.length; store = !store) {
            int end = i + data[i] + 1;
            byte[] range = Arrays.copyOfRange(data, i + 1, end);

            String string = new String(range);

            if (store) {
                mods.put(tempName, string);
            } else {
                tempName = string;
            }

            i = end;
        }

        return mods;
    }
}