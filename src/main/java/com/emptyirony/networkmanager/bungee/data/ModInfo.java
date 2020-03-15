package com.emptyirony.networkmanager.bungee.data;

import lombok.Data;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 2 * @Author: EmptyIrony
 * 3 * @Date: 2020/2/26 11:13
 * 4
 */
@Data
public class ModInfo {

    @Getter
    private static Map<UUID, ModInfo> cache = new HashMap<>();

    private final UUID uuid;
    private final String name;
    private final Map<String, String> mods;

    public ModInfo(UUID uuid, String name, Map<String, String> mods) {
        this.uuid = uuid;
        this.name = name;
        this.mods = mods;

        System.out.println("recive a mods info");
        cache.put(uuid, this);
    }
}
