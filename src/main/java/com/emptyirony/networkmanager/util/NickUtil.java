package com.emptyirony.networkmanager.util;

import com.mojang.authlib.GameProfile;
import lombok.SneakyThrows;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;

/**
 * 2 * @Author: EmptyIrony
 * 3 * @Date: 2020/3/9 10:45
 * 4
 */
public class NickUtil {
    @SneakyThrows
    public static void nick(Player player, String id, String prefix) {
        Field name = GameProfile.class.getDeclaredField("name");
        name.setAccessible(true);
        GameProfile profile = ((CraftPlayer) player).getProfile();
        name.set(profile, id);
        player.setDisplayName(id);
        player.setPlayerListName(id);
        player.setCustomName(id);
    }
}
